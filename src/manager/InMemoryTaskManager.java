package manager;

import task.*;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final LocalDateTime startYear = LocalDateTime.of(2023, 1, 1, 0, 0);
    private int taskId;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private final HistoryManager historyManager;
    private final TreeSet<Task> sortedTaskNoTime = new TreeSet<>(Comparator.comparing(Task::getId));
    private final TreeSet<Task> sortedTaskWithTime = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private final HashMap<LocalDateTime, Boolean> timeGrid;

    public InMemoryTaskManager() {
        this.taskId = 0;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
        this.timeGrid = new HashMap<>();
        fillTimeGrid();
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private int generateId(Task task) {
        if (task == null || task.getId() == 0)
            return generateId();
        return task.getId();
    }

    private int generateId() {
        return ++taskId;
    }

    @Override
    public ArrayList<Task> getAllTasks(TaskType type) {
        if (type == TaskType.EPIC) {
            return new ArrayList<>(epics.values());
        } else if (type == TaskType.SUBTASK) {
            return new ArrayList<>(subTasks.values());
        }
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void removeTasks() {
        tasks.clear();
    }

    @Override
    public void removeEpics() {
        epics.clear();
        removeSubtasks();
    }

    @Override
    public void removeSubtasks() {
        subTasks.clear();
    }

    @Override
    public Task getTask(int taskId) {
        Task task = null;
        if (tasks.containsKey(taskId)) {
            task = tasks.get(taskId);
            historyManager.add(task);
        } else if (epics.containsKey(taskId)) {
            task = epics.get(taskId);
            historyManager.add(task);
        } else if (subTasks.containsKey(taskId)) {
            task = subTasks.get(taskId);
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public void createTask(Task task) {
        if (task != null) {
            int taskId = generateId(task);
            task.setId(taskId);
            addNewPrioritizedTask(task);
            switch (task.getType()) {
                case TASK:
                    tasks.put(taskId, task);
                    break;
                case EPIC:
                    task.setStatus(checkStatus((Epic) task));
                    epics.put(taskId, (Epic) task);
                    break;
                case SUBTASK:
                    subTasks.put(taskId, (SubTask) task);
                    SubTask sub = (SubTask) task;
                    epics.get(sub.getEpicId()).putSubTask(sub);
                    Epic epic = epics.get(sub.getEpicId());
                    epic.calculateEpicTime();
                    epics.get(sub.getEpicId()).setStatus(checkStatus(epic));
                    break;
            }
        }
    }

    @Override
    public Task removeById(int id) {
        Task task = null;
        if (tasks.containsKey(id)) {
            task = tasks.remove(id);
        } else if (epics.containsKey(id)) {
            task = epics.remove(id);
            Epic ep = (Epic) task;
            removeEpicsSubTask(ep.getSubTasks());
        } else if (subTasks.containsKey(id)) {
            task = subTasks.remove(id);
            SubTask sub = (SubTask) task;
            epics.get(sub.getEpicId()).removeSubtask(sub);
        }
        return task;
    }

    private void removeEpicsSubTask(ArrayList<SubTask> subTasks) {
        for (var sub : subTasks) {
            this.subTasks.remove(sub.getId());
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task == null) {
            return;
        }
        Task removed = this.removeById(task.getId());
        if (removed != null) {
            this.createTask(task);
        }
    }

    @Override
    public ArrayList<SubTask> getEpicSubTask(Epic epic) {
        return epic.getSubTasks();
    }

    private TaskStatus checkStatus(Epic epic) {
        ArrayList<SubTask> sub = getEpicSubTask(epic);
        if (sub.size() == 0) {
            return TaskStatus.NEW;
        }
        TaskStatus status = sub.get(0).getStatus();
        for (var task : sub) {
            if (task.getStatus() != status) {
                return TaskStatus.IN_PROGRESS;
            }
        }
        return status;
    }

    public void addToHistory(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
        } else if (subTasks.containsKey(id)) {
            historyManager.add(subTasks.get(id));
        } else if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
        }
    }

    public List<Task> getPrioritizedTasks() {
        List<Task> allSortedTask = new ArrayList<>(sortedTaskWithTime);
        allSortedTask.addAll(sortedTaskNoTime);
        return allSortedTask;
    }

    private void fillTimeGrid() {
        LocalDateTime start = startYear;
        while (start.isBefore(startYear.plusYears(1))) {
            timeGrid.put(start, true);
            start = start.plusMinutes(15);
        }
    }

    private void addNewPrioritizedTask(Task task) {
        if (task.getStartTime() == null)
            sortedTaskNoTime.add(task);
        else if (checkTimeIntersections(task))
            sortedTaskWithTime.add(task);
    }

    public boolean checkTimeIntersections(Task task) {
        long taskDuration = task.getDuration();
        taskDuration = taskDuration / 15 * 15;
        LocalDateTime startTime = task.getStartTime();
        int min = startTime.getMinute() / 15 * 15;
        startTime = startTime.withMinute(min);
        if (timeGrid.get(startTime)) {
            for (int i = 0; i < taskDuration; i += 15) {
                if (!timeGrid.get(startTime.plusMinutes(i)))
                    return false;
            }
            for (int i = 0; i < taskDuration; i += 15) {
                timeGrid.put(startTime.plusMinutes(i), false);
            }
        }
        return true;
    }
}
