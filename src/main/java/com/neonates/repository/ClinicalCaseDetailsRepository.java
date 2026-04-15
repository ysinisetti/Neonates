package com.neonates.repository;

import com.neonates.entity.ClinicalCaseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicalCaseDetailsRepository extends JpaRepository<ClinicalCaseDetails, Long> {

    Optional<ClinicalCaseDetails> findByCaseId(Long caseId);
}
