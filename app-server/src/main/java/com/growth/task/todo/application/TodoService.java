package com.growth.task.todo.application;

import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.composite.TodoAndPomodoroUpdateRequest;
import com.growth.task.todo.dto.request.TodoAddRequest;
import com.growth.task.todo.dto.request.TodoUpdateRequest;
import com.growth.task.todo.exception.TaskNotFoundException;
import com.growth.task.todo.exception.TodoNotFoundException;
import com.growth.task.todo.repository.TodosRepository;
import org.springframework.stereotype.Service;

@Service
public class TodoService {
    private TodosRepository todosRepository;

    private TasksRepository tasksRepository;

    public TodoService(TodosRepository todosRepository, TasksRepository tasksRepository) {
        this.todosRepository = todosRepository;
        this.tasksRepository = tasksRepository;
    }

    public Todos save(TodoAddRequest todoAddRequest) {
        Long taskId = todoAddRequest.getTaskId();
        Tasks tasks = tasksRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        Todos todos = todoAddRequest.toEntity(tasks);
        return todosRepository.save(todos);
    }

    public Todos update(Long todoId, TodoAndPomodoroUpdateRequest todoAndPomodoroUpdateRequest) {
        Todos todos = todosRepository.findById(todoId)
                .orElseThrow(() -> new TodoNotFoundException(todoId));

        TodoUpdateRequest todoUpdateReq = todoAndPomodoroUpdateRequest.getTodoUpdateRequest();
        if(todoUpdateReq.getTodo() != null) {
            todos.updateTodo(todoUpdateReq.getTodo());
        }
        if(todoUpdateReq.getTaskId() != null) {
            Tasks task = tasksRepository.findById(todoUpdateReq.getTaskId())
                    .orElseThrow(() -> new TaskNotFoundException(todoUpdateReq.getTaskId()));
            todos.updateTask(task);
        }
        todosRepository.save(todos);
        return todos;
    }
}
