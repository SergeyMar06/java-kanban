package ru.common.model;

import ru.common.enums.Status;
import ru.common.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;
    public static final TaskType TASK_TYPE = TaskType.EPIC;

    public Epic(String title, String description) {
        super(title, description, Status.NEW, null, null);
        subtaskIds = new ArrayList<>();
    }

    public Epic(Epic other) {
        super(other.getTitle(), other.getDescription(), other.getStatus(), other.getDuration(), other.getStartTime());
        setId(other.getId());
        this.subtaskIds = new ArrayList<>(other.getSubtaskIds());
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskId(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void updateTimes(HashMap<Integer, Subtask> allSubtasks) {
        if (subtaskIds.isEmpty()) {
            setStartTime(null);
            setDuration(Duration.ZERO);
            return;
        }

        LocalDateTime earliestStart = null;
        Duration totalDuration = Duration.ZERO;

        for (Integer id : subtaskIds) {
            Subtask sub = allSubtasks.get(id);
            if (sub == null || sub.getStartTime() == null) continue;

            // Находим самое раннее время старта
            if (earliestStart == null || sub.getStartTime().isBefore(earliestStart)) {
                earliestStart = sub.getStartTime();
            }

            // Суммируем длительность
            if (sub.getDuration() != null) {
                totalDuration = totalDuration.plus(sub.getDuration());
            }
        }

        setStartTime(earliestStart);
        setDuration(totalDuration);
    }

    @Override
    public LocalDateTime getEndTime() {
        if (getStartTime() != null && getDuration() != null) {
            return getStartTime().plus(getDuration());
        }
        return null;
    }


    @Override
    public TaskType getTaskType() {
        return TASK_TYPE;
    }


    @Override
    public String toString() {
        String str = "ru.common.model.Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", subtaskIds=" + subtaskIds +
                ", duration=" + getDuration() +
                ", endTime=";
                if (getDuration() == null || getStartTime() == null) {
                    str += "null";
                } else {
                    str += getEndTime();
                }

        str += '}';

        return str;
    }
}
