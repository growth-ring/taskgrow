package com.growth.task.todo.application;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.domain.PomodorosRepository;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.domain.TodosRepository;
import com.growth.task.todo.dto.response.TodoGetResponse;
import com.growth.task.todo.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TodosService {

    private TodosRepository todosRepository;
    private PomodorosRepository pomodorosRepository;
    private TasksRepository tasksRepository;

    public TodosService(
            TodosRepository todosRepository,
            PomodorosRepository pomodorosRepository,
            TasksRepository tasksRepository
    ) {
        this.todosRepository = todosRepository;
        this.pomodorosRepository = pomodorosRepository;
        this.tasksRepository = tasksRepository;
    }

    public List<TodoGetResponse> getTodosByTaskId(Long taskId) {
        List<Todos> todosEntities = validateTaskAndFetchTodos(taskId);

        // Todo 가 없으면 빈 리스트 반환
        if (todosEntities.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> todoIds = todosEntities.stream()
                .map(Todos::getTodoId)
                .collect(Collectors.toList());

        List<Pomodoros> pomodorosEntities = pomodorosRepository.findAllByTodo_TodoIdIn(todoIds);

        Map<Long, Pomodoros> pomodorosMap = pomodorosEntities.stream()
                .collect(Collectors.toMap(
                        pomodoro -> pomodoro.getTodo().getTodoId(),
                        pomodoro -> pomodoro
                ));

        return todosEntities.stream()
                .map(todo -> {
                    Pomodoros pomodoro = pomodorosMap.get(todo.getTodoId());
                    return new TodoGetResponse(todo, pomodoro);
                })
                .collect(Collectors.toList());
    }

    private List<Todos> validateTaskAndFetchTodos(Long taskId) {
        if (!tasksRepository.existsById(taskId)) {
            throw new TaskNotFoundException(taskId);
        }
        return todosRepository.findByTask_TaskId(taskId);
    }
}
