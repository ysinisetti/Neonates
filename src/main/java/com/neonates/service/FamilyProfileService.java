package com.neonates.service;

import com.neonates.request.FamilyProfileRequest;
import com.neonates.response.FamilyProfileResponse;

import java.util.Optional;

public interface FamilyProfileService {

    FamilyProfileResponse createFamilyProfile(FamilyProfileRequest request);

    Optional<FamilyProfileResponse> getFamilyProfileByCaseId(Long caseId);

    FamilyProfileResponse updateFamilyProfile(Long familyId, FamilyProfileRequest request);
}
