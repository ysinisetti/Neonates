package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "fact_hospital_onboarding")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactHospitalOnboarding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "onboarding_id")
    private Long onboardingId;

    @Column(name = "hospital_id", nullable = false)
    private Long hospitalId;

    @Column(name = "stage", nullable = false, length = 50)
    private String stage;

    @Column(name = "stage_date_id", nullable = false)
    private Integer stageDateId;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
