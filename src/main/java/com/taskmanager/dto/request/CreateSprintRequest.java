package com.taskmanager.dto.request;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record CreateSprintRequest(
    @NotBlank @Size(max = 100) String name,
    String goal,
    LocalDate startDate,
    LocalDate endDate
) {}
