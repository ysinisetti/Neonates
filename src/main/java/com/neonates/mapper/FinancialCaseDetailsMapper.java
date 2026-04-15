package com.neonates.mapper;

import com.neonates.entity.FinancialCaseDetails;
import com.neonates.request.FinancialCaseCreateRequest;
import com.neonates.response.FinancialCaseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FinancialCaseDetailsMapper {

    @Mapping(target = "financialId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "beneficiaryPayableBalance", ignore = true)
    @Mapping(target = "totalAmountApproved", ignore = true)
    FinancialCaseDetails toEntity(FinancialCaseCreateRequest request);

    FinancialCaseResponse toResponse(FinancialCaseDetails entity);
}
