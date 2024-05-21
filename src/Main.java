public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        String taskType = "Задача";
        String name = "поесть";
        String description = "поесть вкусное что-нибудь";
        String progress = String.valueOf(Progress.IN_PROGRESS);

        Task task = TaskManager.composeNewTask(taskType, name, description, Progress.IN_PROGRESS);
        TaskManager.addNewTask(task);

        taskType = "Эпик";
        name = "имя";
        description = "описание";
        progress = String.valueOf(Progress.NEW);

        Task task1 = TaskManager.composeNewTask(taskType, name, description, Progress.NEW);
        TaskManager.addNewTask(task1);
        task1 = (Epic) task1;

        taskType = "Подзадача";
        name = "имя1";
        description = "описание1";
        progress = String.valueOf(Progress.NEW);
        Task task2 = TaskManager.composeNewTask(taskType, name, description, Progress.NEW);

        ((Epic) task1).addNewSubTask((SubTask) task2);

        System.out.println(TaskManager.allTasks);
        System.out.println(((Epic) task1).subTasks);
        // System.out.println(task1.getClass());
        task2.setProgress(Progress.DONE);

        System.out.println(((Epic) task1).subTasks);



    }





}
