package com.neonates.service.implementation;

import com.neonates.entity.Hospital;
import com.neonates.entity.HospitalProcessMap;
import com.neonates.exception.BadRequestException;
import com.neonates.exception.ResourceNotFoundException;
import com.neonates.mapper.HospitalProcessMapMapper;
import com.neonates.repository.HospitalProcessMapRepository;
import com.neonates.repository.HospitalRepository;
import com.neonates.request.HospitalProcessMapRequestDTO;
import com.neonates.response.HospitalProcessMapResponseDTO;
import com.neonates.service.HospitalProcessMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HospitalProcessMapServiceImpl implements HospitalProcessMapService {

    private final HospitalProcessMapRepository hospitalProcessMapRepository;
    private final HospitalRepository hospitalRepository;
    private final HospitalProcessMapMapper hospitalProcessMapMapper;

    @Override
    public HospitalProcessMapResponseDTO createMapping(HospitalProcessMapRequestDTO dto) {
        // Validate hospital exists
        Hospital hospital = hospitalRepository.findById(dto.getHospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + dto.getHospitalId()));

        // Validate dates
        if (dto.getEffectiveFrom() == null) {
            throw new BadRequestException("Effective from date is required");
        }
        if (dto.getEffectiveTo() != null && dto.getEffectiveTo().isBefore(dto.getEffectiveFrom())) {
            throw new BadRequestException("Effective to date must be after effective from date");
        }

        // Check for existing active mapping for the same hospital and process type
        List<HospitalProcessMap> existingMappings = hospitalProcessMapRepository.findByHospitalHospitalId(dto.getHospitalId());
        boolean hasActive = existingMappings.stream()
                .anyMatch(mapping -> mapping.getProcessType().toString().equals(dto.getProcessType().toString()) && mapping.getActiveFlag());
        if (hasActive) {
            throw new BadRequestException("An active mapping already exists for this hospital and process type");
        }

        HospitalProcessMap hospitalProcessMap = hospitalProcessMapMapper.toEntity(dto);
        hospitalProcessMap.setHospital(hospital);
        hospitalProcessMap.setActiveFlag(true);
        HospitalProcessMap savedMapping = hospitalProcessMapRepository.save(hospitalProcessMap);
        return hospitalProcessMapMapper.toDTO(savedMapping);
    }

    @Override
    public HospitalProcessMapResponseDTO updateMapping(Long id, HospitalProcessMapRequestDTO dto) {
        HospitalProcessMap hospitalProcessMap = hospitalProcessMapRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HospitalProcessMap not found with id: " + id));

        // Validate hospital exists if changing
        if (!hospitalProcessMap.getHospital().getHospitalId().equals(dto.getHospitalId())) {
            Hospital hospital = hospitalRepository.findById(dto.getHospitalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + dto.getHospitalId()));
            hospitalProcessMap.setHospital(hospital);
        }

        // Validate dates
        if (dto.getEffectiveFrom() == null) {
            throw new BadRequestException("Effective from date is required");
        }
        if (dto.getEffectiveTo() != null && dto.getEffectiveTo().isBefore(dto.getEffectiveFrom())) {
            throw new BadRequestException("Effective to date must be after effective from date");
        }

        hospitalProcessMap.setProcessType(dto.getProcessType());
        hospitalProcessMap.setEffectiveFrom(dto.getEffectiveFrom());
        hospitalProcessMap.setEffectiveTo(dto.getEffectiveTo());
        HospitalProcessMap updatedMapping = hospitalProcessMapRepository.save(hospitalProcessMap);
        return hospitalProcessMapMapper.toDTO(updatedMapping);
    }

    @Override
    public List<HospitalProcessMapResponseDTO> getMappingsByHospital(Long hospitalId) {
        List<HospitalProcessMap> mappings = hospitalProcessMapRepository.findByHospitalHospitalId(hospitalId);
        return mappings.stream()
                .map(hospitalProcessMapMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HospitalProcessMapResponseDTO> getActiveMappings() {
        List<HospitalProcessMap> mappings = hospitalProcessMapRepository.findByActiveFlagTrue();
        return mappings.stream()
                .map(hospitalProcessMapMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deactivateMapping(Long id) {
        HospitalProcessMap hospitalProcessMap = hospitalProcessMapRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HospitalProcessMap not found with id: " + id));
        hospitalProcessMap.setActiveFlag(false);
        hospitalProcessMapRepository.save(hospitalProcessMap);
    }
}
