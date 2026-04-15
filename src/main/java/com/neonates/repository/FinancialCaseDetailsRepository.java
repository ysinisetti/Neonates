package com.neonates.repository;

import com.neonates.entity.FinancialCaseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FinancialCaseDetailsRepository extends JpaRepository<FinancialCaseDetails, Long> {

    Optional<FinancialCaseDetails> findByCaseId(Long caseId);
}
