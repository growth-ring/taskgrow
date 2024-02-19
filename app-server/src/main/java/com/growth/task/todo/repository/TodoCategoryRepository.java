package com.growth.task.todo.repository;

import com.growth.task.todo.domain.TodoCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoCategoryRepository extends JpaRepository<TodoCategory, Long> {
}
