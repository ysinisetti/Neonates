package com.neonates.entity;

import com.neonates.Enum.ReferenceAmountBasis;
import com.neonates.Enum.SponsorProgram;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sponsor_quantification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SponsorQuantification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quantification_id")
    private Long quantificationId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "sponsor_program", nullable = false)
    private SponsorProgram sponsorProgram;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_amount_basis", nullable = false)
    private ReferenceAmountBasis referenceAmountBasis;

    @Column(name = "recommended_sponsor_amount", precision = 12, scale = 2)
    private BigDecimal recommendedSponsorAmount;

    @Column(name = "justification_notes", columnDefinition = "TEXT")
    private String justificationNotes;

    @Column(name = "reference_document_id")
    private Long referenceDocumentId;

    @Column(name = "quantified_by")
    private Long quantifiedBy;

    @Column(name = "quantified_at")
    private LocalDateTime quantifiedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
