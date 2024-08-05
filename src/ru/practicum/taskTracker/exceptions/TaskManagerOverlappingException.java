package ru.practicum.taskTracker.exceptions;

public class TaskManagerOverlappingException extends RuntimeException {
    public TaskManagerOverlappingException(String message) {
        super(message);
    }

    public TaskManagerOverlappingException(String message, Throwable cause) {
        super(message, cause);
    }
}