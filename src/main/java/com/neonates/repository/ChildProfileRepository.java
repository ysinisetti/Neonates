package com.neonates.repository;

import com.neonates.entity.ChildProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildProfileRepository extends JpaRepository<ChildProfile, Long> {

    List<ChildProfile> findByCaseId(Long caseId);
}
