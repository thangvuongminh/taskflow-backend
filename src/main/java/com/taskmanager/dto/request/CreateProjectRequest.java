package com.taskmanager.dto.request;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record CreateProjectRequest(
    @NotBlank @Size(max = 100) String name,
    String description,
    LocalDate startDate,
    LocalDate endDate
) {}
