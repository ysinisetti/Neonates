package com.neonates.request;

import com.neonates.Enum.InterviewStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryInterviewCreateRequest {
    private Long caseId;
    private InterviewStatus interviewStatus;
    private String outcome;
    private String notes;
    private Long interviewedBy;
    private LocalDateTime interviewedAt;
}
