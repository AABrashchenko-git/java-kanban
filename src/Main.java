import ru.practicum.taskTracker.model.*;
import ru.practicum.taskTracker.service.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        // Пользовательский сценарий для ТЗ-8, добавляются задачи с учетом времени и обычные
        File taskStorage = new File("src/resources", "tasks.csv");
        TaskManager manager = Managers.getFileBackedManager();

        // 1. Заведите несколько разных задач, эпиков и подзадач
        Task task1 = new Task("имяЗадачи1", "описаниеЗадачи1");
        Task task2 = new Task("имяЗадачи2", "описаниеЗадачи2",
                LocalDateTime.now(), Duration.ofMinutes(30));
        Task task3 = new Task("имяЗадачи3", "описаниеЗадачи3",
                LocalDateTime.now().plusMinutes(31), Duration.ofMinutes(29));
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        Epic epic1 = new Epic("имяЭпика1", "описаниеЭпика1");
        Epic epic2 = new Epic("имяЭпика2", "описаниеЭпика2");
        Epic epic3 = new Epic("имяЭпика3", "описаниеЭпика3");
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);
        SubTask subTask1 = new SubTask(epic1.getId(), "имяПодзадачи1Эпика1",
                "описаниеПодзадачи1Эпика1");
        SubTask subTask2 = new SubTask(epic1.getId(), "имяПодзадачи2Эпика1",
                "описаниеПодзадачи2Эпика1", LocalDateTime.now().plusMinutes(61), Duration.ofMinutes(9));
        SubTask subTask3 = new SubTask(epic1.getId(), "имяПодзадачи3Эпика1",
                "описаниеПодзадачи1Эпика2", LocalDateTime.now().plusMinutes(71), Duration.ofMinutes(59));
        SubTask subTask4 = new SubTask(epic2.getId(), "имяПодзадачи1Эпика2",
                "описаниеПодзадачи1Эпика2", LocalDateTime.now().plusMinutes(240), Duration.ofMinutes(60));
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);
        manager.addSubTask(subTask4);

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

        // Обновим подзадачу, чтобы после восстановления истории увидеть, что её статус(и её эпика) также сохранились
        // Плюс увидим, что в списке приоритетных задач она заменила старую
        manager.updateSubTask(new SubTask(subTask2.getEpicId(), subTask2.getId(), "newИмяПодзадачи2Эпика1",
                "newОписаниеПодзадачи2Эпика1", Status.DONE, LocalDateTime.now().plusMinutes(61), Duration.ofMinutes(9)));
        manager.getSubTaskById(subTask1.getId());

        // 2. Создайте новый FileBackedTaskManager-менеджер из этого же файла.
        TaskManager newManager = FileBackedTaskManager.loadFromFile(taskStorage);

        // 3. Проверьте, что все задачи, которые были в старом менеджере, есть в новом (с учетом временных меток)
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

        // Посмотрим, сохранились ли временные метки в исходной задаче и в восстановленной,
        // а также проверим работу методов, возвращающих Optional
        System.out.println("\nВремя начала и окончания epic1 в исходном и восстановленном менеджере:");
        manager.getEpicByIdOptional(epic1.getId()).ifPresent(ep -> {
            System.out.print(ep.getStartTime()
                    .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")) + " - ");
            System.out.println(ep.getEndTime()
                    .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")));
        });
        newManager.getEpicByIdOptional(epic1.getId()).ifPresent(ep -> {
            System.out.print(ep.getStartTime()
                    .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")) + " - ");
            System.out.println(ep.getEndTime()
                    .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")));
        });
        manager.getTaskByIdOptional(task3.getId());
        manager.getSubTaskByIdOptional(subTask4.getId());
        System.out.println("\nИстория после использования методов getByIdOptional");
        System.out.println("___________________________________");
        System.out.println(manager.getHistory());

        System.out.println("\nЗадачи по приоритету исходного менеджера");
        System.out.println(manager.getPrioritizedTasks());
        System.out.println("\nЗадачи по приоритету восстановленного менеджера");
        System.out.println(newManager.getPrioritizedTasks());

        // Дополнительное задание ТЗ-8
        InstantComplexityPrioritizedManager instantManager = new InstantComplexityPrioritizedManager();
        // Проверим заполнении временной сетки при создании экземпляра менеджера
        System.out.println("\nИсходная временная сетка");
        System.out.println(instantManager.getTimeAvailabilityGrid());
        // Добавим две задачи, которые не пересекаются по времени
        instantManager.addTask(new Task("timeName1", "timeDescription1",
                LocalDateTime.now().plusMinutes(13), Duration.ofMinutes(15)));
        instantManager.addTask(new Task("timeName2", "timeDescription1",
                LocalDateTime.now().plusMinutes(45), Duration.ofMinutes(95)));
        System.out.println("\nЗадачи, не пересекающиеся во времени");
        System.out.println(instantManager.getPrioritizedTasks());
        System.out.println("\nВременная сетка после добавления задач");
        System.out.println(instantManager.getTimeAvailabilityGrid());
        // Попытаемся добавить задачу, которая пересекается по времени с имеющимися
        instantManager.addTask(new Task("timeNameOverlapping", "timeDescriptionOverlapping",
                LocalDateTime.now().plusMinutes(26), Duration.ofMinutes(50)));
        System.out.println("\nЗадачи после добавления новой, пересекающейся с ними задачи");
        System.out.println(instantManager.getPrioritizedTasks());
        // Увидим, что задачи заняли соответствующие интервалы во временной сетки
        System.out.println("\nВременная сетка не изменилась:");
        System.out.println(instantManager.getTimeAvailabilityGrid());

        // Если попытаемся добавить новую задачу, пересекающуюся по времени, будет выброшено исключение
        /*manager.addTask(new Task("timeName2", "timeDescription1",
                LocalDateTime.now().plusMinutes(45), Duration.ofMinutes(95)));*/
    }

}