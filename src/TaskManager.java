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

    public void generateId() {
        id++;
    }

    public void createTask(Task task) {
        task.setId(id);
        tasks.put(id, task);
        generateId();
    }

    public void createSubtask(Subtask subtask) {
        subtask.setId(id);
        subtasks.put(id, subtask);
        getEpicById(subtask.getEpicId()).getSubtaskIds().add(getId());
        checkTheSubtasksInEpic(subtask.getEpicId());
        generateId();
    }

    public void createEpic(Epic epic) {
        epic.setId(id);
        epics.put(id, epic);
        checkTheSubtasksInEpic(id);
        generateId();
    }

    public void updateTask(int taskId, Task newTask) {
        tasks.put(taskId, newTask);
    }

    public void updateSubtask(int subtaskId, Subtask newSubtask) {
        subtasks.put(subtaskId, newSubtask);
        checkTheSubtasksInEpic(newSubtask.getEpicId());
    }

    public void updateEpic(int epicId, Epic newEpic) {
        epics.put(epicId, newEpic);
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeSubtask(int id) {
        subtasks.remove(id);
    }

    public void removeEpic(int id) {
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

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public int getId() {
        return id;
    }
}
