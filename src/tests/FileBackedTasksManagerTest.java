package tests;

import manager.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.TaskType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static manager.FileBackedTasksManager.loadFromFile;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void beforeEach() throws IOException {
        taskManager = new FileBackedTasksManager(Paths.get("history.csv"));
        taskManager.createTask(task);
        taskManager.createTask(epic);
        taskManager.createTask(subTask);
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(Paths.get("history.csv"));
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void saveAndGetFromFile() throws IOException {
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(3);

        File file = new File("history.csv");
        FileBackedTasksManager gm = loadFromFile(file);

        String history1 = gm.getHistory().toString();
        String history2 = taskManager.getHistory().toString();
        Assertions.assertEquals(history1, history2, "не верно восстановилась из файла");

        String subTask1 = gm.getAllTasks(TaskType.SUBTASK).toString();
        String subTask2 = taskManager.getAllTasks(TaskType.SUBTASK).toString();
        Assertions.assertEquals(subTask1, subTask2);
    }

    @Test
    public void emptyHistoryFromFile() throws IOException {

        File file = new File("history.csv");
        FileBackedTasksManager gm = loadFromFile(file);

        String history1 = gm.getHistory().toString();
        String history2 = taskManager.getHistory().toString();
        Assertions.assertEquals(history1, history2, "история не пустая");
    }
}
