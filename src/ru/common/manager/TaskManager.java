package ru.common.manager;

import ru.common.model.Epic;
import ru.common.enums.Status;
import ru.common.model.Subtask;
import ru.common.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;

    public TaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        this.id = 1;
    }

    private void incrementId() {
        id++;
    }

    public int createTask(Task task) {
        task.setId(id);
        tasks.put(id, task);
        incrementId();

        return task.getId();
    }

    public int createSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            System.out.println("Вы передали несуществующий эпик!");
            System.out.println("Создание невозможно!");
            return 0;
        }

        subtask.setId(id);
        subtasks.put(id, subtask);
        getEpicById(subtask.getEpicId()).getSubtaskIds().add(getId());
        checkTheSubtasksInEpic(subtask.getEpicId());
        incrementId();

        return subtask.getId();
    }

    public int createEpic(Epic epic) {
        epic.setId(id);
        epics.put(id, epic);
        checkTheSubtasksInEpic(id);
        incrementId();

        return epic.getId();
    }

    public void updateTask(int taskId, Task newTask) {
        tasks.put(taskId, newTask);
    }

    public void updateSubtask(int subtaskId, Subtask newSubtask) {
        subtasks.put(subtaskId, newSubtask);
        checkTheSubtasksInEpic(newSubtask.getEpicId());
    }

    public void updateEpic(int epicId, Epic newEpic) {
        Epic epic = getEpicById(epicId);
        if (epic != null) {
            epic.setTitle(newEpic.getTitle());
            epic.setDescription(newEpic.getDescription());
        }
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeSubtask(int id) {
        getEpicById(getSubtaskById(id).getEpicId()).getSubtaskIds().remove((Integer) id);
        checkTheSubtasksInEpic(getSubtaskById(id).getEpicId());
        subtasks.remove(id);
    }

    public void removeEpic(int id) {
        for (Integer el : getEpicById(id).getSubtaskIds()) {
            subtasks.remove(el);
        }

        epics.remove(id);
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void checkTheSubtasksInEpic(int epicId) {
        int countDONE = 0;
        int countNEW = 0;

        for (Integer subtaskId : getEpicById(epicId).getSubtaskIds()) {
            if (Status.DONE.equals(getSubtaskById(subtaskId).getStatus())) {
                countDONE++;
            }
            if (Status.NEW.equals(getSubtaskById(subtaskId).getStatus())) {
                countNEW++;
            }
        }

        if (countDONE == getEpicById(epicId).getSubtaskIds().size()
                & !getEpicById(epicId).getSubtaskIds().isEmpty()) {
            getEpicById(epicId).setStatus(Status.DONE);
        } else if (countNEW == getEpicById(epicId).getSubtaskIds().size()
                | getEpicById(epicId).getSubtaskIds().isEmpty()) {
            getEpicById(epicId).setStatus(Status.NEW);
        } else {
            getEpicById(epicId).setStatus(Status.IN_PROGRESS);
        }
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public int getId() {
        return id;
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllSubtasks() {
        subtasks.clear();

        for (Epic epic : getEpics()) {
            epic.setStatus(Status.NEW);
            epic.getSubtaskIds().clear();
        }
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }
}
