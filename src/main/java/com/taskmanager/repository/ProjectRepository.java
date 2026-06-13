package com.taskmanager.repository;

import com.taskmanager.entity.Project;
import com.taskmanager.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p JOIN ProjectMember pm ON pm.project = p WHERE pm.user.id = :userId AND p.status = :status")
    List<Project> findByMemberUserIdAndStatus(@Param("userId") Long userId, @Param("status") ProjectStatus status);

    @Query("SELECT p FROM Project p JOIN ProjectMember pm ON pm.project = p WHERE pm.user.id = :userId")
    List<Project> findByMemberUserId(@Param("userId") Long userId);
}
