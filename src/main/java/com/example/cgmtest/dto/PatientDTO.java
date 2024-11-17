package com.example.cgmtest.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
public class PatientDTO {
    private Long id;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String socialSecurityNumber;
    private List<VisitDTO> visits;
}