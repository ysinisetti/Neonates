package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fact_case_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactCaseEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Column(name = "hospital_id", nullable = false)
    private Long hospitalId;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "event_date_id", nullable = false)
    private Integer eventDateId;

    @Column(name = "event_ts", nullable = false)
    private LocalDateTime eventTs;

    @Column(name = "status_code", length = 50)
    private String statusCode;

    @Column(name = "amount_value", precision = 12, scale = 2)
    private BigDecimal amountValue;

    @Column(name = "details_json", columnDefinition = "JSON")
    private String detailsJson;
}
