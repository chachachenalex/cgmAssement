package com.example.cgmtest.dto.mapper;

import com.example.cgmtest.dto.PatientDTO;
import com.example.cgmtest.dto.VisitDTO;
import com.example.cgmtest.entity.Patient;
import com.example.cgmtest.entity.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PatientMapper {
    @Autowired
    public VisitMapper visitMapper;

    public  PatientDTO toDto(Patient patient) {
        if (patient == null) {
            return null;
        }

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(patient.getId());
        patientDTO.setName(patient.getName());
        patientDTO.setSurname(patient.getSurname());
        patientDTO.setDateOfBirth(patient.getDateOfBirth());
        patientDTO.setSocialSecurityNumber(patient.getSocialSecurityNumber());

        // map from visitList to visits
        if (patient.getVisitList() != null) {
            List<VisitDTO> visitDTOList = patient.getVisitList().stream()
                    .map(visitMapper::toDto)
                    .collect(Collectors.toList());
            patientDTO.setVisits(visitDTOList);
        }

        return patientDTO;
    }

    public  Patient toEntity(PatientDTO patientDTO) {
        if (patientDTO == null) {
            return null;
        }

        Patient patient = new Patient();
        patient.setId(patientDTO.getId());
        patient.setName(patientDTO.getName());
        patient.setSurname(patientDTO.getSurname());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        patient.setSocialSecurityNumber(patientDTO.getSocialSecurityNumber());

        // map from visits to visitList
        if (patientDTO.getVisits() != null) {
            List<Visit> visitList = patientDTO.getVisits().stream()
                    .map(visitMapper::toEntity)
                    .peek(visit -> visit.setPatient(patient))
                    .collect(Collectors.toList());
            patient.setVisitList(visitList);
        }

        return patient;
    }
}
