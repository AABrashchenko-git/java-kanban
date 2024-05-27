
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Поехали!");

        Task task1 = new Task("name1", "descrooption1");
        TaskManager.addTask(task1);
        System.out.println(TaskManager.getAllTasks());
        System.out.println(task1.getId());
        System.out.println(task1.getStatus());
        System.out.println(task1.getName());
        System.out.println(task1.getDescription());


        Epic epic1 = new Epic("epicName1", "epicDescription1");
        TaskManager.addEpic(epic1);
        System.out.println(TaskManager.getAllEpics());
        System.out.println(epic1.getId());
        System.out.println(epic1.getStatus());
        System.out.println(epic1.getName());
        System.out.println(epic1.getDescription());



        SubTask subTask1 = new SubTask(epic1.getId(), "подзадача1 эпика1", "описание 1 подзадачи1 эпика 1");
        TaskManager.addSubTask(subTask1);
        System.out.println("......");
       System.out.println(TaskManager.getAllEpics());
        System.out.println("......");
        System.out.println(subTask1.getId());
        System.out.println(subTask1.getEpicId());

        System.out.println(subTask1.getStatus());
        System.out.println(subTask1.getName());
        System.out.println(subTask1.getDescription());
        System.out.println("...... обновляем подзадачу");
        SubTask subTask01 = new SubTask(subTask1.getId(), "новое имя подзадачи1 эпика1", "новое описание пподзадачи1 эпика1", Status.DONE);
        System.out.println(subTask1.getEpicId());
        System.out.println(subTask01.getEpicId());

    SubTask subTask1updated = TaskManager.updateSubTask(subTask01);
       System.out.println(subTask1updated);









    }

}
/*        while (true) {
            printMenu();
            String command = scanner.nextLine();

            switch (command) {
                case "1":
                    manageOptionOne();
                    break;
                case "2":
                    manageOptionTwo();
                    break;
                case "3":
                    manageOptionThree();
                    break;
                case "4":
                    manageOptionFour();
                    break;
                case "5":
                    manageOptionFive();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Команда не найдена!");
                    break;
            }
        }
    }

    private static void printMenu() {
        System.out.println("-".repeat(20));
        System.out.println("Выберите тип операций с задачами:");
        System.out.println("1. Создание(обновление) задачи (с добавлением в список)");
        System.out.println("2. Получение списка каждого типа задач");
        System.out.println("3. Удаление задач разного типа (всех и по отдельности)");
        System.out.println("4. Получение разного типа задач по идентификатору (имя, описание, статус)");
        System.out.println("5. Обновление статуса задачи, подзадачи конкретного эпика");
        System.out.println("6. Выход");
    }

    private static void manageOptionOne() {
        System.out.println("-".repeat(20));
        // В ТЗ прописано, что для обновления и для добавления используем один и тот же метод. Для изменения статуса
        // задач и подзадач используем manageOptionFive()
        System.out.println("1. Создание задачи (добавление в список)");
        System.out.println("    1) Добавление (обновление)  задачи");
        System.out.println("    2) Добавление (обновление)  эпика");
        System.out.println("    3) Добавление (обновление) подзадачи для выбранного эпика");

        String command = scanner.nextLine();
        System.out.println("Введите имя выбранного типа задачи");
        String name = scanner.nextLine();
        System.out.println("Введите описание выбранного типа задачи");
        String description = scanner.nextLine();

        switch (command) {
            case "1":
                Task task = new Task(name, description);
                TaskManager.addNewTask(task);
                break;
            case "2":
                Epic epic = new Epic(name, description);
                TaskManager.addNewEpic(epic);
                break;
            case "3":
                System.out.println("Введите название эпика для выбранной подзадачи");
                String epicName = scanner.nextLine();
                SubTask subTask = new SubTask(epicName, name, description);
                if(!TaskManager.checkIfInputTaskExists(TaskManager.getEpicById(epicName))) {
                    System.out.println("Эпик не найден!");
                    break;
                }
                TaskManager.addNewSubTask(subTask);
                break;
            default:
                System.out.println("Команда не найдена!");
                break;
        }
    }

    private static void manageOptionTwo() {
        System.out.println("-".repeat(20));
        System.out.println("2. Получение списка каждого типа задач");
        System.out.println("    1) Получение списка всех задач");
        System.out.println("    2) Получение списка всех эпиков");
        System.out.println("    3) Получение списка всех подзадач конкретного эпика");

        String command = scanner.nextLine();

        switch (command) {
            case "1":
                TaskManager.printAllTasks();
                break;
            case "2":
                TaskManager.printAllEpics();
                break;
            case "3":
                System.out.println("Введите название эпика:");
                String epicName = scanner.nextLine();
                TaskManager.printAllSubTask(epicName);
                break;
            default:
                System.out.println("Команда не найдена!");
                break;
        }
    }

    private static void manageOptionThree() {
        System.out.println("-".repeat(20));
        System.out.println("3. Удаление задач разного типа (всех и по отдельности)");
        System.out.println("    1) Удаление всех задач");
        System.out.println("    2) Удаление всех эпиков");
        System.out.println("    3) Удаление всех подзадач конкретного эпика");
        System.out.println("    4) Удаление одной конкретной подзадачи конкретного эпика");
        System.out.println("    5) Удаление одного эпика");
        System.out.println("    6) Удаление одной задачи");

        String command = scanner.nextLine();

        switch (command) {
            case "1":
                TaskManager.removeTasks();
                break;
            case "2":
                TaskManager.removeEpics();
                break;
            case "3":
                System.out.println("Введите название эпика:");
                String epicName = scanner.nextLine();
                TaskManager.removeSubTasksOfEpic(epicName);
                break;
            case "4":
                System.out.println("Введите название эпика:");
                String newEpicName = scanner.nextLine();
                System.out.println("Введите название подзадачи:");
                String subTaskName = scanner.nextLine();
                TaskManager.removeOneSubTaskOfEpic(subTaskName, newEpicName);
                break;
            case "5":
                System.out.println("Введите название эпика:");
                String epicToRemove = scanner.nextLine();
                TaskManager.removeOneEpic(epicToRemove);
                break;
            case "6":
                System.out.println("Введите название задачи:");
                String taskToRemove = scanner.nextLine();
                TaskManager.removeOneTask(taskToRemove);
                break;
            default:
                System.out.println("Команда не найдена!");
                break;
        }
    }

    private static void manageOptionFour() {
        System.out.println("-".repeat(20));
        System.out.println("4. Получение разного типа задач по идентификатору (имя, описание, статус)");
        System.out.println("    1) Получение конкретной задачи по имени");
        System.out.println("    2) Получение конкретного эпика по имени");
        System.out.println("    3) Получение конкретной подзадачи по имени и по эпику");

        String command = scanner.nextLine();

        switch (command) {
            case "1":
                System.out.println("Введите название задачи:");
                String taskName = scanner.nextLine();
                Task taskFound = TaskManager.getTaskById(taskName);
                System.out.println(taskFound);
                break;
            case "2":
                System.out.println("Введите название эпика:");
                String epicName = scanner.nextLine();
                Epic epicFound = TaskManager.getEpicById(epicName);
                System.out.println(epicFound);
                break;
            case "3":
                System.out.println("Введите название эпика:");
                String newEpicName = scanner.nextLine();
                System.out.println("Введите название подзадачи:");
                String subTaskName = scanner.nextLine();
                if(!TaskManager.checkIfInputTaskExists(TaskManager.getEpicById(newEpicName))) {
                    System.out.println("Эпик не найден!");
                    break;
                }

                SubTask subTaskFound = TaskManager.getSubTaskById(subTaskName, newEpicName);
                System.out.println(subTaskFound);
                break;
            default:
                System.out.println("Команда не найдена!");
                break;
        }
    }

    private static void manageOptionFive() {
        System.out.println("-".repeat(20));
        System.out.println("5. Обновление статуса задачи, подзадачи конкретного эпика");
        System.out.println("Введите номер нового статуса, на который вы хотите обновить текущий статус задачи:");
        System.out.println("1 - NEW");
        System.out.println("2 - IN_PROGRESS");
        System.out.println("3 - DONE");

        String statusName = scanner.nextLine();
        Status status = getStatusByChoice(statusName);

        System.out.println("Введите тип задачи, статус которой вы хотите обновить: ");
        System.out.println("    1) Обновление статуса ЗАДАЧИ");
        System.out.println("    2) Обновление статуса ПОДЗАДАЧИ конкретного эпика");

        String command = scanner.nextLine();
        System.out.println("Введите имя выбранного типа задачи");
        String name = scanner.nextLine();

        switch (command) {
            case "1":
                TaskManager.updateTaskStatus(name, status);
                break;
            case "2":
                System.out.println("Введите название эпика для выбранной подзадачи");
                String epicName = scanner.nextLine();
                TaskManager.updateSubTaskStatus(name, epicName, status);
                break;
            default:
                System.out.println("Команда не найдена!");
                break;
        }

    }

    public static Status getStatusByChoice(String statusName) {
        Status status;

        if (statusName.equals("1")) {
            status = Status.NEW;
        } else if (statusName.equals("2")) {
            status = Status.IN_PROGRESS;
        } else if (statusName.equals("3")) {
            status = Status.DONE;
        } else {
            System.out.println("Неизвестная команда! По умолчанию статус IN_PROGRESS");
            status = Status.IN_PROGRESS;
        }
        return status;
    }*/


