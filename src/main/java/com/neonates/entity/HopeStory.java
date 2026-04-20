package com.neonates.entity;

import com.neonates.Enum.HopeStoryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "hope_story")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HopeStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hope_story_id")
    private Long hopeStoryId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private HopeStoryStatus status = HopeStoryStatus.Not_Started;

    @Column(name = "internal_summary_export_id")
    private Long internalSummaryExportId;

    @Column(name = "story_link", length = 500)
    private String storyLink;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
