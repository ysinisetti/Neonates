package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dim_case_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DimCaseStatus {

    @Id
    @Column(name = "status_code", length = 50)
    private String statusCode;

    @Column(name = "status_group", nullable = false, length = 50)
    private String statusGroup;

    @Column(name = "is_open_flag")
    private Boolean isOpenFlag = false;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "active_flag")
    private Boolean activeFlag = true;
}
