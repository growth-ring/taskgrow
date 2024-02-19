package com.growth.task.category.service;

import com.growth.task.category.domain.Category;
import com.growth.task.category.dto.CategoryResponse;
import com.growth.task.category.exception.CategoryNotFoundException;
import com.growth.task.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryRetrieveService {
    private final CategoryRepository categoryRepository;

    public CategoryRetrieveService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryResponse::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException());
    }
}
