package tests;

import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = (InMemoryTaskManager) Managers.getInMemoryTaskManager();
        taskManager.createTask(task);
        taskManager.createTask(epic);
        taskManager.createTask(subTask);
    }
}
