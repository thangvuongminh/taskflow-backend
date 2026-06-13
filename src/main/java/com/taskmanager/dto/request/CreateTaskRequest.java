package com.taskmanager.dto.request;
import com.taskmanager.enums.Priority;
import com.taskmanager.enums.TaskStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record CreateTaskRequest(
    @NotBlank @Size(max = 200) String title,
    String description,
    TaskStatus status,
    Priority priority,
    Integer storyPoints,
    Long assigneeId,
    Long sprintId,
    LocalDate dueDate
) {}
