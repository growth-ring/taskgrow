package com.growth.task.pomodoro.repository;

import com.growth.task.pomodoro.domain.Pomodoros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PomodorosRepository extends JpaRepository<Pomodoros, Long> {
    List<Pomodoros> findAllByTodo_TodoIdIn(List<Long> todoIds);
    Pomodoros findByTodo_TodoId(Long todoId);
}
