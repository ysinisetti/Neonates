package com.neonates.response;

import com.neonates.Enum.IncomeCaptureBasis;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class FamilyProfileResponse {

    private Long familyId;
    private Long caseId;
    private String fatherName;
    private LocalDate fatherDateOfBirth;
    private String fatherPhone;
    private String fatherEducation;
    private String fatherOccupation;
    private String fatherEmployerName;
    private BigDecimal fatherMonthlyIncome;
    private BigDecimal fatherDailyWage;
    private String motherName;
    private LocalDate motherDateOfBirth;
    private String motherPhone;
    private String motherEducation;
    private String motherOccupation;
    private String motherEmployerName;
    private BigDecimal motherMonthlyIncome;
    private BigDecimal motherDailyWage;
    private LocalDate dateOfMarriage;
    private BigDecimal yearsMarried;
    private String emailId;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private Integer familyMembersCount;
    private BigDecimal totalFamilyIncome;
    private IncomeCaptureBasis incomeCaptureBasis;
    private Boolean incomeThresholdFlag;
    private Boolean incomeExceptionReviewRequired;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
