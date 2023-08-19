package com.growth.task.pomodoro.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PomodorosRepository extends JpaRepository<Pomodoros, Long> {
}
