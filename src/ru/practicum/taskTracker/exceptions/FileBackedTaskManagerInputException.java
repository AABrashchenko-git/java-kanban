package ru.practicum.taskTracker.exceptions;

public class FileBackedTaskManagerInputException extends RuntimeException {
    public FileBackedTaskManagerInputException(String message) {
        super(message);
    }

    public FileBackedTaskManagerInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
