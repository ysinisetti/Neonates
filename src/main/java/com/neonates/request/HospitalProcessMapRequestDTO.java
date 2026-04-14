package com.neonates.request;

import com.neonates.Enum.ProcessType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HospitalProcessMapRequestDTO {
    private Long hospitalId;
    private ProcessType processType;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
