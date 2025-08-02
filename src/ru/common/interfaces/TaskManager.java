package ru.common.interfaces;

import ru.common.model.Epic;
import ru.common.model.Subtask;
import ru.common.model.Task;

import java.util.ArrayList;

public interface TaskManager {
    int createTask(Task task);

    int createSubtask(Subtask subtask);

    int createEpic(Epic epic);

    void updateTask(int taskId, Task newTask);

    void updateSubtask(int subtaskId, Subtask newSubtask);

    void updateEpic(int epicId, Epic newEpic);

    void removeTask(int id);

    void removeSubtask(int id);

    void removeEpic(int id);

    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    void checkTheSubtasksInEpic(int epicId);

    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    int getId();

    void removeAllTasks();

    void removeAllSubtasks();

    void removeAllEpics();

    ArrayList<Subtask> getSubtaskByEpicId(int epicId);

    public HistoryManager getHistoryManager();
}
