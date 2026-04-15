package com.neonates.request;

import com.neonates.Enum.BplAplStatus;
import com.neonates.Enum.IncomeProofType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinancialCaseCreateRequest {

    @NotNull(message = "Case ID is mandatory")
    private Long caseId;

    private IncomeProofType incomeProofType;

    private Integer bankStatementMonthsCount;

    private String economicCardType;

    private BplAplStatus bplAplStatus;

    @DecimalMin(value = "0.0", inclusive = true, message = "Advance paid amount must be non-negative")
    private BigDecimal advancePaidAmount;

    @DecimalMin(value = "0.0", inclusive = true, message = "Medical bill estimate must be non-negative")
    private BigDecimal medicalBillEstimate;

    private Integer estimatedNicuDays;

    @DecimalMin(value = "0.0", inclusive = true, message = "Hospital discount amount must be non-negative")
    private BigDecimal hospitalDiscountAmount;

    @DecimalMin(value = "0.0", inclusive = true, message = "Reduction amount must be non-negative")
    private BigDecimal reductionAmount;

    private String reductionNotes;

    @DecimalMin(value = "0.0", inclusive = true, message = "Sponsor amount quantified must be non-negative")
    private BigDecimal sponsorAmountQuantified;

    @DecimalMin(value = "0.0", inclusive = true, message = "Sponsor amount final must be non-negative")
    private BigDecimal sponsorAmountFinal;

    private String financialSummarySnapshot;

    private String manualExceptionNotes;
}
