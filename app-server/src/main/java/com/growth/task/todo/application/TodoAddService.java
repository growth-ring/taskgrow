package com.growth.task.todo.application;

import com.growth.task.task.domain.Tasks;
import com.growth.task.task.service.TaskDetailService;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.request.TodoAddRequest;
import com.growth.task.todo.repository.TodosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoAddService {
    private TodosRepository todosRepository;

    private TaskDetailService taskDetailService;

    public TodoAddService(TodosRepository todosRepository, TaskDetailService taskDetailService) {
        this.todosRepository = todosRepository;
        this.taskDetailService = taskDetailService;
    }

    @Transactional
    public Todos save(TodoAddRequest todoAddRequest) {
        Long taskId = todoAddRequest.getTaskId();
        Tasks tasks = taskDetailService.findTaskById(taskId);

        Todos todos = todoAddRequest.toEntity(tasks);
        return todosRepository.save(todos);
    }
}
