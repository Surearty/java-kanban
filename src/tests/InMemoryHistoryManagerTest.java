package tests;

import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InMemoryHistoryManagerTest {
    InMemoryHistoryManager hm;
    Task task = new Task(TaskType.TASK, "Задача_1", TaskStatus.NEW, "Description");
    Epic epic = new Epic(TaskType.EPIC, "Эпик_1", TaskStatus.NEW, "Description");
    SubTask subTask = new SubTask(TaskType.SUBTASK, "Подзадача2_Эпик_1", TaskStatus.NEW, "Description", 2);

    @BeforeEach
    public void beforeEach() {
        hm = new InMemoryHistoryManager();
        task.setId(1);
        epic.setId(2);
        subTask.setId(3);
        hm.add(task);
        hm.add(epic);
        hm.add(subTask);
    }

    @Test
    public void addTaskTest() {
        List<Task> tasks = Arrays.asList(task, epic, subTask);

        Assertions.assertEquals(tasks, hm.getHistory());
        hm.add(null);
        Assertions.assertEquals(tasks, hm.getHistory());
    }

    @Test
    public void removeTaskTest() {
        hm.remove(2);
        Assertions.assertEquals(Arrays.asList(task, subTask), hm.getHistory());
        hm.remove(1);
        Assertions.assertEquals(Collections.singletonList(subTask), hm.getHistory());
        hm.remove(3);
        Assertions.assertEquals(Collections.EMPTY_LIST, hm.getHistory());
    }

}
