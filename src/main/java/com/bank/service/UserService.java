package com.bank.service;

import com.bank.exceptions.InvalidOperationException;
import com.bank.exceptions.ResourceNotFoundException;
import com.bank.models.Rating;
import com.bank.models.User;
import com.bank.repositories.UserRepository;
import com.bank.security.JWTEntity;
import com.bank.utils.enums.Role;
import com.bank.utils.enums.UserRank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getAuthorizedUser(){
        JWTEntity jwtEntity = (JWTEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(jwtEntity.getId()).orElseThrow(() -> new ResourceNotFoundException("Not authorized!"));
    }

    public User getById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User whit this id not found!"));
    }

    public User getByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("User with this username not found!"));
    }

    public User getByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User with this email not found!"));
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    @Transactional
    public User save(User user){
        if (userRepository.findByUsername(user.getUsername()).isPresent())
            throw new IllegalStateException("User with this username already exists");
        if (userRepository.findByEmail(user.getEmail()).isPresent())
            throw new IllegalStateException("User with this email already exists");
        user.setRoles(new HashSet<>(List.of(Role.ROLE_USER)));
        user.setEnabled(false);
        user.setConfirmationCode(generateCofirmationCode());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRating(Rating.builder()
                        .points(0)
                        .rank(UserRank.NEW_MEMBER)
                        .user(user)
                        .id(user.getId())
                .build());
        return userRepository.save(user);
    }

    @Transactional
    public User update(Long id, User user) {
        User user1 = getById(id);

        user.setId(user1.getId());

        return userRepository.save(user1);
    }

    @Transactional
    public void deleteById(Long aLong) {
        userRepository.deleteById(aLong);
    }

    private String generateCofirmationCode(){
        Integer code = (int)(1000+Math.random()*9000);
        System.out.println("Confirmation code");
        System.out.println(code);
        return code.toString();
    }

    @Transactional
    public void enable(String email, String confirmationCode){
        User user = getByEmail(email);
        if (!user.getConfirmationCode().equals(confirmationCode))
            throw new InvalidOperationException("Code and code confirmation doesnt match");
        user.setEnabled(true);
        userRepository.save(user);
    }
}
