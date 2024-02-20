package com.growth.task.todo.controller;

import com.growth.task.todo.application.TodoCategoryDeleteService;
import com.growth.task.todo.application.TodoDeleteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/todos")
@Tag(name = "Todo", description = "Todo API Document")
public class TodoDeleteController {

    private final TodoDeleteService todoDeleteService;
    private final TodoCategoryDeleteService todoCategoryDeleteService;

    public TodoDeleteController(
            TodoDeleteService todoDeleteService,
            TodoCategoryDeleteService todoCategoryDeleteService
    ) {
        this.todoDeleteService = todoDeleteService;
        this.todoCategoryDeleteService = todoCategoryDeleteService;
    }

    @DeleteMapping("/{todo_id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("todo_id") Long todoId) {
        todoDeleteService.deleteByTodoId(todoId);
    }
    @DeleteMapping("/{todoId}/category")
    @ResponseStatus(NO_CONTENT)
    public void deleteCategory(@PathVariable("todoId") Long todoId) {
        todoCategoryDeleteService.deleteByTodo(todoId);
    }
}
