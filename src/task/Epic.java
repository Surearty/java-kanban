package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private final ArrayList<SubTask> subTasks;
    private LocalDateTime endTime;

    public Epic(TaskType type, String title, TaskStatus status, String description) {
        super(type, title, status, description);
        subTasks = new ArrayList<>();
    }

    public Epic(TaskType type, String title, TaskStatus status, String description, String startTime) {
        super(type, title, status, description, startTime);
        subTasks = new ArrayList<>();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void putSubTask(SubTask task) {
        subTasks.add(task);
        calculateEpicTime();
    }

    public void removeSubtask(SubTask subTask) {
        subTasks.remove(subTask);
        calculateEpicTime();
    }

    public LocalDateTime getEpicEndTime() {
        calculateEpicTime();
        return this.endTime;
    }

    public void calculateEpicTime() {
        Long totalDuration = subTasks.stream()
                .filter(Objects::nonNull)
                .mapToLong(Task::getDuration)
                .sum();
        LocalDateTime min = subTasks.stream()
                .filter(Objects::nonNull)
                .map(Task::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo).orElse(null);
        this.endTime = subTasks.stream()
                .filter(Objects::nonNull)
                .map(Task::getStartTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo).orElse(null);
        this.setDuration(Duration.ofMinutes(totalDuration));
        this.setStartTime(min == null ? "null" : min.format(formatter));
    }
}
