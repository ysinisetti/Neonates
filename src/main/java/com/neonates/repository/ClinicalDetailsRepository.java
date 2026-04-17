package com.neonates.repository;

import com.neonates.entity.ClinicalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicalDetailsRepository extends JpaRepository<ClinicalDetails, Long> {
    Optional<ClinicalDetails> findByCaseId(Long caseId);
}
