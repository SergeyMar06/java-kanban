package ru.common.manager;

import ru.common.interfaces.HistoryManager;
import ru.common.model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> history;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if (getHistory().size() < 10) {
            history.add(task);
        } else {
            getHistory().remove(0);
            history.add(new Task(task));
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}
