package ru.practicum.taskTracker.service;

import ru.practicum.taskTracker.model.SubTask;
import ru.practicum.taskTracker.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InstantComplexityPrioritizedManager extends InMemoryTaskManager {
    private final LocalDateTime initialTimeStamp = LocalDateTime.now();
    private final Map<Long, Boolean> timeAvailabilityGrid = new LinkedHashMap<>();
    private final Set<Task> instantPrioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public InstantComplexityPrioritizedManager() {
        //по ТЗ заполняем сетку на год вперёд (525_600 минут), но для вывода в Main я поставил 3 часа
        for (long i = 0; i < 180; i = i + 15) {
            timeAvailabilityGrid.put(Duration.between(initialTimeStamp,
                    initialTimeStamp.plusMinutes(i)).toMinutes(), false);
        }
    }

    @Override
    public Task addTask(Task task) {
        Task addedTask = super.addTask(task);
        addPrioritizedTask(task);
        return addedTask;
    }

    @Override
    public SubTask addSubTask(SubTask subTask) {
        SubTask subTaskAdded = super.addSubTask(subTask);
        addPrioritizedTask(subTask);
        return subTaskAdded;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return instantPrioritizedTasks;
    }

    public Map<Long, Boolean> getTimeAvailabilityGrid() {
        return timeAvailabilityGrid;
    }

    @Override
    public void addPrioritizedTask(Task task) {
        if(task.getStartTime() == null && task.getDuration() == null)
            return;

        boolean isValid = true;
        // время начала, конца и прололжительность задачи относительно initialTimeStamp
        long startTimeInterval = Duration.between(initialTimeStamp, task.getStartTime()).toMinutes();
        long endTimeInterval = Duration.between(initialTimeStamp, task.getEndTime()).toMinutes();
        // ключи (ячейки), которые будут занимать в мапе начало и конец задачи
        int startKey = ((int) (startTimeInterval / 15)) * 15;
        int endKey = ((int) (endTimeInterval / 15)) * 15;
        long i = startKey;
        // Проверим, не содержит ли добавляемая задача пересечения с имеющимися во временной сетке
        while (i <= endKey) {
            if (timeAvailabilityGrid.get(i)) {
                isValid = false;
                break;
            }
            i = i + 15;
        }
        // если не пересекается - добавляем
        if (isValid) {
            instantPrioritizedTasks.add(task);
            // занимаем в сетке ячейки, соответствующие добавляемой задаче
            i = startKey;
            while (i <= endKey) {
                timeAvailabilityGrid.replace(i, true);
                i = i + 15;
            }
        }
    }
}
