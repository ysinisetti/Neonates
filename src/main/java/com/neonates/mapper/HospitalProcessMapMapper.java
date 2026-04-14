package com.neonates.mapper;

import com.neonates.entity.Hospital;
import com.neonates.entity.HospitalProcessMap;
import com.neonates.request.HospitalProcessMapRequestDTO;
import com.neonates.response.HospitalProcessMapResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HospitalProcessMapMapper {

    @Mapping(target = "hospitalProcessId", ignore = true)
    @Mapping(target = "hospital", source = "hospitalId")
    @Mapping(target = "activeFlag", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    HospitalProcessMap toEntity(HospitalProcessMapRequestDTO dto);

    @Mapping(target = "hospitalId", source = "hospital.hospitalId")
    HospitalProcessMapResponseDTO toDTO(HospitalProcessMap hospitalProcessMap);

    default Hospital mapHospitalIdToHospital(Long hospitalId) {
        if (hospitalId == null) {
            return null;
        }
        Hospital hospital = new Hospital();
        hospital.setHospitalId(hospitalId);
        return hospital;
    }
}
