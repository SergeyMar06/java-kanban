package ru.common.model;

import ru.common.enums.Status;
import ru.common.enums.TaskType;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;
    public static final TaskType TASK_TYPE = TaskType.EPIC;

    public Epic(String title, String description) {
        super(title, description, Status.NEW);
        subtaskIds = new ArrayList<>();
    }

    public Epic(Epic other) {
        super(other.getTitle(), other.getDescription(), other.getStatus());
        setId(other.getId());
        this.subtaskIds = new ArrayList<>(other.getSubtaskIds());
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskId(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    @Override
    public TaskType getTaskType() {
        return TASK_TYPE;
    }

    @Override
    public String toString() {
        return "ru.common.model.Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", subtaskIds=" + subtaskIds +
                '}';
    }
}
