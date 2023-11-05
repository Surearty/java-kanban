package manager;

import task.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static final String headerCSV = "id,type,name,status,description,epic,startTime,duration\n";
    private final Path fileCSV;

    public FileBackedTasksManager(Path filePath) throws IOException {
        super();
        fileCSV = filePath;
        if (!(Files.exists(fileCSV)))
            Files.createFile(fileCSV);
    }

    public void save() {
        try {
            Files.write(fileCSV, headerCSV.getBytes());
            List<Task> ls = getAllTasks(TaskType.TASK);
            ls.addAll(getAllTasks(TaskType.EPIC));
            ls.addAll(getAllTasks(TaskType.SUBTASK));
            for (var task : ls)
                Files.write(fileCSV, task.toString().getBytes(), StandardOpenOption.APPEND);
            Files.write(fileCSV, "\n".getBytes(), StandardOpenOption.APPEND);
            String history = historyToString(getHistoryManager());
            Files.write(fileCSV, history.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось найти файл или записать данные");
        }
    }

    private Task taskFromString(String value) {
        String[] params = value.split(",");
        if (params[1].equals("EPIC")) {
            Epic epic = new Epic(TaskType.valueOf(params[1]), params[2], TaskStatus.valueOf(params[3]), params[4]);
            epic.setId(Integer.parseInt(params[0]));
            epic.setStartTime(params[5]);
            epic.setDuration(Duration.ofMinutes(Long.parseLong(params[6])));
            return epic;
        } else if (params[1].equals("SUBTASK")) {
            SubTask subtask = new SubTask(TaskType.valueOf(params[1]), params[2], TaskStatus.valueOf(params[3]), params[4], Integer.parseInt(params[5]));
            subtask.setId(Integer.parseInt(params[0]));
            subtask.setStartTime(params[6]);
            subtask.setDuration(Duration.ofMinutes(Long.parseLong(params[7])));
            return subtask;
        }
        Task task = new Task(TaskType.valueOf(params[1]), params[2], TaskStatus.valueOf(params[3]), params[4]);
        task.setId(Integer.parseInt(params[0]));
        task.setStartTime(params[5]);
        task.setDuration(Duration.ofMinutes(Long.parseLong(params[6])));
        return task;
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        if(value != null && !value.isBlank()) {
            List<String> historyId = List.of(value.split(","));
            for (var id : historyId) {
                history.add(Integer.parseInt(id));
            }
        }
        return history;
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        List<String> historyID = new ArrayList<>();
        for (Task task : history) {
            historyID.add(Integer.toString(task.getId()));
        }
        return historyID.isEmpty() ? "0" : String.join(",", historyID);
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        FileBackedTasksManager backendTaskManager = new FileBackedTasksManager(file.toPath());
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        for (int i = 1; i < lines.size() - 2; i++) {
            Task newTask = backendTaskManager.taskFromString(lines.get(i));
            backendTaskManager.createTask(newTask);
        }
        List<Integer> history = historyFromString(lines.get(lines.size() - 1));
        for (var id : history) {
            backendTaskManager.addToHistory(id);
        }
        return backendTaskManager;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public Task getTask(int taskId) {
        Task task = super.getTask(taskId);
        save();
        return task;
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void addToHistory(int id) {
        super.addToHistory(id);
        save();
    }

    public static void main(String[] args) throws IOException {
        FileBackedTasksManager tm = new FileBackedTasksManager(Paths.get("history.csv"));
        Task task1 = new Task(TaskType.TASK, "Задача_1", TaskStatus.NEW, "Description");
        Task task2 = new Task(TaskType.TASK, "Задача_2", TaskStatus.NEW, "Description");
        Epic epic1 = new Epic(TaskType.EPIC, "Эпик_1", TaskStatus.NEW, "Description");
        Epic epic2 = new Epic(TaskType.EPIC, "Эпик_2", TaskStatus.NEW, "Description");

        SubTask sub1 = new SubTask(TaskType.SUBTASK, "Подзадача1_Эпик_1", TaskStatus.DONE, "Description", 3);
        SubTask sub2 = new SubTask(TaskType.SUBTASK, "Подзадача2_Эпик_1", TaskStatus.NEW, "Description", 3);
        SubTask sub3 = new SubTask(TaskType.SUBTASK, "Подзадача3_Эпик_1", TaskStatus.IN_PROGRESS, "Description", 4);

        sub1.setStartTime("2023-02-09 22:30");
        sub2.setStartTime("2023-02-09 23:30");
        sub3.setStartTime("2023-02-09 23:30");


        tm.createTask(task1);
        tm.createTask(task2);
        tm.createTask(epic1);
        tm.createTask(epic2);
        tm.createTask(sub1);
        tm.createTask(sub2);
        tm.createTask(sub3);

        tm.getTask(1);
        tm.getTask(3);
        tm.getTask(5);

        System.out.println(tm.getTask(3).getEndTime());
        System.out.println(tm.getTask(3).getStartTime());
        System.out.println(tm.getTask(3).getDuration());
//        File file = new File("history.csv");
//        FileBackedTasksManager gm = loadFromFile(file);
//
//        String subTask1 = gm.getAllTasks(TaskType.SUBTASK).toString();
//        String subTask2 = tm.getAllTasks(TaskType.SUBTASK).toString();
//        System.out.println(subTask1);
//        System.out.println(subTask2);
////
//        System.out.println(subTask1.equals(subTask2));
//
//        String history1 = gm.getHistory().toString();
//        String history2 = tm.getHistory().toString();
//        System.out.println(history1);
//        System.out.println(history2);
//        System.out.println(history1.equals(history2));
    }
}
