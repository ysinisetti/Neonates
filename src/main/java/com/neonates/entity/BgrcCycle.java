package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bgrc_cycle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BgrcCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bgrc_cycle_id")
    private Long bgrcCycleId;

    @Column(name = "cycle_month", nullable = false)
    private Integer cycleMonth;

    @Column(name = "cycle_year", nullable = false)
    private Integer cycleYear;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "utilization_document_id")
    private Long utilizationDocumentId;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
