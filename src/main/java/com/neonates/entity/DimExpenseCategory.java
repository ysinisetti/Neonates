package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dim_expense_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DimExpenseCategory {

    @Id
    @Column(name = "category_code", length = 50)
    private String categoryCode;

    @Column(name = "subcategory", length = 100)
    private String subcategory;

    @Column(name = "cost_center", length = 100)
    private String costCenter;

    @Column(name = "active_flag")
    private Boolean activeFlag = true;
}
