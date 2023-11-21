package com.growth.task.review.controller;

import com.growth.task.review.dto.ReviewStatsRequest;
import com.growth.task.review.dto.ReviewStatsResponse;
import com.growth.task.review.service.ReviewListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/review")
@Tag(name = "Review", description = "회고 API")
public class ReviewListController {
    private final ReviewListService reviewListService;

    public ReviewListController(ReviewListService reviewListService) {
        this.reviewListService = reviewListService;
    }
    @GetMapping("/stats/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewStatsResponse getReviewStats(
     @PathVariable("user_id") Long userId,
     @ModelAttribute @Valid ReviewStatsRequest request
    ){
        return reviewListService.getReviewStats(userId, request);
    }
}
