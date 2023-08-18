package com.growth.task.todo.enums;

public enum Status {
    READY("ready", "대기중"),
    DONE("done", "완료"),
    PROGRESS("progress", "진행중");

    private final String key;
    private final String title;

    Status(String key, String title) {
        this.key = key;
        this.title = title;
    }
}
