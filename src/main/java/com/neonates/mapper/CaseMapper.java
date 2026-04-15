package com.neonates.mapper;

import com.neonates.entity.Case;
import com.neonates.request.CaseCreateRequestDTO;
import com.neonates.response.CaseResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CaseMapper {

    @Mapping(target = "caseId", ignore = true)
    @Mapping(target = "caseUuid", ignore = true)
    @Mapping(target = "caseReferenceNo", ignore = true)
    @Mapping(target = "caseStatus", ignore = true)
    @Mapping(target = "intakeDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Case toEntity(CaseCreateRequestDTO dto);

    CaseResponseDTO toDTO(Case caseEntity);
}
