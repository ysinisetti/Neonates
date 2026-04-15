package com.neonates.repository;

import com.neonates.entity.DimDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DimDateRepository extends JpaRepository<DimDate, Integer> {
}
