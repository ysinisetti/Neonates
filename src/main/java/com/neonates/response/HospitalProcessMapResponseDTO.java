package com.neonates.response;

import com.neonates.Enum.ProcessType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HospitalProcessMapResponseDTO {
    private Long hospitalProcessId;
    private Long hospitalId;
    private ProcessType processType;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Boolean activeFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
