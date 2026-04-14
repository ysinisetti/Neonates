package com.neonates.service;

import com.neonates.request.HospitalProcessMapRequestDTO;
import com.neonates.response.HospitalProcessMapResponseDTO;

import java.util.List;

public interface HospitalProcessMapService {

    HospitalProcessMapResponseDTO createMapping(HospitalProcessMapRequestDTO dto);

    HospitalProcessMapResponseDTO updateMapping(Long id, HospitalProcessMapRequestDTO dto);

    List<HospitalProcessMapResponseDTO> getMappingsByHospital(Long hospitalId);

    List<HospitalProcessMapResponseDTO> getActiveMappings();

    void deactivateMapping(Long id);
}
