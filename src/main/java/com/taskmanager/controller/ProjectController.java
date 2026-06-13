package com.taskmanager.controller;

import com.taskmanager.dto.request.*;
import com.taskmanager.dto.response.*;
import com.taskmanager.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public List<ProjectResponse> getMyProjects(@AuthenticationPrincipal Long userId) {
        return projectService.getMyProjects(userId);
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest req,
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(req, userId));
    }

    @GetMapping("/{id}")
    public ProjectResponse getProject(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        return projectService.getProject(id, userId);
    }

    @PutMapping("/{id}")
    public ProjectResponse updateProject(@PathVariable Long id,
            @Valid @RequestBody UpdateProjectRequest req,
            @AuthenticationPrincipal Long userId) {
        return projectService.updateProject(id, req, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        projectService.deleteProject(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/members")
    public List<MemberResponse> getMembers(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        return projectService.getMembers(id, userId);
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<MemberResponse> addMember(@PathVariable Long id,
            @Valid @RequestBody AddMemberRequest req,
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.addMember(id, req, userId));
    }

    @PutMapping("/{id}/members/{targetUserId}")
    public MemberResponse updateRole(@PathVariable Long id,
            @PathVariable Long targetUserId,
            @Valid @RequestBody UpdateMemberRoleRequest req,
            @AuthenticationPrincipal Long userId) {
        return projectService.updateMemberRole(id, targetUserId, req, userId);
    }

    @DeleteMapping("/{id}/members/{targetUserId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long id,
            @PathVariable Long targetUserId,
            @AuthenticationPrincipal Long userId) {
        projectService.removeMember(id, targetUserId, userId);
        return ResponseEntity.noContent().build();
    }
}
