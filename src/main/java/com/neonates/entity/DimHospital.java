package com.neonates.entity;

import com.neonates.Enum.ProcessType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dim_hospital")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DimHospital {

    @Id
    @Column(name = "hospital_id")
    private Long hospitalId;

    @Column(name = "hospital_name", nullable = false, length = 200)
    private String hospitalName;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_type", nullable = false)
    private ProcessType processType;

    @Column(name = "active_flag")
    private Boolean activeFlag = true;

    @CreationTimestamp
    @Column(name = "synced_at", updatable = false)
    private LocalDateTime syncedAt;
}
