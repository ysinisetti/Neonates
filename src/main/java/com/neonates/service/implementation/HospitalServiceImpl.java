package com.neonates.service.implementation;

import com.neonates.entity.Hospital;
import com.neonates.exception.ResourceNotFoundException;
import com.neonates.mapper.HospitalMapper;
import com.neonates.repository.HospitalRepository;
import com.neonates.request.HospitalRequestDTO;
import com.neonates.response.HospitalResponseDTO;
import com.neonates.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalMapper hospitalMapper;

    @Override
    public HospitalResponseDTO createHospital(HospitalRequestDTO dto) {
        Hospital hospital = hospitalMapper.toEntity(dto);
        hospital.setActiveFlag(true);
        Hospital savedHospital = hospitalRepository.save(hospital);
        return hospitalMapper.toDTO(savedHospital);
    }

    @Override
    public HospitalResponseDTO updateHospital(Long id, HospitalRequestDTO dto) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + id));
        hospital.setHospitalName(dto.getHospitalName());
        hospital.setHospitalType(dto.getHospitalType());
        hospital.setCity(dto.getCity());
        hospital.setState(dto.getState());
        hospital.setSpocName(dto.getSpocName());
        hospital.setSpocPhone(dto.getSpocPhone());
        Hospital updatedHospital = hospitalRepository.save(hospital);
        return hospitalMapper.toDTO(updatedHospital);
    }

    @Override
    public List<HospitalResponseDTO> getAllHospitals() {
        List<Hospital> hospitals = hospitalRepository.findByActiveFlagTrue();
        return hospitals.stream()
                .map(hospitalMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HospitalResponseDTO getHospitalById(Long id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + id));
        return hospitalMapper.toDTO(hospital);
    }

    @Override
    public void deactivateHospital(Long id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + id));
        hospital.setActiveFlag(false);
        hospitalRepository.save(hospital);
    }
}
