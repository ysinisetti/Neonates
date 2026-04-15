package com.neonates.mapper;

import com.neonates.entity.PanelAssignment;
import com.neonates.request.PanelAssignmentCreateRequest;
import com.neonates.response.PanelAssignmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PanelAssignmentMapper {

    @Mapping(target = "panelAssignmentId", ignore = true)
    @Mapping(target = "assignedAt", ignore = true)
    @Mapping(target = "assignmentStatus", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "dueDate", source = "deadline")
    PanelAssignment toEntity(PanelAssignmentCreateRequest request);

    @Mapping(target = "deadline", source = "dueDate")
    PanelAssignmentResponse toResponse(PanelAssignment entity);
}
