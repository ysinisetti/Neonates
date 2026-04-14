package com.neonates.mapper;

import com.neonates.entity.Hospital;
import com.neonates.request.HospitalRequestDTO;
import com.neonates.response.HospitalResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HospitalMapper {

    @Mapping(target = "hospitalId", ignore = true)
    @Mapping(target = "hospitalUuid", ignore = true)
    @Mapping(target = "activeFlag", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Hospital toEntity(HospitalRequestDTO dto);

    HospitalResponseDTO toDTO(Hospital hospital);
}
