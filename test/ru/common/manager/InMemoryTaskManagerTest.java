package ru.common.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.common.enums.Status;
import ru.common.interfaces.TaskManager;
import ru.common.model.Epic;
import ru.common.model.Subtask;
import ru.common.model.Task;

import java.util.ArrayList;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    public void initializationTaskManager() {
        this.taskManager = Managers.getDefault();
    }

    @Test
    public void shouldAddTaskSubtaskAndEpicSuccessfully() {

        // для epic
        Epic epic = new Epic("epic1", "description1");
        int epicId = taskManager.createEpic(epic);

        Epic saveEpic = taskManager.getEpicById(epicId);

        Assertions.assertNotNull(saveEpic);
        Assertions.assertTrue(taskManager.getEpics().contains(saveEpic));

        // для subtask
        Subtask subtask = new Subtask("subtask1", "description1", Status.NEW, epicId);
        int subtaskId = taskManager.createSubtask(subtask);

        Subtask saveSubtask = taskManager.getSubtaskById(subtaskId);

        Assertions.assertNotNull(saveSubtask);
        Assertions.assertTrue(taskManager.getSubtasks().contains(saveSubtask));

        // для task
        Task task = new Task("task1", "description1", Status.NEW);
        int taskId = taskManager.createTask(task);

        Task saveTask = taskManager.getTaskById(taskId);

        Assertions.assertNotNull(saveTask);
        Assertions.assertTrue(taskManager.getTasks().contains(saveTask));
    }

    @Test
    public void shouldReturnTaskSubtaskAndEpicById() {

        // для epic
        Epic epic = new Epic("epic1", "description1");
        int epicId = taskManager.createEpic(epic);

        Epic saveEpic = taskManager.getEpicById(epicId);

        Assertions.assertNotNull(saveEpic);

        // для subtask
        Subtask subtask = new Subtask("subtask1", "description1", Status.NEW, epicId);
        int subtaskId = taskManager.createSubtask(subtask);

        Subtask saveSubtask = taskManager.getSubtaskById(subtaskId);

        Assertions.assertNotNull(saveSubtask);

        // для task
        Task task = new Task("task1", "description1", Status.NEW);
        int taskId = taskManager.createTask(task);

        Task saveTask = taskManager.getTaskById(taskId);

        Assertions.assertNotNull(saveTask);
    }

    @Test
    public void shouldRemainUnchangedWhenAddTaskSubtaskAndEpicInManager() {
        /* проверяем только неизменные поля (id задаётся в менеджере,
        поэтому его не проверяем)
         */

        // для epic
        Epic epic = new Epic("epic1", "description1");
        int epicId = taskManager.createEpic(epic);

        Epic saveEpic = taskManager.getEpicById(epicId);

        Assertions.assertEquals(epic.getTitle(), saveEpic.getTitle());
        Assertions.assertEquals(epic.getDescription(), saveEpic.getDescription());
        Assertions.assertEquals(epic.getStatus(), saveEpic.getStatus());
        Assertions.assertEquals(epic.getSubtaskIds(), saveEpic.getSubtaskIds());

        // для subtask
        Subtask subtask = new Subtask("subtask1", "description1", Status.NEW, epicId);
        int subtaskId = taskManager.createSubtask(subtask);

        Subtask saveSubtask = taskManager.getSubtaskById(subtaskId);

        Assertions.assertEquals(subtask.getTitle(), saveSubtask.getTitle());
        Assertions.assertEquals(subtask.getDescription(), saveSubtask.getDescription());
        Assertions.assertEquals(subtask.getStatus(), saveSubtask.getStatus());
        Assertions.assertEquals(subtask.getEpicId(), saveSubtask.getEpicId());

        // для task
        Task task = new Task("task1", "description1", Status.NEW);
        int taskId = taskManager.createTask(task);

        Task saveTask = taskManager.getTaskById(taskId);

        Assertions.assertEquals(task.getTitle(), saveTask.getTitle());
        Assertions.assertEquals(task.getDescription(), saveTask.getDescription());
        Assertions.assertEquals(task.getStatus(), saveTask.getStatus());
    }

    @Test
    public void shouldDeleteItCorrectTaskSubtaskAndEpic() {

        Epic epic = new Epic("epic1", "description1");
        taskManager.createEpic(epic);

        Task task = new Task("task1", "description2", Status.NEW);
        taskManager.createTask(task);

        Subtask subtask = new Subtask("subtask1", "description3", Status.NEW, epic.getId());
        taskManager.createSubtask(subtask);

        Assertions.assertTrue(taskManager.getTasks().contains(task));
        Assertions.assertTrue(taskManager.getSubtasks().contains(subtask));
        Assertions.assertTrue(taskManager.getEpics().contains(epic));

        taskManager.removeTask(task.getId());
        taskManager.removeSubtask(subtask.getId());
        taskManager.removeEpic(epic.getId());

        Assertions.assertTrue(taskManager.getTasks().isEmpty());
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
    }

    @Test
    public void shouldReturnCorrectListSubtaskByEpicId() {

        Epic epic = new Epic("epic1", "description1");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("subtask1", "description2", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("subtask2", "description3", Status.DONE, epic.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(taskManager.getSubtaskById(subtask1.getId()));
        subtasks.add(taskManager.getSubtaskById(subtask2.getId()));

        Assertions.assertEquals(subtasks, taskManager.getSubtaskByEpicId(epic.getId()));
    }
}