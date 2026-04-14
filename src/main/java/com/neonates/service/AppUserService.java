package com.neonates.service;

import com.neonates.Enum.UserRole;
import com.neonates.request.AppUserRequestDTO;
import com.neonates.response.AppUserResponseDTO;

import java.util.List;

public interface AppUserService {

    AppUserResponseDTO createUser(AppUserRequestDTO dto);

    AppUserResponseDTO updateUser(Long id, AppUserRequestDTO dto);

    AppUserResponseDTO getUserById(Long id);

    List<AppUserResponseDTO> getAllUsers();

    void deactivateUser(Long id);

    List<AppUserResponseDTO> getUsersByRole(UserRole role);
}
