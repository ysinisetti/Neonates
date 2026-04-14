package com.neonates.response;

import com.neonates.Enum.DonorType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DonorResponse {
    private Long donorId;
    private String donorName;
    private DonorType donorType;
    private String contactEmail;
    private Boolean activeFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
