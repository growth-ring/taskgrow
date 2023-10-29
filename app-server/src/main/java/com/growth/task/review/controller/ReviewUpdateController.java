package com.growth.task.review.controller;

import com.growth.task.review.domain.Review;
import com.growth.task.review.dto.ReviewDetailResponse;
import com.growth.task.review.dto.ReviewUpdateRequest;
import com.growth.task.review.service.ReviewUpdateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/review")
@Tag(name = "Review", description = "회고 API")
public class ReviewUpdateController {
    private final ReviewUpdateService reviewUpdateService;

    public ReviewUpdateController(ReviewUpdateService reviewUpdateService) {
        this.reviewUpdateService = reviewUpdateService;
    }

    @PutMapping("/{review_id}")
    @ResponseStatus(OK)
    public ReviewDetailResponse update(
            @PathVariable("review_id") Long reviewId,
            @RequestBody @Valid ReviewUpdateRequest request
    ) {
        Review review = reviewUpdateService.update(reviewId, request);
        return new ReviewDetailResponse(review);
    }
}
