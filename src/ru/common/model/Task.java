package ru.common.model;

import ru.common.enums.Status;
import ru.common.enums.TaskType;

import java.util.Objects;

public class Task {
    private int id;
    private String title;
    private String description;
    private Status status;
    public static final TaskType TASK_TYPE = TaskType.TASK;

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.description;
        this.status = task.getStatus();
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getTaskType() {
        return TASK_TYPE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ru.common.model.Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
