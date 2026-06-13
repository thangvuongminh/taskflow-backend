package com.taskmanager.dto.request;
import jakarta.validation.constraints.*;
public record CreateCommentRequest(@NotBlank String content) {}
