package com.neonates.response;

import com.neonates.Enum.BirthStatus;
import com.neonates.Enum.Gender;
import com.neonates.Enum.MortalityStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ChildProfileResponse {

    private Long childId;
    private Long caseId;
    private String beneficiaryNo;
    private String displayName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private BirthStatus birthStatus;
    private String birthHospitalName;
    private LocalDate admissionDate;
    private BigDecimal gestationalAgeWeeks;
    private BigDecimal birthWeightKg;
    private BigDecimal currentWeightKg;
    private Integer nicuStayDays;
    private MortalityStatus mortalityStatus;
    private String morbidity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
