
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Поехали!");

        Task task1 = new Task("Задача1", "поесть");
        Task task2 = new Task("Задача2", "сходить в туалет");
        Task task3 = new Task("Задача3", "посмотреть мемы");

        Epic epic1 = new Epic("Эпик1", "поработать1");
        Epic epic2 = new Epic("Эпик2", "поработать2");
        Epic epic3 = new Epic("Эпик3", "поработать3");

        SubTask subTask11 = new SubTask("Эпик1", "Подзадача1", "Поработать1");
        SubTask subTask12 = new SubTask("Эпик1", "Подзадача2", "Поработать2");
        SubTask subTask13 = new SubTask("Эпик1", "Подзадача3", "Поработать3");
        SubTask subTask14 = new SubTask("Эпик1", "Подзадача4", "Поработать4");

        SubTask subTask21 = new SubTask("Эпик2", "Подзадача01", "Поработать01");
        SubTask subTask22 = new SubTask("Эпик2", "Подзадача02", "Поработать02");
        SubTask subTask23 = new SubTask("Эпик2", "Подзадача03", "Поработать03");
        SubTask subTask24 = new SubTask("Эпик2", "Подзадача04", "Поработать04");

        // Добавление всех типов задач
        // Добавление задач
        TaskManager.addNewTask(task1);
        TaskManager.addNewTask(task2);
        TaskManager.addNewTask(task3);
        // Добавление эпиков
        TaskManager.addNewEpic(epic1);
        TaskManager.addNewEpic(epic2);
        TaskManager.addNewEpic(epic3);
        // Добавление подзадач
        TaskManager.addNewSubTask(subTask11);
        TaskManager.addNewSubTask(subTask12);
        TaskManager.addNewSubTask(subTask13);
        TaskManager.addNewSubTask(subTask14);

        TaskManager.addNewSubTask(subTask21);
        TaskManager.addNewSubTask(subTask22);
        TaskManager.addNewSubTask(subTask23);
        TaskManager.addNewSubTask(subTask24);

        while (true) {
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
        System.out.println("3. Удаление всех задач разного типа");
        System.out.println("4. Получение разного типа задач по идентификатору");
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
        System.out.println("3. Удаление всех задач разного типа");
        System.out.println("    1) Удаление задач");
        System.out.println("    2) Удаление эпиков");
        System.out.println("    3) Удаление всех подзадач конкретного эпика");
        System.out.println("    4) Удаление одной конкретной подзадачи конкретного эпика");

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
            default:
                System.out.println("Команда не найдена!");
                break;
        }
    }

    private static void manageOptionFour() {
        System.out.println("-".repeat(20));
        System.out.println("4. Получение разного типа задач по идентификатору");
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

        System.out.println("Введите номер нового статуса у задачи:");
        System.out.println("1 - NEW");
        System.out.println("2 - IN_PROGRESS");
        System.out.println("3 - DONE");

        String progressName = scanner.nextLine();
        Progress progress = getProgressByChoice(progressName);

        System.out.println("Введите желаемое действие: ");
        System.out.println("    1) Обновление статуса ЗАДАЧИ");
        System.out.println("    2) Обновление статуса ПОДЗАДАЧИ конкретного эпика");

        String command = scanner.nextLine();
        System.out.println("Введите имя выбранного типа задачи");
        String name = scanner.nextLine();

        switch (command) {
            case "1":
                TaskManager.updateTaskProgress(name, progress);
                break;
            case "2":
                System.out.println("Введите название эпика для выбранной подзадачи");
                String epicName = scanner.nextLine();
                TaskManager.updateSubTaskStatus(name, epicName, progress);
                break;
            default:
                System.out.println("Команда не найдена!");
                break;
        }

    }

    public static Progress getProgressByChoice(String progressName) {
        Progress progress;

        if (progressName.equals("1")) {
            progress = Progress.NEW;
        } else if (progressName.equals("2")) {
            progress = Progress.IN_PROGRESS;
        } else if (progressName.equals("3")) {
            progress = Progress.DONE;
        } else {
            System.out.println("Неизвестная команда! По умолчанию статус IN_PROGRESS");
            progress = Progress.IN_PROGRESS;
        }
        return progress;
    }
}
