package com.taskmanager.repository;

import com.taskmanager.entity.Sprint;
import com.taskmanager.enums.SprintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
    List<Sprint> findByProjectIdOrderByIdDesc(Long projectId);
    Optional<Sprint> findByProjectIdAndStatus(Long projectId, SprintStatus status);
    boolean existsByProjectIdAndStatus(Long projectId, SprintStatus status);

    @Query("SELECT s FROM Sprint s JOIN ProjectMember pm ON pm.project = s.project WHERE pm.user.id = :userId AND s.status = 'ACTIVE'")
    List<Sprint> findActiveSprintsByUserId(@Param("userId") Long userId);
}
