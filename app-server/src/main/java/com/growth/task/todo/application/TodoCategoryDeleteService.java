package com.growth.task.todo.application;

import com.growth.task.todo.repository.TodoCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoCategoryDeleteService {
    private final TodoCategoryRepository todoCategoryRepository;

    public TodoCategoryDeleteService(TodoCategoryRepository todoCategoryRepository) {
        this.todoCategoryRepository = todoCategoryRepository;
    }

    @Transactional
    public void deleteByTodo(Long todoId) {
        todoCategoryRepository.deleteByTodos_TodoId(todoId);
    }
}
