package com.growth.task.category.service;

import com.growth.task.category.domain.Category;
import com.growth.task.category.dto.CategoryRequest;
import com.growth.task.category.dto.CategoryResponse;
import com.growth.task.category.exception.CategoryAlreadyExistsException;
import com.growth.task.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryAddService {
    private final CategoryRepository categoryRepository;

    public CategoryAddService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryResponse save(CategoryRequest request) {
        categoryRepository.findByName(request.getName())
                .ifPresent(it -> {
                    throw new CategoryAlreadyExistsException();
                });

        Category category = categoryRepository.save(request.toEntity());
        return CategoryResponse.of(category);
    }
}
