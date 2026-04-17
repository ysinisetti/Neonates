package com.neonates.mapper;

import com.neonates.entity.BeneficiaryInterview;
import com.neonates.request.BeneficiaryInterviewCreateRequest;
import com.neonates.response.BeneficiaryInterviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BeneficiaryInterviewMapper {

    @Mapping(target = "caseId", source = "caseMaster.caseId")
    BeneficiaryInterviewResponse toResponse(BeneficiaryInterview entity);

    List<BeneficiaryInterviewResponse> toResponseList(List<BeneficiaryInterview> entities);

    @Mapping(target = "caseMaster.caseId", source = "caseId")
    BeneficiaryInterview toEntity(BeneficiaryInterviewCreateRequest request);

    void updateEntityFromRequest(BeneficiaryInterviewCreateRequest request, @MappingTarget BeneficiaryInterview entity);
}
