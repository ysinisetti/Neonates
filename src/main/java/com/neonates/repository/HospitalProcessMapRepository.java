package com.neonates.repository;

import com.neonates.entity.HospitalProcessMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalProcessMapRepository extends JpaRepository<HospitalProcessMap, Long> {

    List<HospitalProcessMap> findByHospitalHospitalId(Long hospitalId);

    List<HospitalProcessMap> findByActiveFlagTrue();

    List<HospitalProcessMap> findByProcessType(String processType);
}
