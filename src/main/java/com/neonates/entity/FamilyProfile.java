package com.neonates.entity;

import com.neonates.Enum.IncomeCaptureBasis;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "family_profile")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_id")
    private Long familyId;

    @Column(name = "case_id", nullable = false, unique = true)
    private Long caseId;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "father_date_of_birth")
    private LocalDate fatherDateOfBirth;

    @Column(name = "father_phone")
    private String fatherPhone;

    @Column(name = "father_education")
    private String fatherEducation;

    @Column(name = "father_occupation")
    private String fatherOccupation;

    @Column(name = "father_employer_name")
    private String fatherEmployerName;

    @Column(name = "father_monthly_income", precision = 10, scale = 2)
    private BigDecimal fatherMonthlyIncome;

    @Column(name = "father_daily_wage", precision = 10, scale = 2)
    private BigDecimal fatherDailyWage;

    @Column(name = "mother_name")
    private String motherName;

    @Column(name = "mother_date_of_birth")
    private LocalDate motherDateOfBirth;

    @Column(name = "mother_phone")
    private String motherPhone;

    @Column(name = "mother_education")
    private String motherEducation;

    @Column(name = "mother_occupation")
    private String motherOccupation;

    @Column(name = "mother_employer_name")
    private String motherEmployerName;

    @Column(name = "mother_monthly_income", precision = 10, scale = 2)
    private BigDecimal motherMonthlyIncome;

    @Column(name = "mother_daily_wage", precision = 10, scale = 2)
    private BigDecimal motherDailyWage;

    @Column(name = "date_of_marriage")
    private LocalDate dateOfMarriage;

    @Column(name = "years_married", precision = 5, scale = 2)
    private BigDecimal yearsMarried;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "family_members_count")
    private Integer familyMembersCount;

    @Column(name = "total_family_income", precision = 10, scale = 2)
    private BigDecimal totalFamilyIncome;

    @Enumerated(EnumType.STRING)
    @Column(name = "income_capture_basis")
    private IncomeCaptureBasis incomeCaptureBasis;

    @Column(name = "income_threshold_flag", columnDefinition = "TINYINT(1)")
    private Boolean incomeThresholdFlag;

    @Column(name = "income_exception_review_required", columnDefinition = "TINYINT(1) DEFAULT FALSE")
    private Boolean incomeExceptionReviewRequired;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.incomeExceptionReviewRequired == null) {
            this.incomeExceptionReviewRequired = false;
        }
        if (this.incomeThresholdFlag == null) {
            this.incomeThresholdFlag = false;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
