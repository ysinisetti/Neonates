package com.neonates.response;

import com.neonates.Enum.BplAplStatus;
import com.neonates.Enum.IncomeProofType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FinancialCaseResponse {

    private Long financialId;
    private Long caseId;
    private IncomeProofType incomeProofType;
    private Integer bankStatementMonthsCount;
    private String economicCardType;
    private BplAplStatus bplAplStatus;
    private BigDecimal advancePaidAmount;
    private BigDecimal medicalBillEstimate;
    private Integer estimatedNicuDays;
    private BigDecimal hospitalDiscountAmount;
    private BigDecimal beneficiaryPayableBalance;
    private BigDecimal reductionAmount;
    private String reductionNotes;
    private BigDecimal sponsorAmountQuantified;
    private BigDecimal sponsorAmountFinal;
    private BigDecimal totalAmountApproved;
    private String financialSummarySnapshot;
    private String manualExceptionNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
