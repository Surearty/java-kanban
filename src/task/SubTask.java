package task;

public class SubTask extends Task {
    private final int epicId;

    public SubTask(TaskType type, String title, TaskStatus status, String description, int epicId) {
        super(type, title, status, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return getId() + "," + getType() + "," +
                getTitle() + "," + getStatus() + "," +
                getDescription() + "," + getEpicId() + "," +
                getStartTimeInString(getStartTime()) + "," + getDuration() + "\n";

    }

    public static void main(String[] args) {
        SubTask subTask = new SubTask(TaskType.SUBTASK, "Подзадача3_Эпик_1", TaskStatus.IN_PROGRESS,
                "Description", 3);
        subTask.setStartTime("2016-11-09 10:30");
        System.out.println(subTask);
    }
}

