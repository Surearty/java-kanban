package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private final String title;
    private final String description;
    private TaskStatus status;
    private final TaskType type;
    private int id;
    private Duration duration;
    private LocalDateTime startTime;
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Task(TaskType type, String title, TaskStatus status, String description) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type;
    }

    public Task(TaskType type, String title, TaskStatus status, String description, String startTime) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type;
        setStartTime(startTime);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public TaskType getType() {
        return type;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration == null ? 0 : duration.toMinutes();
    }

    public void setStartTime(String time) {
        if (time.equals("null"))
            this.startTime = null;
        else
            this.startTime = LocalDateTime.parse(time, formatter);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getStartTimeInString(LocalDateTime time) {
        return time == null ? "null" : time.format(formatter);
    }

    public LocalDateTime getEndTime() {
        if (this.type == TaskType.EPIC) {
            Epic epic = (Epic) this;
            return epic.getEpicEndTime();
        }
        return startTime != null ? startTime.plusMinutes(duration.toMinutes()) : null;
    }

    @Override
    public String toString() {
        return getId() + "," + getType() + "," +
                getTitle() + "," + getStatus() + "," +
                getDescription() + "," + getStartTimeInString(startTime) + "," +
                getDuration() + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(description, task.description) && Objects.equals(type, task.type) &&
                status == task.status && Objects.equals(startTime, task.startTime) &&
                Objects.equals(duration, task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, status, id, startTime, duration);
    }
}
