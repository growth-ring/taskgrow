package com.growth.task.todo.repository;

import com.growth.task.todo.domain.Todos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodosRepository extends JpaRepository<Todos, Long>, TodosRepositoryCustom {
    List<Todos> findByTask_TaskId(Long taskId);
}
