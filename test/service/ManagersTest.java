package service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import ru.practicum.taskTracker.service.*;

class ManagersTest {

    @Test
    void shouldReturnDefaultTaskManager() {
        Assertions.assertNotNull(Managers.getDefault(), "экземпляр менеджера не объявлен или не инициализирован");
        assertTrue(Managers.getDefault() instanceof TaskManager);
    }

    @Test
    void shouldReturnDefaultHistoryManager() {
        assertNotNull(Managers.getDefaultHistory(), "экземпляр менеджера не объявлен или не инициализирован");
        assertTrue(Managers.getDefaultHistory() instanceof HistoryManager);
    }
}