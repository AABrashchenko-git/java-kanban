import ru.practicum.taskTracker.model.Epic;
import ru.practicum.taskTracker.model.Status;
import ru.practicum.taskTracker.model.SubTask;
import ru.practicum.taskTracker.model.Task;
import ru.practicum.taskTracker.service.TaskManager;

/** Спасибо за проверку и комментарии! Пришлось помучаться и переделать всё практически с нуля,
 * но было интересно :)
 * В Main убрал консольный интерфейс, но добавил вывод в консоль работу всех требуемых методов из ТЗ
 * P.S. Отдельно лагодарю за StringBuilder, крутая штука. Было бы просто супер, если при ревью
 * от тебя можно было бы получать вот такие рекомендации о новых методах, которые можно применить
 * в той или иной ситуации
 */

public class Main {
    public static void main(String[] args) {
        // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
        // a. Создание. Сам объект должен передаваться в качестве параметра.
            // 1) Задачи
        Task task1 = new Task("имяЗадачи1", "описаниеЗадачи1");
        Task task2 = new Task("имяЗадачи2", "описаниеЗадачи2");
        TaskManager.addTask(task1);
        TaskManager.addTask(task2);
            // 2) Эпики
        Epic epic1 = new Epic("имяЭпика1", "описаниеЭпика1");
        Epic epic2 = new Epic("имяЭпика2", "описаниеЭпика2");
        TaskManager.addEpic(epic1);
        TaskManager.addEpic(epic2);
            // 3) Подзадачи
        SubTask subTask1 = new SubTask(epic1.getId(), "имяПодзадачи1Эпика1",
                "описаниеПодзадачи1Эпика1");
        SubTask subTask2 = new SubTask(epic1.getId(), "имяПодзадачи2Эпика1",
                "описаниеПодзадачи2Эпика1");
        SubTask subTask3 = new SubTask(epic2.getId(), "имяПодзадачи3Эпика1",
                "описаниеПодзадачи3Эпика1");

        TaskManager.addSubTask(subTask1);
        TaskManager.addSubTask(subTask2);
        TaskManager.addSubTask(subTask3);

        // b. Получение списка всех задач.
            // 1) Задачи
        System.out.println("\nвсе задачи:");
        System.out.println(TaskManager.getAllTasks());
            // 2) Эпики
        System.out.println("\nвсе эпики:");
        System.out.println(TaskManager.getAllEpics());
            // 3) Подзадачи
        System.out.println("\nвсе подзадачи:");
        System.out.println(TaskManager.getAllSubTasks());
        // c. Дополнительные методы:
            //1) Получение списка всех подзадач определённого эпика.
        System.out.println("\nСписок всех подзадач определенного эпика epic1 (id=3)");
        System.out.println(TaskManager.getAllSubTaskOfEpic(epic1.getId()));
        System.out.println("..................................");
        // d. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
            // 1) Задачи
        System.out.println("\nобновляем задачу1, выводим в консоль:");
        Task task01 = new Task(task1.getId(), "НовоеИмяЗадачи1", "новоеОписаниеЗадачи1", Status.DONE);
        TaskManager.updateTask(task01);
        System.out.println(TaskManager.getTaskById(task1.getId()));
            // 2) Эпики
        System.out.println("\nобновляем эпик1, выводим в консоль :");
        Epic epic01 = new Epic(epic1.getId(), "НовоеИмяЭпика1", "новоеОписаниеЭпика1");
        TaskManager.updateEpic(epic01);
        System.out.println(TaskManager.getEpicById(epic1.getId()));
            // 3) Подзадачи
        System.out.println("\nобновляем подзадачи1-3, выводим эпики в консоль, смотрим новые статусы подзадач и эпиков:");

        SubTask subTask01 = new SubTask(subTask1.getEpicId(), subTask1.getId(), "новоеИмяПодзадачи1Эпика1",
                "новоеОписаниеПодзадачи1Эпика1", Status.DONE);
        SubTask subTask02 = new SubTask(subTask2.getEpicId(), subTask2.getId(), "новоеИмяПодзадачи2Эпика1",
                "новоеОписаниеПодзадачи2Эпика1", Status.IN_PROGRESS);
        SubTask subTask03 = new SubTask(subTask3.getEpicId(), subTask3.getId(), "новоеИмяПодзадачи1Эпика2",
                "новоеОписаниеПодзадачи1Эпика2", Status.DONE);

        TaskManager.updateSubTask(subTask01);
        TaskManager.updateSubTask(subTask02);
        TaskManager.updateSubTask(subTask03);
        System.out.println(TaskManager.getAllEpics());

        // e. Получение по идентификатору
            // 1) Задачи
        System.out.println("\nПолучаем задачу по id:");
        Task getTask = TaskManager.getTaskById(task1.getId());
        System.out.println(getTask);
            // 2) Эпики
        System.out.println("\nПолучаем эпик по id:");
        Epic getEpic = TaskManager.getEpicById(epic1.getId());
        System.out.println(getEpic);
            // 3) Подзадачи
        System.out.println("\nПолучаем подзадачу по id:");
        SubTask getSubTask = TaskManager.getSubTaskById(subTask1.getId());
        System.out.println(getSubTask);
        // f. Удаление по идентификатору.
            // 1) Задачи
        TaskManager.removeOneTaskById(task1.getId());
        System.out.println("\nСписок задач после удаления task1:");
        System.out.println(TaskManager.getAllTasks());
            // 2) Эпики
        TaskManager.removeOneEpicById(epic1.getId());
        System.out.println("\nСписок эпиков после удаления epic1:");
        System.out.println(TaskManager.getAllEpics());
            // 3) Подзадачи
        TaskManager.removeOneSubTaskById(subTask3.getId());
        System.out.println("\nСписок подзадач после удаления subTask3:");
        System.out.println(TaskManager.getAllSubTasks());
        System.out.println("\nПосмотрим на статусы эпиков (обновились ли):");
        System.out.println(TaskManager.getAllEpics());
        // g. Удаление всех задач.
            // 1) Задачи
        TaskManager.removeTasks();
        System.out.println("\nСписок задач после удаления:");
        System.out.println(TaskManager.getAllTasks());
            // 2) Подзадачи
        TaskManager.removeSubTasks();
        System.out.println("\nСписок подзадач после удаления:");
        System.out.println(TaskManager.getAllSubTasks());
        System.out.println("\nПосмотрим на статусы эпиков (обновились ли):");
        System.out.println(TaskManager.getAllEpics());
            // 3) Эпики
        TaskManager.removeEpics();
        System.out.println("\nСписок эпиков после удаления:");
        System.out.println(TaskManager.getAllEpics());

        if (TaskManager.getAllEpics().isEmpty() && TaskManager.getAllTasks().isEmpty() &&
                TaskManager.getAllSubTasks().isEmpty()) {
            System.out.println("\nВсе списки задач пусты!");
        }
    }
}