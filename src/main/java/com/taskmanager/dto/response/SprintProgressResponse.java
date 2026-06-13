package com.taskmanager.dto.response;
import java.time.LocalDate;
public record SprintProgressResponse(
    Long sprintId, String sprintName, Long projectId, String projectName,
    long totalTasks, long doneTasks, long totalPoints, long donePoints,
    LocalDate endDate
) {}
