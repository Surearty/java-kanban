package tests;

import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;
    Task task = new Task(TaskType.TASK, "Задача_1", TaskStatus.NEW, "Description");
    Epic epic = new Epic(TaskType.EPIC, "Эпик_1", TaskStatus.NEW, "Description");
    SubTask subTask = new SubTask(TaskType.SUBTASK, "Подзадача2_Эпик_1", TaskStatus.NEW, "Description", 2);

    @Test
    public void createTaskTest() {
        Assertions.assertNotNull(taskManager.getTask(1), "Задача = null");
        Assertions.assertNotNull(taskManager.getTask(2), "Эпик = null");
        Assertions.assertNotNull(taskManager.getTask(3), "Подзадача = null");
        Assertions.assertEquals(1, taskManager.getAllTasks(TaskType.TASK).size(), "Списки задач не равны");
        Assertions.assertEquals(1, taskManager.getAllTasks(TaskType.EPIC).size(), "Списки эпиков не равны");
        Assertions.assertEquals(1, taskManager.getAllTasks(TaskType.SUBTASK).size(), "Списки подзадач не равны");
        Assertions.assertEquals(epic.getId(), subTask.getEpicId(), "не совпал номер эпика у подзадачи");
        taskManager.createTask(null);
        Assertions.assertEquals(1, taskManager.getAllTasks(TaskType.TASK).size(), "Списки задач не равны");
    }

    @Test
    public void updateTaskTest() {
        task.setStatus(TaskStatus.DONE);
        subTask.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task);
        taskManager.updateTask(subTask);
        Assertions.assertEquals(TaskStatus.DONE, taskManager.getTask(1).getStatus(), "Не изменился статус");
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, taskManager.getTask(3).getStatus(), "У подзадачи не изменился статус");
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, taskManager.getTask(2).getStatus(), "не меняется статус у эпика");
    }

    @Test
    public void removeTasksTest() {
        taskManager.removeTasks();
        taskManager.removeEpics();
        Assertions.assertEquals(Collections.EMPTY_LIST, taskManager.getAllTasks(TaskType.TASK), "Задачи не стерлись");
        Assertions.assertEquals(Collections.EMPTY_LIST, taskManager.getAllTasks(TaskType.EPIC), "Эпики не стерлись");
        Assertions.assertEquals(Collections.EMPTY_LIST, taskManager.getAllTasks(TaskType.SUBTASK), "Субтаски не стерлись после удаления эпиков");
    }


    @Test
    public void removeTaskByIdTest() {
        taskManager.removeById(1);
        taskManager.removeById(2);
        Assertions.assertNull(taskManager.getTask(1), "Не удалилась задача");
        Assertions.assertNull(taskManager.getTask(2), "Не удалился эпик");
        Assertions.assertNull(taskManager.getTask(3), "Подзадача не удалилась после эпика");

        assertDoesNotThrow(() -> {taskManager.removeById(Integer.MAX_VALUE);
                taskManager.getTask(Integer.MAX_VALUE);});
    }

    @Test
    public void getAllTaskTest() {
        List<Task> taskList = new ArrayList<>(List.of(task));
        List<Task> epicList = new ArrayList<>(List.of(epic));
        Assertions.assertEquals(taskList, taskManager.getAllTasks(TaskType.TASK));
        Assertions.assertEquals(epicList, taskManager.getAllTasks(TaskType.EPIC));
    }


    @Test
    public void returnEmptyHistoryTest() {
        Assertions.assertEquals(Collections.EMPTY_LIST, taskManager.getHistory());
        taskManager.getTask(100500);
        Assertions.assertEquals(Collections.EMPTY_LIST, taskManager.getHistory());
    }

    @Test
    public void returnHistoryTest() {
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(3);
        List<Integer> id = taskManager.getHistory().stream().map(Task::getId).collect(Collectors.toList());
        List <Integer> expect = List.of(1,2,3);
        Assertions.assertEquals(expect, id);
        Assertions.assertEquals(3, taskManager.getHistory().size());
    }
}

