package com.neonates.repository;

import com.neonates.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseMasterRepository extends JpaRepository<Case, Long> {
}
