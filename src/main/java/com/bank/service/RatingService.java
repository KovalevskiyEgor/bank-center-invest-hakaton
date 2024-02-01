package com.bank.service;

import com.bank.models.Rating;
import com.bank.repositories.RatingRepository;
import com.bank.security.JWTEntity;
import com.bank.utils.enums.UserRank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserService userService;

    @Transactional
    public Rating updateUserRating(Integer points){
        JWTEntity jwtEntity = (JWTEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Rating rating = userService.getById(jwtEntity.getId()).getRating();
        rating.setPoints(rating.getPoints()+points);
        updateUserRank(rating);
        ratingRepository.save(rating);
        return new Rating();
    }

    private void updateUserRank(Rating rating) {
        if (rating.getPoints() > 200) {
            rating.setRank(UserRank.EXPERIENCED_MEMBER);
        } else if (rating.getPoints() > 450) {
            rating.setRank(UserRank.PRO_MEMBER);
        }
    }
}
