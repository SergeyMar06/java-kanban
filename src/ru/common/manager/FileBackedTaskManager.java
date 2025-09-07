package ru.common.manager;

import ru.common.enums.Status;
import ru.common.enums.TaskType;
import ru.common.errors.ManagerSaveException;
import ru.common.errors.TaskTypeException;
import ru.common.interfaces.TaskManager;
import ru.common.model.Epic;
import ru.common.model.Subtask;
import ru.common.model.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private String url;

    public FileBackedTaskManager(String url) {
        this.url = url;
    }

    public void save() {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(url))) {
            for (Task task : getTasks().values()) {
                fileWriter.write(toString(task));
                fileWriter.newLine();
            }

            for (Subtask subtask : getSubtasks().values()) {
                fileWriter.write(toString(subtask));
                fileWriter.newLine();
            }

            for (Epic epic : getEpics().values()) {
                fileWriter.write(toString(epic));
                fileWriter.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить данные в файл", e);
        }
    }

    public String toString(Task task) {
        String str = task.getId() + ","
                + task.getTaskType() + ","
                + task.getTitle() + ","
                + task.getStatus()
                + "," + task.getDescription();

        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            str += "," + subtask.getEpicId();
        }

        return str;
    }

    public Task fromString(String value) throws TaskTypeException {
        String[] arrayStr = value.trim().split(",");

        int id = Integer.parseInt(arrayStr[0]);
        TaskType type = TaskType.valueOf(arrayStr[1]);
        String title = arrayStr[2];
        Status status = Status.valueOf(arrayStr[3]);
        String description = arrayStr[4];

        switch (type) {
            case TASK:
                Task task = new Task(title, description, status);
                task.setId(id);
                return task;
            case EPIC:
                Epic epic = new Epic(title, description);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
            case SUBTASK:
                int epicId = Integer.parseInt(arrayStr[5]);
                Subtask subtask = new Subtask(title, description, status, epicId);
                subtask.setId(id);
                return subtask;
            default:
                throw new TaskTypeException("Неизвестный тип задачи");
        }
    }

    @Override
    public int createTask(Task task) {
        int id = super.createTask(task);
        System.out.println("Tasks in memory: " + getTasks().size());
        save();

        return id;
    }

    @Override
    public int createSubtask(Subtask subtask) {
        int id = super.createSubtask(subtask);
        save();

        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = super.createEpic(epic);
        save();

        return id;
    }

    @Override
    public void updateTask(int taskId, Task newTask) {
        super.updateTask(taskId, newTask);
        save();
    }

    @Override
    public void updateSubtask(int subtaskId, Subtask newSubtask) {
        super.updateSubtask(subtaskId, newSubtask);
        save();
    }

    @Override
    public void updateEpic(int epicId, Epic newEpic) {
        super.updateEpic(epicId, newEpic);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void checkTheSubtasksInEpic(int epicId) {
        super.checkTheSubtasksInEpic(epicId);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }
}

