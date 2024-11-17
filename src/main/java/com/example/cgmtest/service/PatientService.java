package com.example.cgmtest.service;

import com.example.cgmtest.dto.PatientDTO;

import java.util.List;

public interface PatientService {
    List<PatientDTO> getAllPatients();
    PatientDTO getPatientById(Long id);
    PatientDTO createPatient(PatientDTO patientDTO);
    PatientDTO updatePatient(Long id, PatientDTO patientDTO);
    void deletePatient(Long id);
}
