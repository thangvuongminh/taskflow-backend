package com.taskmanager.service;

import com.taskmanager.dto.response.*;
import com.taskmanager.entity.Sprint;
import com.taskmanager.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TaskRepository taskRepository;
    private final SprintRepository sprintRepository;
    private final TaskService taskService;

    public DashboardResponse getMyFocus(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate in3Days = today.plusDays(3);

        List<TaskResponse> todayTasks = taskRepository.findTodayTasks(userId, today)
            .stream().map(taskService::toResponse).toList();

        List<TaskResponse> upcomingTasks = taskRepository.findUpcomingTasks(userId, today, in3Days)
            .stream().map(taskService::toResponse).toList();

        List<SprintProgressResponse> sprintProgress = sprintRepository.findActiveSprintsByUserId(userId)
            .stream().map(this::toSprintProgress).toList();

        return new DashboardResponse(todayTasks, upcomingTasks, sprintProgress);
    }

    private SprintProgressResponse toSprintProgress(Sprint s) {
        long total = taskRepository.countBySprintId(s.getId());
        long done = taskRepository.countDoneBySprintId(s.getId());
        long totalPts = taskRepository.sumStoryPointsBySprintId(s.getId());
        long donePts = taskRepository.sumDoneStoryPointsBySprintId(s.getId());
        return new SprintProgressResponse(
            s.getId(), s.getName(),
            s.getProject().getId(), s.getProject().getName(),
            total, done, totalPts, donePts, s.getEndDate()
        );
    }
}
