package com.neonates.entity;

import com.neonates.Enum.ConsentFormType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "case_consent_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseConsentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consent_id")
    private Long consentId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "consent_form_type", nullable = false)
    private ConsentFormType consentFormType;

    @Column(name = "consent_document_id", nullable = false)
    private Long consentDocumentId;

    @Column(name = "consent_language_code", length = 10)
    private String consentLanguageCode;

    @Column(name = "consent_obtained_flag")
    private Boolean consentObtainedFlag = false;

    @Column(name = "consent_obtained_at")
    private LocalDateTime consentObtainedAt;

    @Column(name = "consent_obtained_by")
    private Long consentObtainedBy;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
