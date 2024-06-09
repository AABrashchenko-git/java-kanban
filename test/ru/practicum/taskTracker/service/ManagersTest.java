package ru.practicum.taskTracker.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ManagersTest {

    @Test
    void shouldReturnDefaultTaskManager() {
        assertNotNull(Managers.getDefault(), "экземпляр менеджера не объявлен или не инициализирован");
    }

    @Test
    void shouldReturnDefaultHistoryManager() {
        assertNotNull(Managers.getDefaultHistory(), "экземпляр менеджера не объявлен или не инициализирован");
    }
}