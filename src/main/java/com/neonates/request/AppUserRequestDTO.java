package com.neonates.request;

import com.neonates.Enum.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AppUserRequestDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    private String phone;

    private UserRole primaryRole;

    private Long hospitalId;

    private String uiLanguageCode;
}
