package com.growth.task.todo.application;

import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.exception.TodoNotFoundException;
import com.growth.task.todo.repository.TodosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoDetailService {
    private final TodosRepository todosRepository;

    public TodoDetailService(TodosRepository todosRepository) {
        this.todosRepository = todosRepository;
    }

    @Transactional(readOnly = true)
    public Todos getTodo(Long todoId) {
        return todosRepository.findById(todoId)
                .orElseThrow(() -> new TodoNotFoundException(todoId));
    }
}
