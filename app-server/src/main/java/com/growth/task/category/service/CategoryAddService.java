package com.growth.task.category.service;

import com.growth.task.category.domain.Category;
import com.growth.task.category.dto.CategoryRequest;
import com.growth.task.category.dto.CategoryResponse;
import com.growth.task.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryAddService {
    private final CategoryRepository categoryRepository;

    public CategoryAddService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse save(CategoryRequest request) {
        Category category = categoryRepository.save(request.toEntity());
        return CategoryResponse.of(category);
    }
}
