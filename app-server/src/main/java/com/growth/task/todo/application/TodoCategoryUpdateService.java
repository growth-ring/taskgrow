package com.growth.task.todo.application;

import com.growth.task.category.domain.Category;
import com.growth.task.category.service.CategoryRetrieveService;
import com.growth.task.todo.domain.TodoCategory;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.repository.TodoCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoCategoryUpdateService {
    private final TodoCategoryRepository todoCategoryRepository;
    private final CategoryRetrieveService categoryRetrieveService;

    public TodoCategoryUpdateService(
            TodoCategoryRepository todoCategoryRepository,
            CategoryRetrieveService categoryRetrieveService
    ) {
        this.todoCategoryRepository = todoCategoryRepository;
        this.categoryRetrieveService = categoryRetrieveService;
    }

    @Transactional
    public void update(Todos todo, Long categoryId) {
        Category category = categoryRetrieveService.getCategory(categoryId);
        todoCategoryRepository.findByTodos(todo)
                .ifPresentOrElse(
                        it -> it.updateCategory(category),
                        () -> todoCategoryRepository.save(new TodoCategory(todo, category))
                );
    }
}
