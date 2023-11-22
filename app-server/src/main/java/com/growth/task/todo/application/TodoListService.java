package com.growth.task.todo.application;

import com.growth.task.task.dto.TaskTodoDetailResponse;
import com.growth.task.todo.dto.TodoResponse;
import com.growth.task.todo.dto.TodoStatsRequest;
import com.growth.task.todo.dto.TodoStatsResponse;
import com.growth.task.todo.dto.response.TodoWithPomodoroResponse;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.repository.TodosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.growth.task.todo.enums.Status.DONE;
import static com.growth.task.todo.enums.Status.PROGRESS;

@Service
public class TodoListService {

    public static final int PREVIEW_LIMIT = 3;
    private final TodosRepository todosRepository;

    public TodoListService(
            TodosRepository todosRepository
    ) {
        this.todosRepository = todosRepository;
    }

    /**
     * Task Id에 해당하는 투두와 뽀모도로 개수를 리턴합니다.
     */
    @Transactional(readOnly = true)
    public List<TodoWithPomodoroResponse> getTodosByTaskId(Long taskId) {
        return todosRepository.findTodoWithPomodoroByTaskId(taskId);
    }

    /**
     * Task Id에 해당하는 투두 내용을 최대 3개 가져와 리턴합니다
     */
    @Transactional(readOnly = true)
    public List<TaskTodoDetailResponse> getTaskTodosPreview(Long taskId) {
        return todosRepository.getTaskTodoPreview(taskId, PREVIEW_LIMIT);
    }

    /**
     * 날짜 범위에 해당하는 todo를 조회하여 통계 값을 리턴한다.
     * 총 투두 개수, 완료한 투두 개수, 진행 중인 투두 개수, 미완료인 투두 개수
     */
    @Transactional(readOnly = true)
    public TodoStatsResponse getTodoStats(Long userId, TodoStatsRequest request) {
        List<TodoResponse> todos = todosRepository.findByUserIdAndBetweenTimeRange(userId, request);

        return aggregate(todos);
    }

    /**
     * todo의 통계를 계산한다.
     * 총 투두 개수, 완료한 투두 개수, 진행 중인 투두 개수, 미완료인 투두 개수
     */
    public TodoStatsResponse aggregate(List<TodoResponse> todos) {
        long totalCount = todos.size();
        long doneCount = calculateCountByStatus(todos, DONE);
        long progressCount = calculateCountByStatus(todos, PROGRESS);
        long undoneCount = totalCount - doneCount;

        return new TodoStatsResponse(totalCount, doneCount, progressCount, undoneCount);
    }

    /**
     * status에 따라 카운트한다.
     */
    private Long calculateCountByStatus(List<TodoResponse> todos, Status status) {
        return todos.stream()
                .filter(todo -> todo.getStatus() == status)
                .count();
    }
}
