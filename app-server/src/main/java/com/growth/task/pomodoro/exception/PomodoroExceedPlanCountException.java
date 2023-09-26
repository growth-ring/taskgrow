package com.growth.task.pomodoro.exception;

public class PomodoroExceedPlanCountException extends RuntimeException {
    public PomodoroExceedPlanCountException() {
        super("Pomodoro perform count cannot be greater than the plan count.");
    }
}
