package com.growth.task.category.controller;

import com.growth.task.category.dto.CategoryResponse;
import com.growth.task.category.service.CategoryRetrieveService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Category", description = "Category API Document")
public class CategoryRetrieveController {
    private final CategoryRetrieveService categoryRetrieveService;

    public CategoryRetrieveController(CategoryRetrieveService categoryRetrieveService) {
        this.categoryRetrieveService = categoryRetrieveService;
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<CategoryResponse> getList() {
        return categoryRetrieveService.getCategories();
    }
}
