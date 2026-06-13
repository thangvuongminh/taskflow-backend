package com.taskmanager.dto.response;
import java.util.List;
public record DashboardResponse(
    List<TaskResponse> todayTasks,
    List<TaskResponse> upcomingTasks,
    List<SprintProgressResponse> sprintProgress
) {}
