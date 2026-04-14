package com.neonates.mapper;

import com.neonates.entity.Donor;
import com.neonates.request.DonorRequest;
import com.neonates.response.DonorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DonorMapper {

    Donor toEntity(DonorRequest request);

    DonorResponse toResponse(Donor donor);

    List<DonorResponse> toResponseList(List<Donor> donors);

    void updateEntityFromRequest(DonorRequest request, @MappingTarget Donor donor);
}
