package com.neonates.request;

import com.neonates.Enum.IncomeCaptureBasis;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FamilyProfileRequest {

    @NotNull(message = "Case ID is mandatory")
    private Long caseId;

    private String fatherName;

    private LocalDate fatherDateOfBirth;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid father phone number format")
    private String fatherPhone;

    private String fatherEducation;

    private String fatherOccupation;

    private String fatherEmployerName;

    @DecimalMin(value = "0.0", inclusive = true, message = "Father monthly income must be non-negative")
    private BigDecimal fatherMonthlyIncome;

    @DecimalMin(value = "0.0", inclusive = true, message = "Father daily wage must be non-negative")
    private BigDecimal fatherDailyWage;

    private String motherName;

    private LocalDate motherDateOfBirth;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid mother phone number format")
    private String motherPhone;

    private String motherEducation;

    private String motherOccupation;

    private String motherEmployerName;

    @DecimalMin(value = "0.0", inclusive = true, message = "Mother monthly income must be non-negative")
    private BigDecimal motherMonthlyIncome;

    @DecimalMin(value = "0.0", inclusive = true, message = "Mother daily wage must be non-negative")
    private BigDecimal motherDailyWage;

    private LocalDate dateOfMarriage;

    private BigDecimal yearsMarried;

    @Email(message = "Invalid email format")
    private String emailId;

    private String address;

    private String city;

    private String state;

    @Size(min = 6, max = 6, message = "Pincode must be 6 digits")
    private String pincode;

    private Integer familyMembersCount;

    private BigDecimal totalFamilyIncome;

    private IncomeCaptureBasis incomeCaptureBasis;

    private Boolean incomeThresholdFlag;

    private Boolean incomeExceptionReviewRequired;
}
