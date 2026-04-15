package com.neonates.entity;

import com.neonates.Enum.BirthStatus;
import com.neonates.Enum.Gender;
import com.neonates.Enum.MortalityStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "child_profile")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "child_id")
    private Long childId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Column(name = "beneficiary_no")
    private String beneficiaryNo;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "birth_status")
    private BirthStatus birthStatus;

    @Column(name = "birth_hospital_name")
    private String birthHospitalName;

    @Column(name = "admission_date")
    private LocalDate admissionDate;

    @Column(name = "gestational_age_weeks", precision = 5, scale = 2)
    private BigDecimal gestationalAgeWeeks;

    @Column(name = "birth_weight_kg", precision = 5, scale = 2)
    private BigDecimal birthWeightKg;

    @Column(name = "current_weight_kg", precision = 5, scale = 2)
    private BigDecimal currentWeightKg;

    @Column(name = "nicu_stay_days")
    private Integer nicuStayDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "mortality_status")
    private MortalityStatus mortalityStatus;

    @Column(name = "morbidity")
    private String morbidity;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
