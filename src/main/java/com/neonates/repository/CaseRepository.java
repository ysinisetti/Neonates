package com.neonates.repository;

import com.neonates.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {

    List<Case> findByHospitalId(Long hospitalId);

    List<Case> findByCaseStatus(com.neonates.Enum.CaseStatus caseStatus);
}
