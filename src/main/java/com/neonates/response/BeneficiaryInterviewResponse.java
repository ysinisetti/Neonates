package com.neonates.response;

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
public class BeneficiaryInterviewResponse {
    private Long interviewId;
    private Long caseId;
    private InterviewStatus interviewStatus;
    private String outcome;
    private String notes;
    private Long interviewedBy;
    private LocalDateTime interviewedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
