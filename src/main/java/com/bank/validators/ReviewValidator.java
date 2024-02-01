package com.bank.validators;

import com.bank.dto.ReviewDTO;
import com.bank.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ReviewValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(ReviewDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ReviewDTO dto = (ReviewDTO) target;

    }
}
