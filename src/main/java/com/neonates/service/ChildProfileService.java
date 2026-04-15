package com.neonates.service;

import com.neonates.request.ChildProfileRequest;
import com.neonates.response.ChildProfileResponse;

import java.util.List;

public interface ChildProfileService {

    ChildProfileResponse createChildProfile(ChildProfileRequest request);

    List<ChildProfileResponse> getChildProfilesByCaseId(Long caseId);

    ChildProfileResponse updateChildProfile(Long childId, ChildProfileRequest request);
}
