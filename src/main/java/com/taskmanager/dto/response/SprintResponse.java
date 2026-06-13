package com.taskmanager.dto.response;
import com.taskmanager.enums.SprintStatus;
import java.time.LocalDate;
public record SprintResponse(
    Long id, Long projectId, String name, String goal,
    LocalDate startDate, LocalDate endDate, SprintStatus status,
    long totalTasks, long doneTasks, long totalPoints, long donePoints
) {}
