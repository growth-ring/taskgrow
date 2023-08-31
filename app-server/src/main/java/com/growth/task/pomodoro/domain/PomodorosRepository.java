package com.growth.task.pomodoro.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PomodorosRepository extends JpaRepository<Pomodoros, Long> {
    Optional<Pomodoros> findByTodo_TodoId(Long todoId);
}
