package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bgrc_cycle_case_map")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BgrcCycleCaseMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_id")
    private Long mapId;

    @Column(name = "bgrc_cycle_id", nullable = false)
    private Long bgrcCycleId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
