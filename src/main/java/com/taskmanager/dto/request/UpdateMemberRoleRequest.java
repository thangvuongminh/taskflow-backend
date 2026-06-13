package com.taskmanager.dto.request;
import com.taskmanager.enums.ProjectRole;
import jakarta.validation.constraints.*;
public record UpdateMemberRoleRequest(@NotNull ProjectRole role) {}
