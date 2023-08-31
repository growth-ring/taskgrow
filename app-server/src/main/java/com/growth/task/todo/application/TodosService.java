package com.growth.task.todo.application;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.domain.PomodorosRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.domain.TodosRepository;
import com.growth.task.todo.dto.response.TodoGetResponse;
import com.growth.task.todo.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodosService {

    private TodosRepository todosRepository;
    private PomodorosRepository pomodorosRepository;

    public TodosService(TodosRepository todosRepository, PomodorosRepository pomodorosRepository) {
        this.todosRepository = todosRepository;
        this.pomodorosRepository = pomodorosRepository;
    }

    public List<TodoGetResponse> getTodosByTaskId(Long taskId) {
        List<Todos> todosEntities = todosRepository.findByTask_TaskId(taskId);
        if (todosEntities.isEmpty()) {
            throw new TaskNotFoundException();
        }

        return todosEntities.stream()
                .map(todo -> {
                    Pomodoros pomodoro = pomodorosRepository.findByTodo_TodoId(todo.getTodoId()).orElse(null);
                    return new TodoGetResponse(todo, pomodoro);
                })
                .collect(Collectors.toList());
    }
}
