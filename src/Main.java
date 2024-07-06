import ru.practicum.taskTracker.model.*;
import ru.practicum.taskTracker.service.*;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();
        // Пользовательский сценарий
        // 1. Создайте две задачи, эпик с тремя подзадачами и эпик без подзадач
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

        // 2. Запросите созданные задачи несколько раз в разном порядке,
        // выведите историю и убедитесь, что в ней нет повторов.

        System.out.println("\nПолучаем задачи в разном порядке несколько раз и смотрим историю...");
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic2.getId());
        manager.getSubTaskById(subTask2.getId());
        manager.getSubTaskById(subTask3.getId());
        manager.getTaskById(task1.getId());
        manager.getTaskById(task1.getId());
        manager.getSubTaskById(subTask2.getId());
        System.out.println("\nИстория просмотра: ");
        System.out.println(manager.getHistory());

        manager.getEpicById(epic2.getId());
        manager.getTaskById(task2.getId());
        manager.getSubTaskById(subTask1.getId());
        manager.getSubTaskById(subTask2.getId());
        manager.getSubTaskById(subTask3.getId());
        manager.getTaskById(task1.getId());
        manager.getTaskById(task1.getId());
        manager.getSubTaskById(subTask3.getId());
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic2.getId());
        System.out.println("\nИстория просмотра: ");
        System.out.println(manager.getHistory());

        // 3. Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться
        manager.removeOneTaskById(task1.getId());
        System.out.println("\nИстория просмотра после удаления задачи1: ");
        System.out.println(manager.getHistory());

        // 4. Удалите эпик с подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи
        manager.removeOneEpicById(epic1.getId());
        System.out.println("\nИстория просмотра после удаления эпика с тремя подзадачами: ");
        System.out.println(manager.getHistory());
    }

}