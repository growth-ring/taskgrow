package com.growth.task.review.controller;

import com.growth.task.review.service.ReviewDeleteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/review")
@Tag(name = "Review", description = "회고 API")
public class ReviewDeleteController {
    private final ReviewDeleteService reviewDeleteService;

    public ReviewDeleteController(ReviewDeleteService reviewDeleteService) {
        this.reviewDeleteService = reviewDeleteService;
    }

    @DeleteMapping("/{review_id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("review_id") Long reviewId) {
        reviewDeleteService.deleteById(reviewId);
    }
}
