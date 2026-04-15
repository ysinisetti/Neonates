package com.neonates.mapper;

import com.neonates.entity.ClinicalCaseDetails;
import com.neonates.request.ClinicalCaseCreateRequest;
import com.neonates.response.ClinicalCaseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClinicalCaseDetailsMapper {

    @Mapping(target = "clinicalId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "currentDayOfLife", ignore = true)
    ClinicalCaseDetails toEntity(ClinicalCaseCreateRequest request);

    ClinicalCaseResponse toResponse(ClinicalCaseDetails entity);
}
