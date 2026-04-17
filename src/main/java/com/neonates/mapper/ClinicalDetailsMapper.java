package com.neonates.mapper;

import com.neonates.entity.ClinicalDetails;
import com.neonates.request.ClinicalDetailsRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ClinicalDetailsMapper {

    ClinicalDetails toEntity(ClinicalDetailsRequest request);

    void updateEntityFromRequest(ClinicalDetailsRequest request, @MappingTarget ClinicalDetails entity);
}
