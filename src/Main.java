public class Main {
    public static void main(String[] args) {

        // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
        // a. Создание. Сам объект должен передаваться в качестве параметра.
        // 1) Задачи
        Task task1 = new Task("имяЗадачи1", "описаниеЗадачи1");
        Task task2 = new Task("имяЗадачи2", "описаниеЗадачи2");
        Task task3 = new Task("имяЗадачи3", "описаниеЗадачи3");
        Task task4 = new Task("имяЗадачи4", "описаниеЗадачи4");
        TaskManager.addTask(task1);
        TaskManager.addTask(task2);
        TaskManager.addTask(task3);
        TaskManager.addTask(task4);

        // 2) Эпики
        Epic epic1 = new Epic("имяЭпика1", "описаниеЭпика1");
        Epic epic2 = new Epic("имяЭпика2", "описаниеЭпика2");
        Epic epic3 = new Epic("имяЭпика3", "описаниеЭпика3");
        Epic epic4 = new Epic("имяЭпика4", "описаниеЭпика4");

        TaskManager.addEpic(epic1);
        TaskManager.addEpic(epic2);
        TaskManager.addEpic(epic3);
        TaskManager.addEpic(epic4);

        // 3) Подзадачи
        SubTask subTask1 = new SubTask(epic1.getId(), "имяПодзадачи1Эпика1", "описаниеПодзадачи1Эпика1");
        SubTask subTask2 = new SubTask(epic1.getId(), "имяПодзадачи2Эпика1", "описаниеПодзадачи2Эпика1");
        SubTask subTask3 = new SubTask(epic1.getId(), "имяПодзадачи3Эпика1", "описаниеПодзадачи3Эпика1");
        SubTask subTask4 = new SubTask(epic2.getId(), "имяПодзадачи1Эпика2", "описаниеПодзадачи1Эпика2");
        SubTask subTask5 = new SubTask(epic2.getId(), "имяПодзадачи2Эпика2", "описаниеПодзадачи2Эпика2");
        SubTask subTask6 = new SubTask(epic3.getId(), "имяПодзадачи1Эпика3", "описаниеПодзадачи1Эпика3");

        TaskManager.addSubTask(subTask1);
        TaskManager.addSubTask(subTask2);
        TaskManager.addSubTask(subTask3);
        TaskManager.addSubTask(subTask4);
        TaskManager.addSubTask(subTask5);
        TaskManager.addSubTask(subTask6);

        // d. Получение списка всех задач.
        // 1) Задачи
        System.out.println("...............");
        System.out.println("все задачи:");
        System.out.println(TaskManager.getAllTasks());
        // 2) Эпики
        System.out.println("...............");
        System.out.println("все эпики:");
        System.out.println(TaskManager.getAllEpics());
        // 3) Подзадачи
        System.out.println("...............");
        System.out.println("все подзадачи:");
        System.out.println(TaskManager.getAllSubTasks());
        System.out.println("..................................");
        // Дополнительные методы:
        //a. Получение списка всех подзадач определённого эпика.
        System.out.println("Получим список всех подзадач определенного эпика эпик01");
        System.out.println(TaskManager.getAllSubTaskOfEpic(epic1.getId()));
        System.out.println("..................................");
        // b. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        // 1) Задачи
        System.out.println("обновляем задачу:");
        Task task01 = new Task(task1.getId(), "НовоеИмяЗадачи1", "новоеОписаниеЗадачи1", Status.DONE);
        TaskManager.updateTask(task01);
        System.out.println(TaskManager.getTaskById(task1.getId()));
        // 2) Эпики
        System.out.println("обновляем эпик:");
        Epic epic01 = new Epic(epic1.getId(), "НовоеИмяЭпика1", "новоеОписаниеЭпика1");
        TaskManager.updateEpic(epic01);
        System.out.println(TaskManager.getEpicById(epic1.getId()));
        // 3) Подзадачи
        System.out.println("обновляем подзадачу:");
        SubTask subTask01 = new SubTask(subTask1.getEpicId(), subTask1.getId(), "новоеИмяПодзадачи1Эпика1", "новоеОписаниеПодзадачи1Эпика1", Status.DONE);
        SubTask subTask02 = new SubTask(subTask2.getEpicId(), subTask2.getId(), "sdfdfs1", "fsdfd", Status.DONE);
        SubTask subTask03 = new SubTask(subTask3.getEpicId(), subTask3.getId(), "sdfsdsddfs1", "fsdfsdsdd", Status.DONE);
        SubTask subTask04 = new SubTask(subTask4.getEpicId(), subTask4.getId(), "11dfsdsddfs1", "1fsdfsdsdd", Status.DONE);

        TaskManager.updateSubTask(subTask01);
        TaskManager.updateSubTask(subTask02);
        TaskManager.updateSubTask(subTask03);
        TaskManager.updateSubTask(subTask04);
        System.out.println(TaskManager.getSubTaskById(subTask01.getId()));

        System.out.println(TaskManager.getAllEpics());
        // c. Получение по идентификатору.
        // 1) Задачи
        System.out.println("Получаем задачу по id:");
        Task getTask = TaskManager.getTaskById(task1.getId());
        System.out.println(getTask);
        // 2) Эпики
        System.out.println("Получаем эпик по id:");
        Epic getEpic = TaskManager.getEpicById(epic1.getId());
        System.out.println(getEpic);
        // 3) Подзадачи
        System.out.println("Получаем подзадачу по id:");
        SubTask getSubTask = TaskManager.getSubTaskById(subTask1.getId());
        System.out.println(getSubTask);
        // f. Удаление по идентификатору.
            // 1) Задачи
        TaskManager.removeOneTaskById(task1.getId());
        System.out.println("Список задач после удаления task1:");
        System.out.println(TaskManager.getAllTasks());
            // 2) Эпики
        TaskManager.removeOneEpicById(epic1.getId());
        System.out.println("Список эпиков после удаления epic1:");
        System.out.println(TaskManager.getAllEpics());
            // 3) Подзадачи
        TaskManager.removeOneSubTaskById(subTask4.getId());
        System.out.println("Список подзадач после удаления subTask4:");
        System.out.println(TaskManager.getAllSubTasks());
        System.out.println("Посмотрим на статусы эпиков (обновились ли):");
        System.out.println(TaskManager.getAllEpics());
            // e. Удаление всех задач.
        // 1) Задачи
        TaskManager.removeTasks();
        System.out.println("Список задач после удаления:");
        System.out.println(TaskManager.getAllTasks());
        // 2) Подзадачи
        TaskManager.removeSubTasks();
        System.out.println("Список подзадач после удаления:");
        System.out.println(TaskManager.getAllSubTasks());
        System.out.println("Посмотрим на статусы эпиков (обновились ли):");
        System.out.println(TaskManager.getAllEpics());
        // 2) Эпики
        TaskManager.removeEpics();
        System.out.println("Список эпиков после удаления:");
        System.out.println(TaskManager.getAllEpics());

    }
}