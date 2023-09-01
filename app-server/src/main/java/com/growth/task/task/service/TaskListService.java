package com.growth.task.task.service;

import com.growth.task.task.dto.TaskListRequest;
import com.growth.task.task.dto.TaskListResponse;
import com.growth.task.task.dto.TaskListWithTodoStatusResponse;
import com.growth.task.task.dto.TaskTodoResponse;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.enums.Status;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.growth.task.todo.enums.Status.isDone;
import static com.growth.task.todo.enums.Status.isRemain;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class TaskListService {
    private final TasksRepository tasksRepository;

    public TaskListService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    @Transactional(readOnly = true)
    public List<TaskListResponse> getTasks(TaskListRequest request) {
        List<TaskListWithTodoStatusResponse> taskList = tasksRepository.findRemainedTodosByUserBetweenTimeRange(
                request.getUserId(),
                request.getStartDate().atStartOfDay(),
                request.getEndDate().atStartOfDay()
        );

        Map<Long, TaskTodoResponse> groupingByTask = groupingByTask(taskList);

        return taskList.stream()
                .map(task -> {
                            TaskTodoResponse todos = groupingByTask.getOrDefault(task.getTaskId(), new TaskTodoResponse(0, 0));
                            return new TaskListResponse(task, todos);
                        }
                )
                .toList();
    }

    /**
     * Task의 id를 기준으로 todos의 status의 개수를 세어 그룹화합니다.
     * 예를 들어,
     * [ {taskId: 1, userId: 1, taskDate: 2023-08-28, todoStatus: READY},
     * {taskId: 1, userId: 1, taskDate: 2023-08-28, todoStatus: READY},
     * {taskId: 1, userId: 1, taskDate: 2023-08-28, todoStatus: READY} ]
     * 의 경우
     * {taskId: 1, {remain: 3, done: 0}}
     * 을 반환합니다.
     *
     * @param taskList Task 리스트
     * @return 테스크의 진행률 Map
     */
    public static Map<Long, TaskTodoResponse> groupingByTask(List<TaskListWithTodoStatusResponse> taskList) {
        return taskList.stream()
                .collect(groupingBy(
                                TaskListWithTodoStatusResponse::getTaskId,
                                collectingAndThen(toList(),
                                        TaskListService::calculateTodoStatus
                                )
                        )
                );
    }

    /**
     * Todos 진행률을 계산합니다.
     * remain : 남은 Todos 개수 (status= READY, PROGRESS)
     * done : 끝난 Todos 개수 (statsu= DONE)
     * ex) {'remain': 3, 'done' : 2}
     *
     * @param tasks Task 리스트
     * @return Todos 진행률
     */
    public static TaskTodoResponse calculateTodoStatus(List<TaskListWithTodoStatusResponse> tasks) {
        int doneCount = 0;
        int remainCount = 0;
        for (TaskListWithTodoStatusResponse task : tasks) {
            Status todoStatus = task.getTodoStatus();
            if (isDone(todoStatus)) {
                doneCount++;
            } else if (isRemain(todoStatus)) {
                remainCount++;
            }
        }
        return new TaskTodoResponse(remainCount, doneCount);
    }
}
