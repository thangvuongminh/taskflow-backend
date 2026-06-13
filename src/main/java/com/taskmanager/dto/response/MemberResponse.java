package com.taskmanager.dto.response;
import com.taskmanager.enums.ProjectRole;
public record MemberResponse(Long userId, String username, String email, String avatarUrl, ProjectRole role) {}
