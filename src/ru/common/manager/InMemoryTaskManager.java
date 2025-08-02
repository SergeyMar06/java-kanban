package ru.common.manager;

import ru.common.interfaces.HistoryManager;
import ru.common.interfaces.TaskManager;
import ru.common.model.Epic;
import ru.common.enums.Status;
import ru.common.model.Subtask;
import ru.common.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        this.id = 1;
        historyManager = Managers.getDefaultHistory();
    }

    private void incrementId() {
        id++;
    }

    @Override
    public int createTask(Task task) {
        task.setId(id);
        tasks.put(id, task);
        incrementId();

        return task.getId();
    }

    @Override
    public int createSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            System.out.println("Вы передали несуществующий эпик!");
            System.out.println("Создание невозможно!");
            return 0;
        }

        subtask.setId(id);
        subtasks.put(id, subtask);
        epics.get(subtask.getEpicId()).getSubtaskIds().add(getId());
        checkTheSubtasksInEpic(subtask.getEpicId());
        incrementId();

        return subtask.getId();
    }

    @Override
    public int createEpic(Epic epic) {
        epic.setId(id);
        epics.put(id, epic);
        checkTheSubtasksInEpic(id);
        incrementId();


        return epic.getId();
    }

    @Override
    public void updateTask(int taskId, Task newTask) {
        newTask.setId(taskId);
        tasks.put(taskId, newTask);
    }

    @Override
    public void updateSubtask(int subtaskId, Subtask newSubtask) {
        newSubtask.setId(subtaskId);
        subtasks.put(subtaskId, newSubtask);
        checkTheSubtasksInEpic(newSubtask.getEpicId());
    }

    @Override
    public void updateEpic(int epicId, Epic newEpic) {
        Epic epic = getEpicById(epicId);
        if (epic != null) {
            epic.setId(epicId);
            epic.setTitle(newEpic.getTitle());
            epic.setDescription(newEpic.getDescription());
        }
    }

    @Override
    public void removeTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeSubtask(int id) {
        getEpicById(getSubtaskById(id).getEpicId()).getSubtaskIds().remove((Integer) id);
        checkTheSubtasksInEpic(getSubtaskById(id).getEpicId());
        subtasks.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        for (Integer el : getEpicById(id).getSubtaskIds()) {
            subtasks.remove(el);
        }

        epics.remove(id);
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void checkTheSubtasksInEpic(int epicId) {
        int countDONE = 0;
        int countNEW = 0;

        for (Integer subtaskId : epics.get(epicId).getSubtaskIds()) {
            if (Status.DONE.equals(subtasks.get(subtaskId).getStatus())) {
                countDONE++;
            }
            if (Status.NEW.equals(subtasks.get(subtaskId).getStatus())) {
                countNEW++;
            }
        }

        if (countDONE == epics.get(epicId).getSubtaskIds().size()
                & !epics.get(epicId).getSubtaskIds().isEmpty()) {
            epics.get(epicId).setStatus(Status.DONE);
        } else if (countNEW == epics.get(epicId).getSubtaskIds().size()
                | epics.get(epicId).getSubtaskIds().isEmpty()) {
            epics.get(epicId).setStatus(Status.NEW);
        } else {
            epics.get(epicId).setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();

        for (Epic epic : getEpics()) {
            epic.setStatus(Status.NEW);
            epic.getSubtaskIds().clear();
        }
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public ArrayList<Subtask> getSubtaskByEpicId(int epicId) {
        ArrayList<Subtask> subtasks = new ArrayList<>();

        for (int subtaskId : getEpicById(epicId).getSubtaskIds()) {
            subtasks.add(getSubtaskById(subtaskId));
        }

        return subtasks;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}