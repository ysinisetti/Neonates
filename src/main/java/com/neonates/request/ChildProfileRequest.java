package com.neonates.request;

import com.neonates.Enum.BirthStatus;
import com.neonates.Enum.Gender;
import com.neonates.Enum.MortalityStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ChildProfileRequest {

    @NotNull(message = "Case ID is mandatory")
    private Long caseId;

    private String beneficiaryNo;

    @NotNull(message = "Display name is mandatory")
    private String displayName;

    private Gender gender;

    private LocalDate dateOfBirth;

    private BirthStatus birthStatus;

    private String birthHospitalName;

    private LocalDate admissionDate;

    private BigDecimal gestationalAgeWeeks;

    @DecimalMin(value = "0.0", inclusive = false, message = "Birth weight must be positive")
    private BigDecimal birthWeightKg;

    @DecimalMin(value = "0.0", inclusive = false, message = "Current weight must be positive")
    private BigDecimal currentWeightKg;

    private Integer nicuStayDays;

    private MortalityStatus mortalityStatus;

    private String morbidity;
}
