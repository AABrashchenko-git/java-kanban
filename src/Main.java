public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        /*String typeChoice = "Эпик";
        String name = "поесть";
        String description = "поесть вкусное что-нибудь";
        String progress = String.valueOf(Progress.IN_PROGRESS);
        Progress status = Progress.IN_PROGRESS;
        String epicName = "подзача1";



        Task task;

        switch(typeChoice) {
            case "Подзадача":
                task = new SubTask(epicName, name, description, status);
                TaskManager.addNewSubTask((SubTask) task);
                break;
            case "Задача":
                task = new Task(name, description, status);
                TaskManager.addNewTask(task);
                break;
            case "Эпик":
                task = new Epic(name, description, status);
                TaskManager.addNewEpic((Epic) task);
                break;
            default:
                break;
        }

        Task task1 = new Task("задача1", "поесть",  Progress.DONE);
        Epic task2 = new Epic("эпик1", "поработать", Progress.DONE);
        Task task3 = new SubTask("эпик1", "Подзадача1", "ПоработатьПРОВЕРКА РАБОТЫ МЕТОДА ПОЛУЧЕНИЯ ПОДЗАДАЧИ", Progress.DONE);

        TaskManager.addNewTask(task1);
        TaskManager.addNewEpic(task2);
        TaskManager.addNewSubTask((SubTask) task3);

        Task task4 = new SubTask("эпик1", "Подзадача2", "Поработать2", Progress.DONE);
        TaskManager.addNewSubTask((SubTask) task4);

        System.out.println(TaskManager.allTasks);
        System.out.println(task2.subTasks);

        TaskManager.printAllTasks();
        System.out.println(task2.getClass());

        System.out.println(task2.toString());
        System.out.println();
        TaskManager.printAllSubTask("эпик1");
        System.out.println();
        TaskManager.printAllEpics();
        System.out.println();
        TaskManager.printAllTasks();
        System.out.println();
        System.out.println(TaskManager.getTaskById("задача1"));

        TaskManager.printAllSubTask("эпик1");

       TaskManager.updateSubTaskStatus(Progress.NEW, "Подзадача1", "эпик1");

        TaskManager.printAllSubTask("эпик1");


       SubTask subTask1 = TaskManager.getSubTaskById("Подзадача1", "эпик1");
        System.out.println(subTask1);
        */


        Task task1 = new Task("Задача1", "поесть");
        Task task2 = new Task("Задача2", "сходить в туалет");
        Task task3 = new Task("Задача3", "посмотреть мемы");


        Epic epic1 = new Epic("Эпик1", "поработать");
        Epic epic2 = new Epic("Эпик2", "поработать");

        SubTask subTask1 = new SubTask("Эпик1", "Подзадача1", "Поработать1");
        SubTask subTask2 = new SubTask("Эпик1", "Подзадача2", "Поработать2");
        SubTask subTask3 = new SubTask("Эпик1", "Подзадача3", "Поработать3");
        SubTask subTask4 = new SubTask("Эпик1", "Подзадача4", "Поработать4");
        SubTask subTask77777777 = new SubTask("Эпик2", "Подзадача4666666666", "Поработать4");


       // System.out.println(epic1.progress);
       // System.out.println(subTask2.progress);
        //    TaskManager.updateSubTaskStatus("Подзадача1", "эпик1", "1");
        // SubTask subTask = TaskManager.getSubTaskById("Подзадача1", "эпик1");
       // System.out.println(subTask);
        TaskManager.addNewTask(task1);
        TaskManager.addNewTask(task2);
        TaskManager.addNewTask(task3);

        TaskManager.addNewEpic(epic1);
        TaskManager.addNewEpic(epic2);

        TaskManager.addNewSubTask(subTask1);
        TaskManager.addNewSubTask(subTask2);
        TaskManager.addNewSubTask(subTask3);
        TaskManager.addNewSubTask(subTask4);
        TaskManager.addNewSubTask(subTask77777777);


        TaskManager.printAllTasks();
        TaskManager.printAllEpics();
        TaskManager.printAllSubTask("Эпик1");
        System.out.println(epic1.getProgress());

        TaskManager.updateSubTaskStatus("Подзадача1", "Эпик1", Progress.NEW);
        TaskManager.updateSubTaskStatus("Подзадача2", "Эпик1", Progress.NEW);
        TaskManager.updateSubTaskStatus("Подзадача3", "Эпик1", Progress.NEW);
        TaskManager.updateSubTaskStatus("Подзадача4", "Эпик1", Progress.DONE);


        TaskManager.printAllSubTask("Эпик1");
        System.out.println(epic1.getProgress());
        System.out.println("..........................");
     //   TaskManager.removeSubTasksOfEpic("Эпик1");
     TaskManager.printAllSubTask("Эпик1");
      //  System.out.println( TaskManager.getEpicProgress(epic1));
        System.out.println("xtrfdfdf..........................");
        TaskManager.removeSubTaskOfEpic("Подзадача1", "Эпик1");
        TaskManager.removeSubTaskOfEpic("Подзадача2", "Эпик1");


        System.out.println("..........................");
        TaskManager.printAllSubTask("Эпик1");


        TaskManager.removeSubTaskOfEpic("Подзадача4666666666", "Эпик2");
        TaskManager.printAllSubTask("Эпик2");
       // System.out.println(Epic.getId);



    }





}
