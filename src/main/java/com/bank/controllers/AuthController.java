package com.bank.controllers;

import com.bank.dto.UserDTO;
import com.bank.models.User;
import com.bank.utils.enums.EmailType;
import com.bank.security.JWTRequest;
import com.bank.security.JWTResponse;
import com.bank.service.AuthService;
import com.bank.service.EmailService;
import com.bank.service.UserService;
import com.bank.utils.mappers.impl.UserMapper;
import com.bank.validators.UserDTOValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController extends MainController{
    private final AuthService authService;
    private final UserService userService;
    private final UserDTOValidator userValidator;
    private final UserMapper userMapper;
    private final EmailService emailService;

    @PostMapping("/login")
    public JWTResponse login(@RequestBody JWTRequest jwtRequest){
        return authService.login(jwtRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserDTO userDTO, BindingResult bindingResult){
        userValidator.validate(userDTO, bindingResult);
        checkBindingResult(bindingResult);
        User user = userMapper.fromDTO(userDTO);
        userService.save(user);
        emailService.sendEmailMessage(user, EmailType.REGISTRATION, new Properties());
        return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.CREATED);
    }

    @PostMapping("/enable")
    public ResponseEntity<Object> enableUser(@RequestBody UserDTO userDTO){
        userService.enable(userDTO.getEmail(), userDTO.getConfirmationCode());
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }

}
