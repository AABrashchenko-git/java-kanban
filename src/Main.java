import ru.practicum.taskTracker.model.*;
import ru.practicum.taskTracker.service.*;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Пользовательский сценарий
        File taskStorage = new File("src/ru/practicum/taskTracker/resources", "tasks.csv");
        TaskManager manager = Managers.getFileBackedManager(taskStorage);

        // 1. Заведите несколько разных задач, эпиков и подзадач
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
        SubTask subTask3 = new SubTask(epic1.getId(), "имяПодзадачи3Эпика1",
                "описаниеПодзадачи1Эпика2");
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);

        // Получим задачи, чтобы добавить их в историю
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic2.getId());
        manager.getSubTaskById(subTask2.getId());
        manager.getSubTaskById(subTask3.getId());
        manager.getTaskById(task1.getId());
        manager.getTaskById(task1.getId());
        manager.getSubTaskById(subTask2.getId());
        manager.getSubTaskById(subTask1.getId());
        // Обновим одну подзадачу, чтобы после восстановления истории увидеть, что её статус(и её эпика) также сохранились
        manager.updateSubTask(new SubTask(subTask1.getEpicId(), subTask1.getId(), "newИмяПодзадачи1Эпика1",
                "newОписаниеПодзадачи1Эпика1", Status.DONE));
        manager.getSubTaskById(subTask1.getId());

        // 2. Создайте новый FileBackedTaskManager-менеджер из этого же файла.
        TaskManager newManager = FileBackedTaskManager.loadFromFile(taskStorage);

        // 3. Проверьте, что все задачи, эпики, подзадачи, которые были в старом менеджере, есть в новом
        System.out.println("\nИсходный список задач");
        System.out.println("___________________________________");
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());
        System.out.println("\nСписок задач, восстановленный из файла:");
        System.out.println("___________________________________");
        System.out.println(newManager.getAllTasks());
        System.out.println(newManager.getAllEpics());
        System.out.println(newManager.getAllSubTasks());

        System.out.println("\nИсходная история:");
        System.out.println("___________________________________");
        System.out.println(manager.getHistory());
        System.out.println("\nИстория, восстановленная из файла:");
        System.out.println("___________________________________");
        System.out.println(newManager.getHistory());
    }

}