package ru.common.model;

import ru.common.enums.Status;
import ru.common.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicId;
    public static final TaskType TASK_TYPE = TaskType.SUBTASK;

    public Subtask(String title, String description, Status status, int epicId, Duration duration, LocalDateTime startTime) {
        super(title, description, status, duration, startTime);
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
