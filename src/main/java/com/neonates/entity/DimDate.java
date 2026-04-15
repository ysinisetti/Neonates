package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dim_date")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DimDate {

    @Id
    @Column(name = "date_id")
    private Integer dateId;

    @Column(name = "date", nullable = false, unique = true)
    private LocalDate date;

    @Column(name = "day", nullable = false)
    private Byte day;

    @Column(name = "month", nullable = false)
    private Byte month;

    @Column(name = "quarter", nullable = false)
    private Byte quarter;

    @Column(name = "year", nullable = false)
    private Short year;

    @Column(name = "fiscal_year", nullable = false)
    private Short fiscalYear;

    @Column(name = "fiscal_month", nullable = false)
    private Byte fiscalMonth;

    @Column(name = "is_month_end", nullable = false)
    private Boolean isMonthEnd = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
