package com.taskmanager.repository;

import com.taskmanager.entity.ProjectMember;
import com.taskmanager.enums.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByProjectId(Long projectId);
    Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, Long userId);
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    @Transactional
    void deleteByProjectIdAndUserId(Long projectId, Long userId);

    List<ProjectMember> findByUserIdAndRole(Long userId, ProjectRole role);
}
