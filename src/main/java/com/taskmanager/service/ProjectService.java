package com.taskmanager.service;

import com.taskmanager.dto.request.*;
import com.taskmanager.dto.response.*;
import com.taskmanager.entity.*;
import com.taskmanager.enums.ProjectRole;
import com.taskmanager.exception.*;
import com.taskmanager.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository memberRepository;
    private final UserRepository userRepository;

    public List<ProjectResponse> getMyProjects(Long userId) {
        return projectRepository.findByMemberUserId(userId)
            .stream().map(this::toResponse).toList();
    }

    @Transactional
    public ProjectResponse createProject(CreateProjectRequest req, Long userId) {
        User creator = getUser(userId);
        Project project = Project.builder()
            .name(req.name()).description(req.description())
            .startDate(req.startDate()).endDate(req.endDate())
            .createdBy(creator).build();
        project = projectRepository.save(project);
        ProjectMember admin = ProjectMember.builder()
            .project(project).user(creator).role(ProjectRole.ADMIN).build();
        memberRepository.save(admin);
        return toResponse(project);
    }

    public ProjectResponse getProject(Long projectId, Long userId) {
        requireMember(projectId, userId);
        return toResponse(getProjectEntity(projectId));
    }

    @Transactional
    public ProjectResponse updateProject(Long projectId, UpdateProjectRequest req, Long userId) {
        requireRole(projectId, userId, ProjectRole.ADMIN);
        Project p = getProjectEntity(projectId);
        if (req.name() != null) p.setName(req.name());
        if (req.description() != null) p.setDescription(req.description());
        if (req.startDate() != null) p.setStartDate(req.startDate());
        if (req.endDate() != null) p.setEndDate(req.endDate());
        if (req.status() != null) p.setStatus(req.status());
        return toResponse(projectRepository.save(p));
    }

    @Transactional
    public void deleteProject(Long projectId, Long userId) {
        requireRole(projectId, userId, ProjectRole.ADMIN);
        projectRepository.deleteById(projectId);
    }

    public List<MemberResponse> getMembers(Long projectId, Long userId) {
        requireMember(projectId, userId);
        return memberRepository.findByProjectId(projectId)
            .stream().map(pm -> new MemberResponse(
                pm.getUser().getId(), pm.getUser().getUsername(),
                pm.getUser().getEmail(), pm.getUser().getAvatarUrl(),
                pm.getRole()
            )).toList();
    }

    @Transactional
    public MemberResponse addMember(Long projectId, AddMemberRequest req, Long requesterId) {
        requireRole(projectId, requesterId, ProjectRole.ADMIN);
        User user = userRepository.findByEmail(req.email())
            .orElseThrow(() -> new ResourceNotFoundException("Email chưa đăng ký trong hệ thống"));
        if (memberRepository.existsByProjectIdAndUserId(projectId, user.getId())) {
            throw new ConflictException("User đã là thành viên của dự án");
        }
        Project project = getProjectEntity(projectId);
        ProjectMember pm = ProjectMember.builder()
            .project(project).user(user).role(req.role()).build();
        memberRepository.save(pm);
        return new MemberResponse(user.getId(), user.getUsername(), user.getEmail(), user.getAvatarUrl(), req.role());
    }

    @Transactional
    public MemberResponse updateMemberRole(Long projectId, Long targetUserId, UpdateMemberRoleRequest req, Long requesterId) {
        requireRole(projectId, requesterId, ProjectRole.ADMIN);
        ProjectMember pm = memberRepository.findByProjectIdAndUserId(projectId, targetUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Thành viên không tồn tại"));
        pm.setRole(req.role());
        memberRepository.save(pm);
        User u = pm.getUser();
        return new MemberResponse(u.getId(), u.getUsername(), u.getEmail(), u.getAvatarUrl(), req.role());
    }

    @Transactional
    public void removeMember(Long projectId, Long targetUserId, Long requesterId) {
        requireRole(projectId, requesterId, ProjectRole.ADMIN);
        if (targetUserId.equals(requesterId)) {
            throw new BadRequestException("Không thể tự xóa mình khỏi dự án");
        }
        memberRepository.deleteByProjectIdAndUserId(projectId, targetUserId);
    }

    // ---- PUBLIC HELPERS used by other services ----

    public void requireMember(Long projectId, Long userId) {
        if (!memberRepository.existsByProjectIdAndUserId(projectId, userId)) {
            throw new AccessDeniedException("Bạn không phải thành viên của dự án");
        }
    }

    public ProjectRole getMemberRole(Long projectId, Long userId) {
        return memberRepository.findByProjectIdAndUserId(projectId, userId)
            .map(ProjectMember::getRole)
            .orElseThrow(() -> new AccessDeniedException("Bạn không phải thành viên của dự án"));
    }

    public void requireRole(Long projectId, Long userId, ProjectRole required) {
        ProjectRole actual = getMemberRole(projectId, userId);
        boolean allowed = switch (required) {
            case ADMIN -> actual == ProjectRole.ADMIN;
            case MANAGER -> actual == ProjectRole.ADMIN || actual == ProjectRole.MANAGER;
            case MEMBER -> true;
        };
        if (!allowed) throw new AccessDeniedException("Bạn không có quyền thực hiện thao tác này");
    }

    // ---- PRIVATE HELPERS ----

    private Project getProjectEntity(Long id) {
        return projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dự án không tồn tại"));
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
    }

    private ProjectResponse toResponse(Project p) {
        return new ProjectResponse(
            p.getId(), p.getName(), p.getDescription(), p.getStatus(),
            p.getStartDate(), p.getEndDate(),
            new UserSummary(p.getCreatedBy().getId(), p.getCreatedBy().getUsername(),
                p.getCreatedBy().getEmail(), p.getCreatedBy().getAvatarUrl()),
            p.getCreatedAt()
        );
    }
}
