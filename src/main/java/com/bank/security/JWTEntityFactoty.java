package com.bank.security;


import com.bank.utils.enums.Role;
import com.bank.models.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

public class JWTEntityFactoty {
    public static JWTEntity createJWTEntity(User user){
        return JWTEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .enabled(user.isEnabled())
                .authorities(mapToGrantedAuthorities(user.getRoles()))
                .build();
    }

    private static List<SimpleGrantedAuthority> mapToGrantedAuthorities(final Set<Role> roles) {
        return roles.stream().map(Enum::name).map(SimpleGrantedAuthority::new).toList();
    }
}
