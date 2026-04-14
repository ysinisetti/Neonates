package com.neonates.response;

import com.neonates.Enum.UserRole;
import lombok.Data;

@Data
public class AppUserResponseDTO {
    private Long userId;
    private String userUuid;
    private String fullName;
    private String email;
    private String phone;
    private UserRole primaryRole;
    private Long hospitalId;
    private String uiLanguageCode;
    private Boolean activeFlag;
}
