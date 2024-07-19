package ru.practicum.taskTracker.exceptions;

public class FileBackedTaskManagerOutputException extends RuntimeException {
    public FileBackedTaskManagerOutputException(String message) {
        super(message);
    }

    public FileBackedTaskManagerOutputException(String message, Throwable cause) {
        super(message, cause);
    }
}
