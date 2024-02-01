package com.bank.controllers;

import com.bank.dto.ReviewDTO;
import com.bank.models.Review;
import com.bank.service.PostService;
import com.bank.service.RatingService;
import com.bank.service.ReviewService;
import com.bank.utils.mappers.impl.ReviewMapper;
import com.bank.validators.ReviewValidator;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/reviews")
@Validated
public class ReviewController extends MainController {
    private final ReviewService reviewService;
    private final RatingService ratingService;
    private final ReviewMapper reviewMapper;
    private final ReviewValidator reviewValidator;
    private final PostService postService;


    @Operation(summary = "Get all reviews")
    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity
                .ok()
                .body(reviewMapper.toDTOs(reviewService.getAll()));
    }

    @Operation(summary = "Get review by id")
    @GetMapping("/{review_id}")
    public ResponseEntity<Object> getById(@PathVariable("review_id") Long id) {
        Review review = reviewService.getById(id);
        return ResponseEntity
                .ok()
                .body(reviewMapper.toDTO(review));
    }

    @Operation(summary = "Delete review by id")
    @DeleteMapping("/{review_id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@customSecurityExpression.isReviewOwner(#reviewId)")
    public void deleteById(@PathVariable("review_id") Long reviewId) {
        reviewService.deleteById(reviewId);
    }

    @Operation(summary = "Create new review")
    @PostMapping("/create/{post_id}")
    public ResponseEntity<Object> createNewEventReview(
            @PathVariable("post_id") Long postId,
            @RequestBody @Valid ReviewDTO reviewDTO,
            BindingResult bindingResult) {
        reviewValidator.validate(reviewDTO, bindingResult);
        checkBindingResult(bindingResult);
        Review review = reviewMapper.fromDTO(reviewDTO);
        review.setPost(postService.getById(postId));
        review = reviewService.save(review);
        ratingService.updateUserRating(50); //TODO
        postService.updateRating(postService.getById(postId), review);
        return ResponseEntity
                .ok()
                .body(reviewMapper.toDTO(review));
    }


    @Operation(summary = "Update existing review")
    @PatchMapping("/{review_id}/update")
    @PreAuthorize("@customSecurityExpression.isReviewOwner(#reviewId)")
    public ResponseEntity<Object> updateReview(
            @PathVariable("review_id") Long reviewId,
            @RequestBody @Valid ReviewDTO reviewDTO,
            BindingResult bindingResult) {
        reviewValidator.validate(reviewDTO, bindingResult);
        Review review = reviewMapper.fromDTO(reviewDTO);
        review.setPost(reviewService.getById(reviewId).getPost());
        review = reviewService.update(reviewId, review);
        return ResponseEntity
                .ok()
                .body(reviewMapper.toDTO(review));
    }

    @Operation(summary = "Put like for a review")
    @PatchMapping("/{review_id}/likes")
    public ResponseEntity<Object> putLikeForReview(@PathVariable("review_id") Long reviewId) {
        Review reviewEvent = reviewService.putLikeForReview(reviewId);
        ratingService.updateUserRating(1);
        return ResponseEntity
                .ok()
                .body(reviewMapper.toDTO(reviewEvent));
    }

}