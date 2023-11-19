package com.growth.task.todo.application;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.task.dto.TaskTodoDetailResponse;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.TodoStatsRequest;
import com.growth.task.todo.dto.TodoStatsResponse;
import com.growth.task.todo.dto.TodoResponse;
import com.growth.task.todo.dto.response.TodoListResponse;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.exception.TaskNotFoundException;
import com.growth.task.todo.repository.TodosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.growth.task.todo.enums.Status.DONE;
import static com.growth.task.todo.enums.Status.PROGRESS;

@Transactional
@Service
public class TodoListService {

    public static final int PREVIEW_LIMIT = 3;
    private final TodosRepository todosRepository;
    private final PomodorosRepository pomodorosRepository;
    private final TasksRepository tasksRepository;

    public TodoListService(
            TodosRepository todosRepository,
            PomodorosRepository pomodorosRepository,
            TasksRepository tasksRepository
    ) {
        this.todosRepository = todosRepository;
        this.pomodorosRepository = pomodorosRepository;
        this.tasksRepository = tasksRepository;
    }

    @Transactional(readOnly = true)
    public List<TodoListResponse> getTodosByTaskId(Long taskId) {
        List<Todos> todosEntities = validateTaskAndFetchTodos(taskId);

        // Todo가 없으면 빈 리스트 반환
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
                    return new TodoListResponse(todo, pomodoro);
                })
                .collect(Collectors.toList());
    }

    private List<Todos> validateTaskAndFetchTodos(Long taskId) {
        if (!tasksRepository.existsById(taskId)) {
            throw new TaskNotFoundException(taskId);
        }
        return todosRepository.findByTask_TaskId(taskId);
    }

    /**
     * Task Id에 해당하는 투두 내용을 최대 3개 가져와 리턴합니다
     *
     * @param taskId 테스크 아이디
     * @return 투두 내용 리스트
     */
    public List<TaskTodoDetailResponse> getTaskTodosPreview(Long taskId) {
        return todosRepository.getTaskTodoPreview(taskId, PREVIEW_LIMIT);
    }

    /**
     * 날짜 범위에 해당하는 todo의 통계를 계산한다.
     * 총 투두 개수, 완료한 투두 개수, 진행 중인 투두 개수, 미완료인 투두 개수
     *
     * @param userId
     * @param request
     * @return
     */
    @Transactional(readOnly = true)
    public TodoStatsResponse getTodoStats(Long userId, TodoStatsRequest request) {
        List<TodoResponse> todos = todosRepository.findByUserIdAndBetweenTimeRange(userId, request);

        return aggregate(todos);
    }

    public TodoStatsResponse aggregate(List<TodoResponse> todos) {
        long totalCount = todos.size();
        long doneCount = calculateCountByStatus(todos, DONE);
        long progressCount = calculateCountByStatus(todos, PROGRESS);
        long undoneCount = totalCount - doneCount;

        return new TodoStatsResponse(totalCount, doneCount, progressCount, undoneCount);
    }

    private Long calculateCountByStatus(List<TodoResponse> todos, Status status) {
        return todos.stream()
                .filter(todo -> todo.getStatus() == status)
                .count();
    }
}
