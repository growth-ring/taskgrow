package com.growth.task.review.service;

import com.growth.task.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByTasks_TaskId(Long taskId);
}
