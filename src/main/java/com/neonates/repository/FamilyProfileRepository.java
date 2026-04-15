package com.neonates.repository;

import com.neonates.entity.FamilyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FamilyProfileRepository extends JpaRepository<FamilyProfile, Long> {

    Optional<FamilyProfile> findByCaseId(Long caseId);
}
