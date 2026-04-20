package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dim_donor_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DimDonorCategory {

    @Id
    @Column(name = "donor_category_code", length = 50)
    private String donorCategoryCode;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "active_flag")
    private Boolean activeFlag = true;
}
