package com.neonates.repository;

import com.neonates.entity.BalanceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BalanceSnapshotRepository extends JpaRepository<BalanceSnapshot, Long> {

    List<BalanceSnapshot> findByDateId(Integer dateId);

    List<BalanceSnapshot> findByCapturedByUserId(Long userId);

    Optional<BalanceSnapshot> findTopByOrderByCreatedAtDesc();
}
