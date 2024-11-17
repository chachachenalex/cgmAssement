package com.example.cgmtest.controller;

import com.example.cgmtest.dto.PatientDTO;
import com.example.cgmtest.exception.PatientException;
import com.example.cgmtest.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients); // HTTP 200
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPatientById(@PathVariable Long id) {
        try {
            PatientDTO patient = patientService.getPatientById(id);
            return ResponseEntity.ok(patient); // HTTP 200
        } catch (PatientException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // HTTP 404
        }
    }

    @PostMapping
    public ResponseEntity<Object> createPatient(@RequestBody PatientDTO patientDTO) {
        try {
            PatientDTO createdPatient = patientService.createPatient(patientDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient); // HTTP 201
        } catch (PatientException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // HTTP 409
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePatient(@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        try {
            PatientDTO updatedPatient = patientService.updatePatient(id, patientDTO);
            return ResponseEntity.ok(updatedPatient); // HTTP 200
        } catch (PatientException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // HTTP 404
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build(); // HTTP 204
        } catch (PatientException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // HTTP 404
        }
    }
}
