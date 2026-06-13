package com.taskmanager.dto.request;
import com.taskmanager.enums.TaskStatus;
import jakarta.validation.constraints.*;
public record UpdateTaskStatusRequest(@NotNull TaskStatus status) {}
