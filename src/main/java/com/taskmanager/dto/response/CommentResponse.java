package com.taskmanager.dto.response;
import java.time.LocalDateTime;
public record CommentResponse(Long id, Long taskId, UserSummary user, String content, LocalDateTime createdAt) {}
