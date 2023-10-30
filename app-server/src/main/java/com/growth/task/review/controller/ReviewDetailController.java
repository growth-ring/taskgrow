package com.growth.task.review.controller;

import com.growth.task.review.dto.ReviewDetailResponse;
import com.growth.task.review.service.ReviewDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/review")
@Tag(name = "Review", description = "회고 API")
public class ReviewDetailController {
    private final ReviewDetailService reviewDetailService;

    public ReviewDetailController(ReviewDetailService reviewDetailService) {
        this.reviewDetailService = reviewDetailService;
    }

    @GetMapping("/{task_id}")
    @ResponseStatus(OK)
    public ReviewDetailResponse getReview(@PathVariable("task_id") Long taskId) {
        return reviewDetailService.getReviewByTaskId(taskId);
    }
}
