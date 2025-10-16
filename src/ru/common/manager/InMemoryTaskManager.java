package ru.common.manager;

import ru.common.interfaces.HistoryManager;
import ru.common.interfaces.TaskManager;
import ru.common.model.Epic;
import ru.common.enums.Status;
import ru.common.model.Subtask;
import ru.common.model.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Epic> epics;
    private final HistoryManager historyManager;
    private final TreeSet<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        this.id = 1;
        historyManager = Managers.getDefaultHistory();
        prioritizedTasks = new TreeSet<>(new TaskComparator());
    }

    static class TaskComparator implements Comparator<Task> {
        @Override
        public int compare(Task t1, Task t2) {
            if (t1.getStartTime() == null && t2.getStartTime() == null) {
                return 0;
            }
            if (t1.getStartTime() == null) {
                return 1; // null идёт в конец
            }
            if (t2.getStartTime() == null) {
                return -1; // null идёт в конец
            }
            return t1.getStartTime().compareTo(t2.getStartTime());
        }
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private boolean isIntersect(Task t1, Task t2) {
        if (t1.getStartTime() == null || t2.getStartTime() == null) {
            return false; // задачи без времени не пересекаются
        }
        return t1.getStartTime().isBefore(t2.getEndTime()) &&
                t2.getStartTime().isBefore(t1.getEndTime());
    }

    private boolean isIntersectWithAny(Task task) {
        return prioritizedTasks.stream()
                .anyMatch(t -> t.getId() != task.getId() && isIntersect(task, t));
    }

    private void incrementId() {
        id++;
    }

    @Override
    public int createTask(Task task) {
        task.setId(id);

        if (task.getStartTime() != null && isIntersectWithAny(task)) {
            throw new IllegalArgumentException("Задача пересекается с другой задачей");
        }

        tasks.put(id, task);
        incrementId();

        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }

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

        if (subtask.getStartTime() != null && isIntersectWithAny(subtask)) {
            throw new IllegalArgumentException("Задача пересекается с другой задачей");
        }
        subtasks.put(id, subtask);
        epics.get(subtask.getEpicId()).getSubtaskIds().add(getId());
        checkTheSubtasksInEpic(subtask.getEpicId());
        incrementId();

        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }

        return subtask.getId();
    }

    @Override
    public int createEpic(Epic epic) {
        epic.setId(id);
        epics.put(id, epic);
        checkTheSubtasksInEpic(id);
        incrementId();

        if (epic.getStartTime() != null) {
            prioritizedTasks.add(epic);
        }

        return epic.getId();
    }

    @Override
    public void updateTask(int taskId, Task newTask) {
        Task oldTask = tasks.get(taskId);

        if (oldTask != null && oldTask.getStartTime() != null) {
            prioritizedTasks.remove(oldTask);
        }

        newTask.setId(taskId);

        if (newTask.getStartTime() != null && isIntersectWithAny(newTask)) {
            throw new IllegalArgumentException("Задача пересекается с другой задачей");
        }

        tasks.put(taskId, newTask);

        if (newTask.getStartTime() != null) {
            prioritizedTasks.add(newTask);
        }
    }

    @Override
    public void updateSubtask(int subtaskId, Subtask newSubtask) {
        Subtask oldSubtask = subtasks.get(subtaskId);
        if (oldSubtask != null && oldSubtask.getStartTime() != null) {
            prioritizedTasks.remove(oldSubtask);
        }

        newSubtask.setId(subtaskId);

        if (newSubtask.getStartTime() != null && isIntersectWithAny(newSubtask)) {
            throw new IllegalArgumentException("Задача пересекается с другой задачей");
        }

        subtasks.put(subtaskId, newSubtask);

        if (newSubtask.getStartTime() != null) {
            prioritizedTasks.add(newSubtask);
        }

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
        Task removedTask = tasks.remove(id);
        historyManager.remove(id);

        if (removedTask != null && removedTask.getStartTime() != null) {
            prioritizedTasks.remove(removedTask);
        }
    }

    @Override
    public void removeSubtask(int id) {
        getEpicById(getSubtaskById(id).getEpicId()).getSubtaskIds().remove((Integer) id);
        checkTheSubtasksInEpic(getSubtaskById(id).getEpicId());
        subtasks.remove(id);
        historyManager.remove(id);

        if (subtasks.get(id) != null && subtasks.get(id).getStartTime() != null) {
            prioritizedTasks.remove(subtasks.get(id));
        }
    }

    @Override
    public void removeEpic(int id) {
        Epic removeEpic = epics.get(id);
        if (removeEpic == null) return;

        // Сначала удаляем все подзадачи
        for (Integer subtaskId : removeEpic.getSubtaskIds()) {
            Subtask removedSubtask = subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
            if (removedSubtask != null && removedSubtask.getStartTime() != null) {
                prioritizedTasks.remove(removedSubtask);
            }
        }

        // Потом удаляем эпик
        epics.remove(id);
        historyManager.remove(id);
        if (removeEpic.getStartTime() != null) {
            prioritizedTasks.remove(removeEpic);
        }
    }


    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
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
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void removeAllTasks() {
        for (Task task : tasks.values()) {
            if (historyManager.getHistory().contains(task)) {
                historyManager.remove(task.getId());
            }
        }

        tasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            if (historyManager.getHistory().contains(subtask)) {
                historyManager.remove(subtask.getId());
            }
        }

        subtasks.clear();

        for (Epic epic : getEpics().values()) {
            epic.setStatus(Status.NEW);
            epic.getSubtaskIds().clear();
        }
    }

    @Override
    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            if (historyManager.getHistory().contains(epic)) {
                historyManager.remove(epic.getId());
            }
        }

        epics.clear();
        subtasks.clear();
    }

    @Override
    public ArrayList<Subtask> getSubtaskByEpicId(int epicId) {
        return getEpicById(epicId).getSubtaskIds().stream()
                .map(id -> getSubtaskById(id))
                .collect(Collectors.toCollection(ArrayList::new));
    }


    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public void setTasks(HashMap<Integer, Task> tasks) {
        this.tasks = tasks;
        for (Task task : tasks.values()) {
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
        }
    }

    public void setSubtasks(HashMap<Integer, Subtask> subtasks) {
        this.subtasks = subtasks;
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getStartTime() != null) {
                prioritizedTasks.add(subtask);
            }
        }
    }

    public void setEpics(HashMap<Integer, Epic> epics) {
        this.epics = epics;
        for (Epic epic : epics.values()) {
            if (epic.getStartTime() != null) {
                prioritizedTasks.add(epic);
            }
        }
    }
}