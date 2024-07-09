package ru.practicum.taskTracker.exceptions;

public class FileBackedTaskManagerInputException extends RuntimeException {
    public FileBackedTaskManagerInputException(final String message) {
        super(message);
    }
}
