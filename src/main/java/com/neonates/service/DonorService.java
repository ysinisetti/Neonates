package com.neonates.service;

import com.neonates.request.DonorRequest;
import com.neonates.response.DonorResponse;

import java.util.List;

public interface DonorService {
    DonorResponse createDonor(DonorRequest request);
    DonorResponse getDonorById(Long id);
    List<DonorResponse> getAllDonors();
    DonorResponse updateDonor(Long id, DonorRequest request);
    void deleteDonor(Long id);
}
