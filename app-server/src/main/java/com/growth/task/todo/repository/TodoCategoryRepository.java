package com.growth.task.todo.repository;

import com.growth.task.todo.domain.TodoCategory;
import com.growth.task.todo.domain.Todos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoCategoryRepository extends JpaRepository<TodoCategory, Long> {
    Optional<TodoCategory> findByTodos(Todos todos);

    void deleteByTodos_TodoId(Long todoId);
}
