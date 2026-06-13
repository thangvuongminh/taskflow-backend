package com.taskmanager.dto.request;
import com.taskmanager.enums.ProjectRole;
import jakarta.validation.constraints.*;
public record AddMemberRequest(
    @NotBlank @Email String email,
    @NotNull ProjectRole role
) {}
