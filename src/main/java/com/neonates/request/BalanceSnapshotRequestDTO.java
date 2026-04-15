package com.neonates.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceSnapshotRequestDTO {

    @NotNull(message = "Date ID is required")
    private Integer dateId;

    @DecimalMin(value = "0.0", message = "Bank amount cannot be negative")
    private BigDecimal bankAmount;

    @DecimalMin(value = "0.0", message = "FD amount cannot be negative")
    private BigDecimal fdAmount;

    private Long capturedBy;
}

