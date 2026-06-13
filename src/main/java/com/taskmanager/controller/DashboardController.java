package com.taskmanager.controller;

import com.taskmanager.dto.response.DashboardResponse;
import com.taskmanager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/my-focus")
    public DashboardResponse getMyFocus(@AuthenticationPrincipal Long userId) {
        return dashboardService.getMyFocus(userId);
    }
}
