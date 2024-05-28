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
        SubTask subTask4 = new SubTask(epic1.getId(), "имяПодзадачи1Эпика2", "описаниеПодзадачи1Эпика2");
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

        // b. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        // 1) Задачи
        Task task01 = new Task(task1.getId(), "НовоеИмяЗадачи1", "новоеОписаниеЗадачи1", Status.DONE);
        TaskManager.updateTask(task01);
        System.out.println(TaskManager.getTaskById(task01.getId()));
        System.out.println(TaskManager.getTaskById(task1.getId()));
        // 2) Эпики

        // 3) Подзадачи


        // c. Получение по идентификатору.
        // 1) Задачи

        // 2) Эпики

        // 3) Подзадачи




        // e. Удаление всех задач.
        // 1) Задачи

        // 2) Эпики

        // 3) Подзадачи


        // f. Удаление по идентификатору.
        // 1) Задачи

        // 2) Эпики

        // 3) Подзадачи

        // Дополнительные методы:

        //a. Получение списка всех подзадач определённого эпика.

    }
}