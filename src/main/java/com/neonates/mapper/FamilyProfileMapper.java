package com.neonates.mapper;

import com.neonates.entity.FamilyProfile;
import com.neonates.request.FamilyProfileRequest;
import com.neonates.response.FamilyProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FamilyProfileMapper {

    @Mapping(target = "familyId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "totalFamilyIncome", ignore = true)
    FamilyProfile toEntity(FamilyProfileRequest request);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "totalFamilyIncome", ignore = true)
    void updateEntityFromRequest(FamilyProfileRequest request, @MappingTarget FamilyProfile entity);

    FamilyProfileResponse toResponse(FamilyProfile entity);
}
