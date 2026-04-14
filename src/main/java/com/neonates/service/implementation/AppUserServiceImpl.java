package com.neonates.service.implementation;

import com.neonates.Enum.UserRole;
import com.neonates.entity.AppUser;
import com.neonates.entity.Hospital;
import com.neonates.exception.DuplicateResourceException;
import com.neonates.exception.ResourceNotFoundException;
import com.neonates.mapper.AppUserMapper;
import com.neonates.repository.AppUserRepository;
import com.neonates.repository.HospitalRepository;
import com.neonates.request.AppUserRequestDTO;
import com.neonates.response.AppUserResponseDTO;
import com.neonates.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final HospitalRepository hospitalRepository;
    private final AppUserMapper appUserMapper;

    @Override
    public AppUserResponseDTO createUser(AppUserRequestDTO dto) {
        // Check email uniqueness
        if (appUserRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User with email " + dto.getEmail() + " already exists");
        }

        // Validate hospital if provided
        Hospital hospital = null;
        if (dto.getHospitalId() != null) {
            hospital = hospitalRepository.findById(dto.getHospitalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + dto.getHospitalId()));
        }

        AppUser appUser = appUserMapper.toEntity(dto);
        appUser.setHospital(hospital);
        appUser.setActiveFlag(true);
        if (dto.getUiLanguageCode() == null) {
            appUser.setUiLanguageCode("en");
        }
        AppUser savedUser = appUserRepository.save(appUser);
        return appUserMapper.toDTO(savedUser);
    }

    @Override
    public AppUserResponseDTO updateUser(Long id, AppUserRequestDTO dto) {
        AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Check email uniqueness if changed
        if (!appUser.getEmail().equals(dto.getEmail()) && appUserRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User with email " + dto.getEmail() + " already exists");
        }

        // Validate hospital if provided
        Hospital hospital = null;
        if (dto.getHospitalId() != null) {
            hospital = hospitalRepository.findById(dto.getHospitalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + dto.getHospitalId()));
        }

        appUser.setFullName(dto.getFullName());
        appUser.setEmail(dto.getEmail());
        appUser.setPhone(dto.getPhone());
        appUser.setPrimaryRole(dto.getPrimaryRole());
        appUser.setHospital(hospital);
        appUser.setUiLanguageCode(dto.getUiLanguageCode() != null ? dto.getUiLanguageCode() : "en");
        AppUser updatedUser = appUserRepository.save(appUser);
        return appUserMapper.toDTO(updatedUser);
    }

    @Override
    public AppUserResponseDTO getUserById(Long id) {
        AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return appUserMapper.toDTO(appUser);
    }

    @Override
    public List<AppUserResponseDTO> getAllUsers() {
        List<AppUser> users = appUserRepository.findByActiveFlagTrue();
        return users.stream()
                .map(appUserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deactivateUser(Long id) {
        AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        appUser.setActiveFlag(false);
        appUserRepository.save(appUser);
    }

    @Override
    public List<AppUserResponseDTO> getUsersByRole(UserRole role) {
        List<AppUser> users = appUserRepository.findByPrimaryRole(role);
        return users.stream()
                .map(appUserMapper::toDTO)
                .collect(Collectors.toList());
    }
}
