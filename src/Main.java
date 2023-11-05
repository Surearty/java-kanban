
import manager.InMemoryTaskManager;
import task.*;

import java.io.IOException;
import java.time.Duration;


public class Main {

    public static void main(String[] args){
        InMemoryTaskManager tm = new InMemoryTaskManager();
        Epic epic = new Epic(TaskType.EPIC, "Эпик_1", TaskStatus.NEW, "Description");
        Task subtask = new SubTask(TaskType.SUBTASK, "Подзадача1_Эпик_1", TaskStatus.DONE, "Description", 1);
        Task subtask2 = new SubTask(TaskType.SUBTASK, "Подзадача2_Эпик_1", TaskStatus.DONE, "Description", 1);
        subtask.setDuration(Duration.ofMinutes(30));
        subtask.setStartTime("2023-02-16 13:26");
        subtask2.setDuration(Duration.ofMinutes(30));
       subtask2.setStartTime("2023-02-16 12:00");

        tm.createTask(epic);
        tm.createTask(subtask);
        tm.createTask(subtask2);


        tm.createTask(new Epic(TaskType.EPIC, "Эпик_1", TaskStatus.NEW, "Description","2023-02-16 12:00"));
        tm.createTask(new Epic(TaskType.EPIC, "Эпик_2", TaskStatus.NEW, "Description","2023-02-16 12:00"));
        tm.createTask(new SubTask(TaskType.SUBTASK, "Подзадача1_Эпик_1", TaskStatus.DONE, "Description", 4));
        tm.createTask(new SubTask(TaskType.SUBTASK, "Подзадача2_Эпик_1", TaskStatus.NEW, "Description", 4));
        tm.createTask(new SubTask(TaskType.SUBTASK, "Подзадача3_Эпик_1", TaskStatus.IN_PROGRESS, "Description", 5));
        System.out.println(tm.getPrioritizedTasks());

//
//        tm.getTask(1);
//        tm.getTask(2);
//        tm.getTask(5);
//        tm.getTask(6);
//
//        System.out.println();
//
//        System.out.println("Получение истории:");
//        System.out.println(tm.getHistory());
////        FileBackedTasksManager fb = new FileBackedTasksManager("history.csv");
////        fb.save();
    }
}
