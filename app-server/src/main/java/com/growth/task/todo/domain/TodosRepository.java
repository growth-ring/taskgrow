package com.growth.task.todo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodosRepository extends JpaRepository<Todos, Long> {
    List<Todos> findByTask_TaskId(Long taskId);
}
