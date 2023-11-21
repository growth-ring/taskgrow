package com.growth.task.todo.application;

import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.exception.TodoNotFoundException;
import com.growth.task.todo.repository.TodosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoDeleteService {
    private final TodosRepository todosRepository;

    public TodoDeleteService(TodosRepository todosRepository) {
        this.todosRepository = todosRepository;
    }

    @Transactional
    public void deleteByTodoId(Long todoId) {
        Todos todos = todosRepository.findById(todoId)
                .orElseThrow(() -> new TodoNotFoundException(todoId));

        todosRepository.delete(todos);
    }
}
