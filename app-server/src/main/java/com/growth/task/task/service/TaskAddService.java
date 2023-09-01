package com.growth.task.task.service;

import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.task.dto.TaskAddRequest;
import com.growth.task.task.dto.TaskAddResponse;
import com.growth.task.task.exception.UserNotFoundException;
import com.growth.task.user.domain.Users;
import com.growth.task.user.domain.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskAddService {
    private final TasksRepository tasksRepository;
    private final UsersRepository usersRepository;

    public TaskAddService(TasksRepository tasksRepository, UsersRepository usersRepository) {
        this.tasksRepository = tasksRepository;
        this.usersRepository = usersRepository;
    }

    public TaskAddResponse save(TaskAddRequest taskAddRequest) {
        Long userId = taskAddRequest.getUserId();
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        Tasks task = taskAddRequest.toEntity(user);
        return new TaskAddResponse(tasksRepository.save(task));
    }
}
