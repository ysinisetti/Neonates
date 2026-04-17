package com.neonates.repository;

import com.neonates.entity.BeneficiaryInterview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BeneficiaryInterviewRepository extends JpaRepository<BeneficiaryInterview, Long> {
    Optional<BeneficiaryInterview> findByCaseMaster_CaseId(Long caseId);
}
