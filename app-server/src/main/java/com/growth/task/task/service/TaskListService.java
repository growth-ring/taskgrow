package com.growth.task.task.service;

import com.growth.task.task.dto.TaskListRequest;
import com.growth.task.task.dto.TaskListResponse;
import com.growth.task.task.dto.TaskListQueryResponse;
import com.growth.task.task.dto.TaskTodoResponse;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.enums.Status;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.growth.task.todo.enums.Status.isDone;
import static com.growth.task.todo.enums.Status.isRemain;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@Transactional
public class TaskListService {
    private final TasksRepository tasksRepository;

    public TaskListService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    @Transactional(readOnly = true)
    public List<TaskListResponse> getTasks(TaskListRequest request) {
        List<TaskListQueryResponse> taskList = tasksRepository.findRemainedTodosByUserBetweenTimeRange(request);

        Map<Long, TaskTodoResponse> groupingByTask = calculateTaskTodoStatusMap(taskList);

        List<TaskListResponse> taskListResponses = collectToTask(taskList, groupingByTask);

        return taskListResponses.stream()
                .sorted(byTaskDate())
                .collect(toList());
    }

    /**
     * TaskList와 Todos 진행률 Map을 TaskId로 매핑하고 중복을 제거합니다.
     *
     * @param taskList       테스크 리스트
     * @param groupingByTask todos 진행률 Map
     * @return todos 진행률이 매핑된 Task List
     */
    public static List<TaskListResponse> collectToTask(
            List<TaskListQueryResponse> taskList,
            Map<Long, TaskTodoResponse> groupingByTask
    ) {
        return taskList.stream()
                .collect(toMap(
                        TaskListQueryResponse::getTaskId,
                        task -> {
                            TaskTodoResponse todos = groupingByTask.getOrDefault(task.getTaskId(), new TaskTodoResponse(0, 0));
                            return new TaskListResponse(task, todos);
                        },
                        (existing, replacement) -> existing
                ))
                .values()
                .stream()
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
    public Map<Long, TaskTodoResponse> calculateTaskTodoStatusMap(List<TaskListQueryResponse> taskList) {
        return taskList.stream()
                .collect(groupingBy(
                                TaskListQueryResponse::getTaskId,
                                collectingAndThen(toList(),
                                        tasks -> calculateTodoStatus(tasks)
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
    public TaskTodoResponse calculateTodoStatus(List<TaskListQueryResponse> tasks) {
        int doneCount = 0;
        int remainCount = 0;
        for (TaskListQueryResponse task : tasks) {
            Status todoStatus = task.getTodoStatus();
            if (isDone(todoStatus)) {
                doneCount++;
            } else if (isRemain(todoStatus)) {
                remainCount++;
            }
        }
        return new TaskTodoResponse(remainCount, doneCount);
    }

    private static Comparator<TaskListResponse> byTaskDate() {
        return Comparator.comparing(TaskListResponse::getTaskDate);
    }
}
