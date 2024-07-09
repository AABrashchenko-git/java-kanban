package ru.practicum.taskTracker.exceptions;

public class FileBackedTaskManagerOutputException extends RuntimeException {
    public FileBackedTaskManagerOutputException(final String message) {
        super(message);
    }
}
