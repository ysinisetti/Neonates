package com.neonates.service;

import com.neonates.request.HospitalRequestDTO;
import com.neonates.response.HospitalResponseDTO;

import java.util.List;

public interface HospitalService {

    HospitalResponseDTO createHospital(HospitalRequestDTO dto);

    HospitalResponseDTO updateHospital(Long id, HospitalRequestDTO dto);

    List<HospitalResponseDTO> getAllHospitals();

    HospitalResponseDTO getHospitalById(Long id);

    void deactivateHospital(Long id);
}
