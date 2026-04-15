package com.neonates.mapper;

import com.neonates.entity.AppUser;
import com.neonates.entity.BalanceSnapshot;
import com.neonates.request.BalanceSnapshotRequestDTO;
import com.neonates.response.BalanceSnapshotResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BalanceSnapshotMapper {

    @Mapping(target = "balanceSnapshotId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "capturedBy", source = "capturedBy")
    BalanceSnapshot toEntity(BalanceSnapshotRequestDTO dto);

    @Mapping(target = "capturedBy", source = "capturedBy.userId")
    BalanceSnapshotResponseDTO toDTO(BalanceSnapshot balanceSnapshot);

    default AppUser mapUserIdToAppUser(Long userId) {
        if (userId == null) {
            return null;
        }
        AppUser appUser = new AppUser();
        appUser.setUserId(userId);
        return appUser;
    }
}
