package com.neonates.entity;

import com.neonates.Enum.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "app_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_uuid", unique = true, length = 36)
    private String userUuid;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "email", unique = true, nullable = false, length = 200)
    private String email;

    @Column(name = "phone", length = 30)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_role", nullable = false)
    private UserRole primaryRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column(name = "ui_language_code", length = 10)
    private String uiLanguageCode = "en";

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
        if (userUuid == null) {
            userUuid = UUID.randomUUID().toString();
        }
        if (uiLanguageCode == null) {
            uiLanguageCode = "en";
        }
    }
}
