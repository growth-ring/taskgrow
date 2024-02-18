package com.growth.task.category.controller;

import com.growth.task.category.dto.CategoryRequest;
import com.growth.task.category.dto.CategoryResponse;
import com.growth.task.category.service.CategoryAddService;
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
@RequestMapping("/api/v1/categories")
@Tag(name = "Task", description = "Category API Document")
public class CategoryAddController {
    private final CategoryAddService categoryAddService;

    public CategoryAddController(CategoryAddService categoryAddService) {
        this.categoryAddService = categoryAddService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public CategoryResponse create(@RequestBody @Valid CategoryRequest request) {
        return categoryAddService.save(request);
    }
}
