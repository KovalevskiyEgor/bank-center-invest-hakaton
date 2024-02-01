package com.bank.validators;

import com.bank.dto.UserDTO;
import com.bank.exceptions.InvalidOperationException;
import com.bank.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserDTOValidator implements Validator {
    private final UserRepository userRepository;
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserDTO.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        UserDTO userDTO = (UserDTO) object;
        if (!userDTO.getPassword().equals(userDTO.getPasswordConfirmation()))
            throw new InvalidOperationException("Password and password confimation doesnt match");
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent())
            throw new InvalidOperationException("User with this username already exists");
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent())
            throw new InvalidOperationException("User with this email already exists");
    }
}
