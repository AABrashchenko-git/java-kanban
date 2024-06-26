import ru.practicum.taskTracker.model.*;
import ru.practicum.taskTracker.service.*;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        // 1. Создание и добавление задач
        Task task1 = new Task("имяЗадачи1", "описаниеЗадачи1");
        Task task2 = new Task("имяЗадачи2", "описаниеЗадачи2");
        manager.addTask(task1);
        manager.addTask(task2);

        Epic epic1 = new Epic("имяЭпика1", "описаниеЭпика1");
        Epic epic2 = new Epic("имяЭпика2", "описаниеЭпика2");
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        SubTask subTask1 = new SubTask(epic1.getId(), "имяПодзадачи1Эпика1",
                "описаниеПодзадачи1Эпика1");
        SubTask subTask2 = new SubTask(epic1.getId(), "имяПодзадачи2Эпика1",
                "описаниеПодзадачи2Эпика1");
        SubTask subTask3 = new SubTask(epic2.getId(), "имяПодзадачи1Эпика2",
                "описаниеПодзадачи1Эпика2");
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);

        // 2. Выводим на экран все добавленные задачи
        System.out.println("\nВсе добавленные в менеджер задачи");
        printAllTasks(manager);

        // 3. Получение разного вида задач по идентификатору
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        manager.getTaskById(task2.getId());
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic2.getId());
        manager.getEpicById(epic2.getId());
        manager.getSubTaskById(subTask1.getId());
        manager.getSubTaskById(subTask2.getId());
        manager.getSubTaskById(subTask3.getId());
        manager.getSubTaskById(subTask3.getId());
        manager.getTaskById(task1.getId());
        manager.getTaskById(task1.getId());
        System.out.println("\nПолучаем элементы с несуществующими индексами" +
                "...");
        manager.getTaskById(124);
        manager.getEpicById(223);
        manager.getSubTaskById(543);

        // 4. Выводим историю просмотра, смотрим отображение только 10 последних задач, несуществующие задачи не отобразятся
        System.out.println("\nИстория просмотра: ");
        System.out.println(manager.getHistory());

        System.out.println("\nСпринт 6. Добавим >10 задач, оценим функционал истории задач");
        Task task3 = new Task("имяЗадачи3", "описаниеЗадачи3");
        Task task4 = new Task("имяЗадачи4", "описаниеЗадачи4");
        Task task5 = new Task("имяЗадачи5", "описаниеЗадачи5");
        Task task6 = new Task("имяЗадачи6", "описаниеЗадачи6");
        Task task7 = new Task("имяЗадачи7", "описаниеЗадачи7");
        manager.addTask(task3);
        manager.addTask(task4);
        manager.addTask(task5);
        manager.addTask(task6);
        manager.addTask(task7);
        manager.getTaskById(task4.getId());
        manager.getTaskById(task5.getId());
        manager.getTaskById(task6.getId());
        manager.getTaskById(task7.getId());
        manager.getTaskById(task3.getId());
        manager.getTaskById(task3.getId());
        manager.getTaskById(task3.getId());

        System.out.println("\nИстория просмотра: ");
        System.out.println(manager.getHistory());

        // 5. Обновим задачи, получим обновленные задачи, посмотрим, что в истории просмотров
        manager.updateTask(new Task(task1.getId(), "новое имя задачи1", "новоеОписаниеЗадачи1", Status.DONE));
        manager.updateEpic(new Epic(epic1.getId(), "новое имя эпика1", "новоеОписаниеЭпика1"));
        manager.updateSubTask(new SubTask(epic1.getId(), subTask1.getId(), "новое имя подзадачи1",
                "новоеОписаниеПодзадачи1", Status.DONE));

        Task updatedTask = manager.getTaskById(task1.getId());
        Epic updatedEpic = manager.getEpicById(epic1.getId());
        SubTask updatedSubTask = manager.getSubTaskById(subTask1.getId());

        System.out.println("\nПроведено обновление задач! Выведем список всех задач");
        printAllTasks(manager);
        System.out.println("\nИстория просмотра после обновления задач:");
        System.out.println(manager.getHistory());

        // Удалим все задачи, эпики и подзадачи, посмотрим на корректное функционирование истории задач
        manager.removeTasks();
        manager.removeEpics();
        manager.removeSubTasks();
        manager.removeOneEpicById(epic2.getId());
        System.out.println("\n!!!!!!!!!!! Проведено удаление всех типов задач! История после удаления:");

        System.out.println(manager.getHistory()); // => в истории всё сохранилось
        System.out.println("\nСписок задач после удаления:");
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());

    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("\nЗадачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("\nЭпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);
        }
        System.out.println("\nПодзадачи:");
        for (Task subtask : manager.getAllSubTasks()) {
            System.out.println(subtask);
        }
    }
}