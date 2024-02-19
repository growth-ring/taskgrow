package com.growth.task.todo.application;

import com.growth.task.category.domain.Category;
import com.growth.task.category.service.CategoryRetrieveService;
import com.growth.task.todo.domain.TodoCategory;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.repository.TodoCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TodoCategoryAddService {
    private final TodoCategoryRepository todoCategoryRepository;
    private final CategoryRetrieveService categoryRetrieveService;

    public TodoCategoryAddService(
            TodoCategoryRepository todoCategoryRepository,
            CategoryRetrieveService categoryRetrieveService
    ) {
        this.todoCategoryRepository = todoCategoryRepository;
        this.categoryRetrieveService = categoryRetrieveService;
    }

    @Transactional
    public TodoCategory save(Todos todo, Long categoryId) {
        if (categoryId == null) {
            log.debug("주어진 카테고리가 없습니다.");
            return null;
        }
        Category category = categoryRetrieveService.getCategory(categoryId);
        return todoCategoryRepository.save(new TodoCategory(todo, category));
    }
}
