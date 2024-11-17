package com.example.cgmtest.service;

import com.example.cgmtest.dto.PatientDTO;
import com.example.cgmtest.dto.mapper.PatientMapper;
import com.example.cgmtest.entity.Patient;
import com.example.cgmtest.exception.PatientException;
import com.example.cgmtest.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    //@Autowired
    private final PatientRepository patientRepository;

    //@Autowired
    private final PatientMapper patientMapper;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository,PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        return patientMapper.toDto(patient);
    }

    @Override
    public PatientDTO createPatient(PatientDTO patientDTO) {
        Patient existingPatient = patientRepository.findBySocialSecurityNumber(patientDTO.getSocialSecurityNumber());
        if (existingPatient != null) {
            throw new PatientException("Patient with social security number "
                    + patientDTO.getSocialSecurityNumber()
                    + " already exists.");
        }

        Patient patient = patientMapper.toEntity(patientDTO);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toDto(savedPatient);
    }

    @Override
    public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientException("Patient not found with id: " + id));

        existingPatient.setName(patientDTO.getName());
        existingPatient.setSurname(patientDTO.getSurname());
        existingPatient.setDateOfBirth(patientDTO.getDateOfBirth());
        existingPatient.setSocialSecurityNumber(patientDTO.getSocialSecurityNumber());

        Patient updatedPatient = patientRepository.save(existingPatient);
        return patientMapper.toDto(updatedPatient);
    }

    @Override
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }
}