package com.taskmanager.dto.request;
import com.taskmanager.enums.ProjectStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record UpdateProjectRequest(
    @Size(max = 100) String name,
    String description,
    LocalDate startDate,
    LocalDate endDate,
    ProjectStatus status
) {}
