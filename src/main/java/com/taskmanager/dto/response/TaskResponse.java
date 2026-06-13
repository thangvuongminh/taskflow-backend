package com.taskmanager.dto.response;
import com.taskmanager.enums.Priority;
import com.taskmanager.enums.TaskStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
public record TaskResponse(
    Long id, Long projectId, String projectName,
    Long sprintId, String sprintName,
    String title, String description,
    TaskStatus status, Priority priority,
    Integer storyPoints,
    UserSummary assignee, UserSummary createdBy,
    LocalDate dueDate, LocalDateTime createdAt, LocalDateTime updatedAt
) {}
