package com.example.cgmtest.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitDTO {
    private Long id;
    private LocalDateTime visitDateTime;
    private String type;
    private String reason;
    private String familyHistory;
    private Long patientId;

}
