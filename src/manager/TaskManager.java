package manager;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskType;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    ArrayList<Task> getAllTasks(TaskType type);

    void removeTasks();

    void removeEpics();

    void removeSubtasks();


    Task getTask(int taskId);

    void createTask(Task task);

    Task removeById(int id);

    void updateTask(Task task);

    ArrayList<SubTask> getEpicSubTask(Epic epic);

    List<Task> getHistory();
}
