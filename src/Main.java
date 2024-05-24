import java.sql.SQLOutput;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Поехали!");

        Task task1 = new Task("Задача1", "поесть");
        Task task2 = new Task("Задача2", "сходить в туалет");
        Task task3 = new Task("Задача3", "посмотреть мемы");


        Epic epic1 = new Epic("Эпик1", "поработать");
        Epic epic2 = new Epic("Эпик2", "поработать");
        Epic epic3 = new Epic("Эпик3", "поработать");

        SubTask subTask1 = new SubTask("Эпик1", "Подзадача1", "Поработать1");
        SubTask subTask2 = new SubTask("Эпик1", "Подзадача2", "Поработать2");
        SubTask subTask3 = new SubTask("Эпик1", "Подзадача3", "Поработать3");
        SubTask subTask4 = new SubTask("Эпик1", "Подзадача4", "Поработать4");


        SubTask subTask01 = new SubTask("Эпик2", "Подзадача01", "Поработать01");
        SubTask subTask02= new SubTask("Эпик2", "Подзадача02", "Поработать02");
        SubTask subTask03 = new SubTask("Эпик2", "Подзадача03", "Поработать03");
        SubTask subTask04 = new SubTask("Эпик2", "Подзадача04", "Поработать04");



        TaskManager.addNewTask(task1);
        TaskManager.addNewTask(task2);
        TaskManager.addNewTask(task3);

        TaskManager.addNewEpic(epic1);
        TaskManager.addNewEpic(epic2);
        TaskManager.addNewEpic(epic3);


        TaskManager.addNewSubTask(subTask1);
        TaskManager.addNewSubTask(subTask2);
        TaskManager.addNewSubTask(subTask3);
        TaskManager.addNewSubTask(subTask4);


        TaskManager.addNewSubTask(subTask01);
        TaskManager.addNewSubTask(subTask02);
        TaskManager.addNewSubTask(subTask03);
        TaskManager.addNewSubTask(subTask04);


        TaskManager.printAllTasks();
        TaskManager.printAllEpics();
        TaskManager.printAllSubTask("Эпик1");
        TaskManager.printAllSubTask("Эпик2");
        System.out.println("...........");
        TaskManager.removeOneEpic("Эпик22");
        TaskManager.printAllEpics();
        System.out.println("...........");
        TaskManager.printAllTasks();
        TaskManager.removeOneTask("Задаvча1");
        System.out.println("...........");

        TaskManager.printAllTasks();
        System.out.println("...........");
        System.out.println("Подзадачи перед удалением:");
        TaskManager.printAllSubTask("Эпик2");
        TaskManager.removeOneSubTaskOfEpic("Подзадача01", "Эпик2");
        System.out.println("Подзадачи после удаления:");
        TaskManager.printAllSubTask("Эпик2");


        System.out.println("Текущие epics");
        TaskManager.printAllEpics();
        System.out.println("Текущие сабтаски эпика1");
        TaskManager.printAllSubTask("Эпик1");
        System.out.println("удаляем сабтаски эпика1");
        // TaskManager.removeSubTasksOfEpic("Эпик1");
        System.out.println("Текущие сабтаски эпика1");
        TaskManager.printAllSubTask("Эпик1");


        System.out.println(TaskManager.allTasks);
        task1 = new Task("Задача1", "поесть111");
        TaskManager.updateTask(task1, Progress.IN_PROGRESS);

        System.out.println(TaskManager.allTasks);


        System.out.println("Текущие epics");
        TaskManager.printAllEpics();
        TaskManager.printAllSubTask("Эпик1");

        epic1 = new Epic("Эпик1", "п1оработать");
        // TaskManager.updateEpic(epic1);
        TaskManager.printAllSubTask("Эпик1");
/*        TaskManager.updateSubTask(subTask1, Progress.DONE);
        TaskManager.updateSubTask(subTask2, Progress.DONE);
        TaskManager.updateSubTask(subTask3, Progress.DONE);*/
        System.out.println("Начинаем менять статусу задач: ");
        TaskManager.updateSubTaskStatus("Подзадача1", "Эпик1", Progress.DONE);
        TaskManager.updateSubTaskStatus("Подзадача2", "Эпик1", Progress.DONE);
        TaskManager.updateSubTaskStatus("Подзадача3", "Эпик1", Progress.IN_PROGRESS);
        TaskManager.updateSubTaskStatus("Подзадача4", "Эпик1", Progress.DONE);
        TaskManager.printAllSubTask("Эпик1");

       System.out.println(epic2.progress);

       TaskManager.printAllEpics();













        /* while (true) {
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
                    return;
            }
        }*/




    }

    private static void printMenu() {
        System.out.println("-".repeat(20));
        System.out.println("Выберите тип операций с задачами:");
        System.out.println("1. Создание задачи (добавление в список)");
        System.out.println("2. Получение списка каждого типа задач");
        System.out.println("3. Удаление всех задач разного типа");
        System.out.println("4. Получение разного типа задач по идентификатору");
        System.out.println("5. Обновление задачи, подзадачи, эпика");
        System.out.println("6. Выход");
    }

    private static void manageOptionOne() {
        System.out.println("-".repeat(20));
        System.out.println("1. Создание задачи (добавление в список)");
        System.out.println("    1) Добавление  задачи");
        System.out.println("    2) Добавление  эпика");
        System.out.println("    3) Добавление подзадачи для выбранного эпика");

        String command = scanner.nextLine();
        System.out.println("Введите имя выбранного типа задачи");
        String name = scanner.nextLine();
        System.out.println("Введите описание выбранного типа задачи");
        String description = scanner.nextLine();
        Task task = new Task(name, description);

        switch (command) {
            case "1", "2":
                TaskManager.addNewTask(task);
                break;
            case "3":
                System.out.println("Введите название эпика для выбранной подзадачи");
                String epicName = scanner.nextLine();
                SubTask subTask = new SubTask(epicName, name, description);
                TaskManager.addNewSubTask(subTask);
                break;
            default:
                return;
        }







    }
    private static void manageOptionTwo() {
        System.out.println("-".repeat(20));
        System.out.println("2. Получение списка каждого типа задач");
        System.out.println("    1) Получение списка всех задач");
        System.out.println("    2) Получение списка всех эпиков");
        System.out.println("    3) Получение списка всех подзадач конкретного эпика");





    }
    private static void manageOptionThree() {
        System.out.println("-".repeat(20));
        System.out.println("3. Удаление всех задач разного типа");
        System.out.println("    1) Удаление задач");
        System.out.println("    2) Удаление эпиков");
        System.out.println("    3) Удаление всех подзадач конкретного эпика");
        System.out.println("    4) Удаление одной конкретной задачи конкретного эпика");
    }
    private static void manageOptionFour() {
        System.out.println("-".repeat(20));
        System.out.println("4. Получение разного типа задач по идентификатору");
        System.out.println("    1) Получение конкретной задачи по имени");
        System.out.println("    2) Получение конкретного эпика по имени");
        System.out.println("    3) Получение конкретной подзадачи по имени и по эпику");
    }
    private static void manageOptionFive() {
        System.out.println("-".repeat(20));
        System.out.println("5. Обновление задачи, подзадачи, эпика");
        System.out.println("    1) Обновление задачи");
        System.out.println("    2) Обновление эпика");
        System.out.println("    3) Обновление подзадачи конкретного эпика");
    }


}
