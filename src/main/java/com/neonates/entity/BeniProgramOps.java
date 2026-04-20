package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "beni_program_ops")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeniProgramOps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "beni_ops_id")
    private Long beniOpsId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Column(name = "volunteer_lead_user_id")
    private Long volunteerLeadUserId;

    @Column(name = "assigned_volunteer_user_id")
    private Long assignedVolunteerUserId;

    @Column(name = "beni_team_member_allotted", length = 150)
    private String beniTeamMemberAllotted;

    @Column(name = "spoc_name", length = 150)
    private String spocName;

    @Column(name = "spoc_number", length = 30)
    private String spocNumber;

    @Column(name = "pre_discharge_contact_flag")
    private Boolean preDischargeContactFlag = false;

    @Column(name = "pre_discharge_contact_at")
    private LocalDateTime preDischargeContactAt;

    @Column(name = "spoc_contacted_flag")
    private Boolean spocContactedFlag = false;

    @Column(name = "spoc_contacted_at")
    private LocalDateTime spocContactedAt;

    @Column(name = "parent_contacted_flag")
    private Boolean parentContactedFlag = false;

    @Column(name = "parent_contacted_at")
    private LocalDateTime parentContactedAt;

    @Column(name = "baby_reached_home_flag")
    private Boolean babyReachedHomeFlag = false;

    @Column(name = "baby_reached_home_at")
    private LocalDateTime babyReachedHomeAt;

    @Column(name = "hamper_sent_date")
    private LocalDate hamperSentDate;

    @Column(name = "hamper_status", length = 100)
    private String hamperStatus;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
