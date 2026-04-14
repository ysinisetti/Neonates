package com.neonates.service;

import com.neonates.entity.Donor;
import com.neonates.exception.ResourceNotFoundException;
import com.neonates.mapper.DonorMapper;
import com.neonates.repository.DonorRepository;
import com.neonates.request.DonorRequest;
import com.neonates.response.DonorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonorServiceImpl implements DonorService {

    private final DonorRepository donorRepository;
    private final DonorMapper donorMapper;

    @Override
    @Transactional
    public DonorResponse createDonor(DonorRequest request) {
        Donor donor = donorMapper.toEntity(request);
        donor = donorRepository.save(donor);
        return donorMapper.toResponse(donor);
    }

    @Override
    public DonorResponse getDonorById(Long id) {
        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: " + id));
        return donorMapper.toResponse(donor);
    }

    @Override
    public List<DonorResponse> getAllDonors() {
        List<Donor> donors = donorRepository.findAll();
        return donorMapper.toResponseList(donors);
    }

    @Override
    @Transactional
    public DonorResponse updateDonor(Long id, DonorRequest request) {
        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: " + id));
        
        donorMapper.updateEntityFromRequest(request, donor);
        donor = donorRepository.save(donor);
        return donorMapper.toResponse(donor);
    }

    @Override
    @Transactional
    public void deleteDonor(Long id) {
        if (!donorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Donor not found with id: " + id);
        }
        donorRepository.deleteById(id);
    }
}
