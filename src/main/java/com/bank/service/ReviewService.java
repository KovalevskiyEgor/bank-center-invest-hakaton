package com.bank.service;

import com.bank.exceptions.ResourceNotFoundException;
import com.bank.models.Review;
import com.bank.models.User;
import com.bank.repositories.ReviewRepository;
import com.bank.utils.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;


    public List<Review> getByUserAndType(User user, PostType type){
        return user.getReviews().stream().filter(el -> el.getPost().getType() == type).toList();
    }


    public boolean isReviewOwner(User user){
        return reviewRepository.existsReviewByUser(user);
    }

    public Review getById(Long id){
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review wiht this id not found!"));
    }

    public List<Review> getAll(){
        return reviewRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id){
        getById(id);
        reviewRepository.deleteById(id);
    }

    @Transactional
    public Review save(Review review){
        review.setLikes(0L);
        review.setUser(userService.getAuthorizedUser());
        review.setCreationTime(Instant.now());
        reviewRepository.save(review);
        return review;
    }

    @Transactional
    public Review update(Long id, Review review){
        Review before = getById(id);
        review.setId(id);
        review.setCreationTime(before.getCreationTime());
        review.setLikes(before.getLikes());
        return reviewRepository.save(review);
    }

    @Transactional
    public Review putLikeForReview(Long reviewId){
        Review review = getById(reviewId);
        review.setLikes(review.getLikes()+1);
        reviewRepository.save(review);
        return review;
    }
}
