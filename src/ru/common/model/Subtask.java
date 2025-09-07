package ru.common.model;

import ru.common.enums.Status;
import ru.common.enums.TaskType;

public class Subtask extends Task {
    private final int epicId;
    public static final TaskType TASK_TYPE = TaskType.SUBTASK;

    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getTaskType() {
        return TASK_TYPE;
    }

    @Override
    public String toString() {
        return "ru.common.model.Subtask{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", epicId=" + epicId +
                '}';
    }
}
