package com.bank.controllers;

import com.bank.dto.ErrorDTO;
import com.bank.exceptions.BagRequestException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class MainController {
    protected void checkBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (var error: allErrors) {
                if (Objects.equals(error.getCode(), "400")){
                    throw new BagRequestException(error.getDefaultMessage());
                }
            }
        }
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorDTO> handleException(ConstraintViolationException e){

        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

        StringBuilder response = new StringBuilder();

        for (var el : constraintViolations) {
            response.append(el.getMessage()).append("; ");
        }

        return ResponseEntity
                .status(400)
                .body(new ErrorDTO("BAD_REQUEST", response.toString()));

    }
}