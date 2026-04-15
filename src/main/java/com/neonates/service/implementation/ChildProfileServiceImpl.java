package com.neonates.service.implementation;

import com.neonates.entity.ChildProfile;
import com.neonates.exception.ResourceNotFoundException;
import com.neonates.mapper.ChildProfileMapper;
import com.neonates.repository.ChildProfileRepository;
import com.neonates.request.ChildProfileRequest;
import com.neonates.response.ChildProfileResponse;
import com.neonates.service.ChildProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChildProfileServiceImpl implements ChildProfileService {

    private final ChildProfileRepository childProfileRepository;
    private final ChildProfileMapper childProfileMapper;

    @Override
    public ChildProfileResponse createChildProfile(ChildProfileRequest request) {
        ChildProfile entity = childProfileMapper.toEntity(request);
        ChildProfile saved = childProfileRepository.save(entity);
        return childProfileMapper.toResponse(saved);
    }

    @Override
    public List<ChildProfileResponse> getChildProfilesByCaseId(Long caseId) {
        List<ChildProfile> entities = childProfileRepository.findByCaseId(caseId);
        return entities.stream()
                .map(childProfileMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ChildProfileResponse updateChildProfile(Long childId, ChildProfileRequest request) {
        ChildProfile entity = childProfileRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child profile not found with id: " + childId));
        childProfileMapper.updateEntityFromRequest(request, entity);
        ChildProfile updated = childProfileRepository.save(entity);
        return childProfileMapper.toResponse(updated);
    }
}
