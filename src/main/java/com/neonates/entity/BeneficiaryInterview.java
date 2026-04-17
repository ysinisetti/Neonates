package com.neonates.entity;

import com.neonates.Enum.InterviewStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "beneficiary_interview")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryInterview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interview_id")
    private Long interviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", nullable = false)
    private Case caseMaster;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_status", nullable = false)
    private InterviewStatus interviewStatus = InterviewStatus.Not_Started;

    @Column(name = "outcome", length = 100)
    private String outcome;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "interviewed_by")
    private Long interviewedBy;

    @Column(name = "interviewed_at")
    private LocalDateTime interviewedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
