package com.growth.task.review.controller;

import com.growth.task.review.dto.ReviewAddRequest;
import com.growth.task.review.dto.ReviewAddResponse;
import com.growth.task.review.service.ReviewAddService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/review")
@Tag(name = "Review", description = "회고 API")
public class ReviewAddController {
    private final ReviewAddService reviewAddService;

    public ReviewAddController(ReviewAddService reviewAddService) {
        this.reviewAddService = reviewAddService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ReviewAddResponse create(@RequestBody @Valid ReviewAddRequest request) {
        return reviewAddService.save(request);
    }
}
