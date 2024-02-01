package com.bank.repositories;

import com.bank.models.Review;
import com.bank.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsReviewByUser(User user);
}
