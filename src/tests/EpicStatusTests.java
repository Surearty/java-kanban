package tests;
import manager.*;
import org.junit.jupiter.api.BeforeEach;
import task.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;


public class EpicStatusTests {
    private final InMemoryTaskManager tm = new InMemoryTaskManager();

    @BeforeEach
    public void createEpic(){
        Epic epic = new Epic(TaskType.EPIC, "Эпик_1", TaskStatus.NEW, "Description");
        tm.createTask(epic);
    }

    @Test
    public void emptySubtaskList(){
        Assertions.assertEquals(tm.getTask(1).getStatus(), TaskStatus.NEW);
    }
    @Test
    public void epicStatusIsNEW(){
        tm.createTask(new SubTask(TaskType.SUBTASK, "Подзадача1_Эпик_1", TaskStatus.NEW, "Description1", 1));
        tm.createTask(new SubTask(TaskType.SUBTASK, "Подзадача2_Эпик_1", TaskStatus.NEW, "Description2", 1));
        Assertions.assertEquals(tm.getTask(1).getStatus(), TaskStatus.NEW);
    }
    @Test
    public void EpicChangeStatusToDONE(){
        tm.createTask(new SubTask(TaskType.SUBTASK, "Подзадача1_Эпик_1", TaskStatus.DONE, "Description1", 1));
        tm.createTask(new SubTask(TaskType.SUBTASK, "Подзадача2_Эпик_1", TaskStatus.DONE, "Description2", 1));
        Assertions.assertEquals(tm.getTask(1).getStatus(), TaskStatus.DONE);
    }
    @Test
    public void EpicChangeStatusToINPROGRESS1(){
        tm.createTask(new SubTask(TaskType.SUBTASK, "Подзадача1_Эпик_1", TaskStatus.DONE, "Description1", 1));
        tm.createTask(new SubTask(TaskType.SUBTASK, "Подзадача2_Эпик_1", TaskStatus.NEW, "Description2", 1));
        Assertions.assertEquals(tm.getTask(1).getStatus(), TaskStatus.IN_PROGRESS);
    }
    @Test
    public void EpicChangeStatusToINPROGRESS2(){
        tm.createTask(new SubTask(TaskType.SUBTASK, "Подзадача1_Эпик_1", TaskStatus.IN_PROGRESS, "Description1", 1));
        tm.createTask(new SubTask(TaskType.SUBTASK, "Подзадача2_Эпик_1", TaskStatus.IN_PROGRESS, "Description2", 1));
        Assertions.assertEquals(tm.getTask(1).getStatus(), TaskStatus.IN_PROGRESS);
    }
    @Test
    public void EpicStartTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        SubTask sub1 = new SubTask(TaskType.SUBTASK, "Подзадача1_Эпик_1", TaskStatus.DONE, "Description", 1);
        SubTask sub2 = new SubTask(TaskType.SUBTASK, "Подзадача2_Эпик_1", TaskStatus.NEW, "Description", 1);
        sub1.setStartTime("2023-02-09 22:30");
        sub2.setStartTime("2023-02-09 23:30");
        tm.createTask(sub1);
        tm.createTask(sub2);

        Assertions.assertEquals("2023-02-09 22:30", tm.getTask(1).getStartTime().format(formatter));
    }
}
