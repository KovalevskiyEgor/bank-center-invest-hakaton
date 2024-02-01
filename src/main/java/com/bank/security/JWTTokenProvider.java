package com.bank.security;

import com.bank.utils.enums.Role;
import com.bank.models.User;
import com.bank.props.JWTProperties;
import com.bank.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JWTTokenProvider {
    private final JWTProperties jwtProperties;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private Key key;

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(final Long userId, final String username, final Set<Role> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId);
        claims.put("roles", resolveRoles(roles));
        Date now = new Date();
        Date validity = new Date(now.getTime()+jwtProperties.getAccess());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long userId, String username){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId);
        Date now = new Date();
        Date validity = new Date(now.getTime()+jwtProperties.getRefresh());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public JWTResponse refreshUserTokens(String refreshToken){
        if (!validateToken(refreshToken)){
            System.out.println("Access Denied"); // TODO
        }
        Long id = Long.valueOf(getIdFromToken(refreshToken));
        User user = userService.getById(id);
        return JWTResponse.builder()
                .id(id)
                .username(user.getUsername())
                .accessToken(createAccessToken(user.getId(),user.getUsername(), Set.of(Role.ROLE_USER)))
                .refreshToken(createRefreshToken(user.getId(), user.getUsername()))
                .build();
    }

    public boolean validateToken(String token){
        Jws<Claims> claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

    private String getIdFromToken(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id")
                .toString();
    }

    public Authentication getAuthenctication(String token){
        String username = getUsernameFromToken(token);
        UserDetails userDetails =  userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsernameFromToken(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private List<String> resolveRoles(Set<Role> roles) {
        return roles.stream().map(Enum::name).toList();
    }


}
