package com.neonates.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BalanceSnapshotResponseDTO {
    private Long balanceSnapshotId;
    private Integer dateId;
    private BigDecimal bankAmount;
    private BigDecimal fdAmount;
    private Long capturedBy;
    private LocalDateTime createdAt;
}
