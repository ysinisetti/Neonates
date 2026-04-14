package com.neonates.response;

import com.neonates.Enum.HospitalType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HospitalResponseDTO {
    private Long hospitalId;
    private String hospitalUuid;
    private String hospitalName;
    private HospitalType hospitalType;
    private String city;
    private String state;
    private String spocName;
    private String spocPhone;
    private Boolean activeFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
