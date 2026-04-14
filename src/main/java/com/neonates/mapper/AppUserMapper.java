package com.neonates.mapper;

import com.neonates.entity.AppUser;
import com.neonates.entity.Hospital;
import com.neonates.request.AppUserRequestDTO;
import com.neonates.response.AppUserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "userUuid", ignore = true)
    @Mapping(target = "activeFlag", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "hospital", source = "hospitalId")
    AppUser toEntity(AppUserRequestDTO dto);

    @Mapping(target = "hospitalId", source = "hospital.hospitalId")
    AppUserResponseDTO toDTO(AppUser appUser);

    default Hospital mapHospitalIdToHospital(Long hospitalId) {
        if (hospitalId == null) {
            return null;
        }
        Hospital hospital = new Hospital();
        hospital.setHospitalId(hospitalId);
        return hospital;
    }
}
