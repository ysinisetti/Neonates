package com.neonates.entity;

import com.neonates.Enum.HospitalType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "hospital")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_id")
    private Long hospitalId;

    @Column(name = "hospital_uuid", unique = true, nullable = false, length = 36)
    private String hospitalUuid;

    @Column(name = "hospital_name", nullable = false, length = 200)
    private String hospitalName;

    @Enumerated(EnumType.STRING)
    @Column(name = "hospital_type", nullable = false)
    private HospitalType hospitalType;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "spoc_name", length = 150)
    private String spocName;

    @Column(name = "spoc_phone", length = 30)
    private String spocPhone;

    @Column(name = "active_flag")
    private Boolean activeFlag = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (hospitalUuid == null) {
            hospitalUuid = UUID.randomUUID().toString();
        }
    }
}
