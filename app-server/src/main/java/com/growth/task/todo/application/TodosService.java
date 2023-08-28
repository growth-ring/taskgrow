package com.growth.task.todo.application;

import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.domain.TodosRepository;
import com.growth.task.todo.dto.response.TodoGetResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodosService {

    private TodosRepository todosRepository;

    public TodosService(TodosRepository todosRepository) {
        this.todosRepository = todosRepository;
    }

    public List<TodoGetResponse> getTodosByTaskId(Long taskId) {
        List<Todos> todosEntities = todosRepository.findByTask_TaskId(taskId);

        return todosEntities.stream()
                .map(entity -> new TodoGetResponse(entity.getTodoId(), entity.getTodo(), entity.getStatus()))
                .collect(Collectors.toList());
    }
}
