import ru.common.interfaces.TaskManager;
import ru.common.manager.Managers;
import ru.common.model.Epic;
import ru.common.enums.Status;
import ru.common.model.Subtask;
import ru.common.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

//public class Main {

//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        TaskManager taskManager = Managers.loadFromFile("src/ru/common/files/tasks.txt");
//
//        while (true) {
//            printMenuTask();
//
//            int command = scanner.nextInt();
//            scanner.nextLine();
//
//            if (command == 1) {
//                while (true) {
//                    printMenu();
//
//                    int numberTask = scanner.nextInt();
//                    scanner.nextLine();
//
//                    if (numberTask == 1) {
//                        System.out.println(taskManager.getTasks());
//                    } else if (numberTask == 2) {
//                        taskManager.removeAllTasks();
//                        System.out.println("Задачи успешно удалены.");
//                    } else if (numberTask == 3) {
//                        System.out.println("Введите id задачи: ");
//                        int taskId = scanner.nextInt();
//                        System.out.println("Задача с id = " + taskId + " - " + taskManager.getTaskById(taskId));
//                    } else if (numberTask == 4) {
//                        try {
//                            System.out.println("Введите название задачи: ");
//                            String title = scanner.nextLine();
//                            System.out.println("Введите описание задачи: ");
//                            String description = scanner.nextLine();
//                            System.out.println("Введите статус задачи (NEW, IN_PROGRESS, DONE)");
//                            String status = scanner.nextLine();
//                            System.out.println("Ведите дату и время в формате: {день.месяц.год часы:минуты}");
//                            String dateTime = scanner.nextLine();
//                            LocalDateTime startTime = null;
//                            if (!dateTime.isEmpty()) {
//                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
//                                startTime = LocalDateTime.parse(dateTime, formatter);
//                            }
//                            System.out.println("Введите продолжительность задачи в минутах: ");
//                            int minutes = scanner.nextInt();
//                            scanner.nextLine();
//                            Duration duration = Duration.ofMinutes(minutes);
//                            System.out.println("Задача c id = " + taskManager.createTask(new Task(title, description, Status.valueOf(status), duration, startTime)) + " успешно создана!");
//                        } catch (IllegalArgumentException e) {
//                            System.out.println("Ошибка: " + e.getMessage());
//                        }
//                    } else if (numberTask == 5) {
//                        try {
//                            System.out.println("Введите id задачи: ");
//                            int taskId = scanner.nextInt();
//                            scanner.nextLine();
//                            System.out.println("Введите новое название задачи: ");
//                            String title = scanner.nextLine();
//                            System.out.println("Введите новое описание задачи: ");
//                            String description = scanner.nextLine();
//                            System.out.println("Введите новый статус задачи (NEW, IN_PROGRESS, DONE)");
//                            String status = scanner.nextLine();
//                            System.out.println("Ведите дату и время в формате: {день.месяц.год часы:минуты}");
//                            String dateTime = scanner.nextLine();
//                            LocalDateTime startTime = null;
//                            if (!dateTime.isEmpty()) {
//                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
//                                startTime = LocalDateTime.parse(dateTime, formatter);
//                            }
//                            System.out.println("Введите продолжительность задачи в минутах: ");
//                            int minutes = scanner.nextInt();
//                            scanner.nextLine();
//                            Duration duration = Duration.ofMinutes(minutes);
//                            taskManager.updateTask(taskId, new Task(title, description, Status.valueOf(status), duration, startTime));
//                            System.out.println("Задача успешно обновлена!");
//                        } catch (IllegalArgumentException e) {
//                            System.out.println("Ошибка: " + e.getMessage());
//                        }
//                    } else if (numberTask == 6) {
//                        System.out.println("Введите id задачи: ");
//                        int taskId = scanner.nextInt();
//                        taskManager.removeTask(taskId);
//                    } else if (numberTask == 7) {
//                        break;
//                    }
//                }
//            } else if (command == 2) {
//                while (true) {
//                    printMenu();
//
//                    int numberSubtask = scanner.nextInt();
//                    scanner.nextLine();
//
//                    if (numberSubtask == 1) {
//                        System.out.println(taskManager.getSubtasks());
//                    } else if (numberSubtask == 2) {
//                        taskManager.removeAllSubtasks();
//                        System.out.println("Подзадачи успешно удалены.");
//                    } else if (numberSubtask == 3) {
//                        System.out.println("Введите id подзадачи: ");
//                        int subtaskId = scanner.nextInt();
//                        System.out.println("Подзадача с id = " + subtaskId + " - " + taskManager.getSubtaskById(subtaskId));
//                    } else if (numberSubtask == 4) {
//                        try {
//                            if (taskManager.getEpics().isEmpty()) {
//                                System.out.println("Вы не можете создать подзадачи, пока не будет создан хотя бы один эпик!");
//                            } else {
//                                System.out.println("Введите название подзадачи: ");
//                                String title = scanner.nextLine();
//                                System.out.println("Введите описание подзадачи: ");
//                                String description = scanner.nextLine();
//                                System.out.println("Введите статус подзадачи (NEW, IN_PROGRESS, DONE)");
//                                String status = scanner.nextLine();
//                                System.out.println("Введите id эпика данной подзадачи: ");
//                                int epicId = scanner.nextInt();
//                                scanner.nextLine();
//                                System.out.println("Ведите дату и время в формате: {день.месяц.год часы:минуты}");
//                                String dateTime = scanner.nextLine();
//                                LocalDateTime startTime = null;
//                                if (!dateTime.isEmpty()) {
//                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
//                                    startTime = LocalDateTime.parse(dateTime, formatter);
//                                }
//                                System.out.println("Введите продолжительность задачи в минутах: ");
//                                int minutes = scanner.nextInt();
//                                Duration duration = Duration.ofMinutes(minutes);
//                                int subtaskId = taskManager.createSubtask(new Subtask(title, description, Status.valueOf(status), epicId, duration, startTime));
//                            /* сохраняем id подзадачи в отдельную переменную, так как иначе,
//                             если строчку "taskManager.createSubtask(new Subtask(title, description, Status.valueOf(status), epicId))"
//                             добавить и в условие, и в вывод (sout), то id увеличится на 2
//                             и его нумерация у дальнейших объектов собьётся
//                             */
//                                taskManager.getEpicById(epicId).updateTimes(taskManager.getSubtasks());
//                                if (subtaskId != 0) {
//                                    System.out.println("Подзадача с id = " + subtaskId + " успешно создана!");
//                                }
//                            }
//                        } catch (IllegalArgumentException e) {
//                            System.out.println("Ошибка: " + e.getMessage());
//                        }
//                    } else if (numberSubtask == 5) {
//                        try {
//                            System.out.println("Введите id подзадачи: ");
//                            int subtaskId = scanner.nextInt();
//                            scanner.nextLine();
//                            System.out.println("Введите новое название подзадачи: ");
//                            String title = scanner.nextLine();
//                            System.out.println("Введите новое описание подзадачи: ");
//                            String description = scanner.nextLine();
//                            System.out.println("Введите новый статус подзадачи (NEW, IN_PROGRESS, DONE)");
//                            String status = scanner.nextLine();
//                            System.out.println("Ведите дату и время в формате: {день.месяц.год часы:минуты}");
//                            String dateTime = scanner.nextLine();
//                            LocalDateTime startTime = null;
//                            if (!dateTime.isEmpty()) {
//                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
//                                startTime = LocalDateTime.parse(dateTime, formatter);
//                            }
//                            System.out.println("Введите продолжительность задачи в минутах: ");
//                            int minutes = scanner.nextInt();
//                            scanner.nextLine();
//                            Duration duration = Duration.ofMinutes(minutes);
//                            taskManager.updateSubtask(subtaskId, new Subtask(title, description, Status.valueOf(status), taskManager.getSubtaskById(subtaskId).getEpicId(), duration, startTime));
//                            System.out.println("Подзадача успешно обновлена!");
//                        } catch (IllegalArgumentException e) {
//                            System.out.println("Ошибка: " + e.getMessage());
//                        }
//                    } else if (numberSubtask == 6) {
//                        System.out.println("Введите id подзадачи: ");
//                        int subtaskId = scanner.nextInt();
//                        taskManager.removeSubtask(subtaskId);
//                    } else if (numberSubtask == 7) {
//                        break;
//                    }
//                }
//            } else if (command == 3) {
//                while (true) {
//                    printMenu();
//                    System.out.println("8. Дополнительно: Вывести список подзадач эпика");
//
//                    int numberEpic = scanner.nextInt();
//                    scanner.nextLine();
//
//                    if (numberEpic == 1) {
//                        System.out.println(taskManager.getEpics());
//                    } else if (numberEpic == 2) {
//                        taskManager.removeAllEpics();
//                        System.out.println("Эпики успешно удалены.");
//                    } else if (numberEpic == 3) {
//                        System.out.println("Введите id эпика: ");
//                        int epicId = scanner.nextInt();
//                        System.out.println("Эпик с id = " + epicId + " - " + taskManager.getEpicById(epicId));
//                    } else if (numberEpic == 4) {
//                        System.out.println("Введите название эпика: ");
//                        String title = scanner.nextLine();
//                        System.out.println("Введите описание эпика: ");
//                        String description = scanner.nextLine();
//                        System.out.println("Эпик c id = " + taskManager.createEpic(new Epic(title, description)) + " успешно создан!");
//                    } else if (numberEpic == 5) {
//                        System.out.println("Введите id эпика: ");
//                        int epicId = scanner.nextInt();
//                        scanner.nextLine();
//                        System.out.println("Введите новое название эпика: ");
//                        String title = scanner.nextLine();
//                        System.out.println("Введите новое описание эпика: ");
//                        String description = scanner.nextLine();
//                        taskManager.updateEpic(epicId, new Epic(title, description));
//                        System.out.println("Эпик успешно обновлен!");
//                    } else if (numberEpic == 6) {
//                        System.out.println("Введите id эпика: ");
//                        int epicId = scanner.nextInt();
//                        taskManager.removeEpic(epicId);
//                    } else if (numberEpic == 7) {
//                        break;
//                    } else if (numberEpic == 8) {
//                        System.out.println("Введите id эпика: ");
//                        int epicId = scanner.nextInt();
//                        scanner.nextLine();
//                        if (taskManager.getEpics().containsValue(taskManager.getEpicById(epicId))) {
//                            for (Integer subtaskId : taskManager.getEpicById(epicId).getSubtaskIds()) {
//                                System.out.println(taskManager.getSubtaskById(subtaskId));
//                            }
//                        } else {
//                            System.out.println("Эпика с таким id не существует");
//                        }
//                    }
//                }
//            } else if (command == 4) {
//                System.out.println(taskManager.getHistoryManager().getHistory());
//            } else if (command == 5) {
//                System.out.println(taskManager.getPrioritizedTasks());
//            } else if (command == 6) {
//                System.out.println("Программа успешно завершена! Хорошего дня!");
//                break;
//            }
//        }
//    }
//
//    public static void printMenuTask() {
//        System.out.println("Выберите тип задачи: ");
//        System.out.println("1. Задача (task)");
//        System.out.println("2. Подзадача (subtask)");
//        System.out.println("3. Эпик (epic)");
//        System.out.println("4. Просмотреть историю задач");
//        System.out.println("5. Вывести список задач в порядке приоритетов");
//        System.out.println("6. Выход");
//    }
//
//    public static void printMenu() {
//        System.out.println("Выберите действие:");
//        System.out.println("1. Вывести список всех задач");
//        System.out.println("2. Удалить все задачи");
//        System.out.println("3. Получить задачу по id");
//        System.out.println("4. Создать");
//        System.out.println("5. Обновить");
//        System.out.println("6. Удалить задачу по id");
//        System.out.println("7. Выход");
//    }
//}