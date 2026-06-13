package com.taskmanager.dto.response;
import com.taskmanager.enums.ProjectStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
public record ProjectResponse(
    Long id, String name, String description,
    ProjectStatus status, LocalDate startDate, LocalDate endDate,
    UserSummary createdBy, LocalDateTime createdAt
) {}
