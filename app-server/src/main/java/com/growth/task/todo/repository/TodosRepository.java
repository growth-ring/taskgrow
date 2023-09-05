package com.growth.task.todo.repository;

import com.growth.task.todo.domain.Todos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodosRepository extends JpaRepository<Todos, Long> {
    List<Todos> findByTask_TaskId(Long taskId);

    List<Todos> findTop3ByTask_TaskId(Long taskId);
}
