package com.bank.security.expression;

import com.bank.service.PostService;
import com.bank.service.ReviewService;
import com.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecutiryExpression {

    private final UserService userService;
    private final PostService postService;
    private final ReviewService reviewService;

    public boolean isPostOwner(Long postId){
        return postService.isPostOwner(userService.getAuthorizedUser());
    }

    public boolean isReviewOwner(Long reviewId){
        return reviewService.isReviewOwner(userService.getAuthorizedUser());
    }
}
