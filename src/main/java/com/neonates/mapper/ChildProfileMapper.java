package com.neonates.mapper;

import com.neonates.entity.ChildProfile;
import com.neonates.request.ChildProfileRequest;
import com.neonates.response.ChildProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ChildProfileMapper {

    @Mapping(target = "childId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ChildProfile toEntity(ChildProfileRequest request);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(ChildProfileRequest request, @MappingTarget ChildProfile entity);

    ChildProfileResponse toResponse(ChildProfile entity);
}
