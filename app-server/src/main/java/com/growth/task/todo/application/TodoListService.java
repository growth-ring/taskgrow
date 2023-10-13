package com.growth.task.todo.application;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.task.dto.TaskTodoDetailResponse;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.response.TodoListResponse;
import com.growth.task.todo.exception.TaskNotFoundException;
import com.growth.task.todo.repository.TodosRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
}
