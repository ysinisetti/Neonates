package com.neonates.entity;

import com.neonates.Enum.PanelType;
import com.neonates.Enum.Recommendation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "panel_review")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanelReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "panel_review_id")
    private Long panelReviewId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "panel_type", nullable = false)
    private PanelType panelType;

    @Column(name = "reviewer_user_id", nullable = false)
    private Long reviewerUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation", nullable = false)
    private Recommendation recommendation = Recommendation.Pending;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "supporting_document_id")
    private Long supportingDocumentId;

    @Column(name = "completed_flag")
    private Boolean completedFlag = false;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @UpdateTimestamp
    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;
}
