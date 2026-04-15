package com.neonates.repository;

import com.neonates.entity.PanelAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PanelAssignmentRepository extends JpaRepository<PanelAssignment, Long> {

    List<PanelAssignment> findByCaseId(Long caseId);

    List<PanelAssignment> findByReviewerUserId(Long reviewerUserId);
}
