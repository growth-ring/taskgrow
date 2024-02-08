package com.growth.task.todo.application;

import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.TodoUpdateOrder;
import com.growth.task.todo.dto.request.TodoUpdateRequest;
import com.growth.task.todo.dto.response.TodoUpdateResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TodoUpdateService {
    private final TodoDetailService todoDetailService;
    private final TodoListService todoListService;

    public TodoUpdateService(
            TodoDetailService todoDetailService,
            TodoListService todoListService
    ) {
        this.todoDetailService = todoDetailService;
        this.todoListService = todoListService;
    }

    @Transactional
    public Todos update(Long todoId, TodoUpdateRequest todoUpdateRequest) {
        Todos todos = todoDetailService.getTodo(todoId);

        if (todoUpdateRequest.hasTodo()) {
            todos.updateTodo(todoUpdateRequest.getTodo());
        }
        if (todoUpdateRequest.hasStatus()) {
            todos.updateStatus(todoUpdateRequest.getStatus());
        }

        return todos;
    }

    @Transactional
    public List<TodoUpdateResponse> updateOrder(List<TodoUpdateOrder> todoUpdateOrder) {
        List<Long> todoIds = todoUpdateOrder.stream().map(TodoUpdateOrder::getTodoId).toList();

        Map<Long, Integer> todoOrders = todoUpdateOrder.stream()
                .collect(Collectors.toMap(
                        TodoUpdateOrder::getTodoId,
                        TodoUpdateOrder::getOrderNo
                ));

        List<Todos> todos = todoListService.getTodosByIdIn(todoIds);

        if (todos.size() != todoIds.size()) {
            throw new IllegalArgumentException("존재하지 않은 Todo가 있습니다.");
        }

        todos.stream()
                .forEach(todo -> {
                    Integer order = todoOrders.get(todo.getTodoId());
                    todo.updateOrder(order);
                });

        return todos.stream()
                .map(TodoUpdateResponse::new)
                .toList();
    }
}
