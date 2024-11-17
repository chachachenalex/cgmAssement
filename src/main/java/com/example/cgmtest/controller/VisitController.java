package com.example.cgmtest.controller;
import com.example.cgmtest.dto.VisitDTO;
import com.example.cgmtest.exception.PatientException;
import com.example.cgmtest.exception.VisitException;
import com.example.cgmtest.service.VisitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping("/patients/{patientId}")
    public ResponseEntity<Object> createVisit(@PathVariable Long patientId, @RequestBody VisitDTO visitDTO) {
        try {
            VisitDTO createdVisit = visitService.createVisit(patientId, visitDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVisit); // HTTP 201
        } catch (PatientException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // HTTP 404
        }
    }

    @GetMapping("/{visitId}")
    public ResponseEntity<Object> getVisitById(@PathVariable Long visitId) {
        try {
            VisitDTO visit = visitService.getVisitById(visitId);
            return ResponseEntity.ok(visit); // HTTP 200
        } catch (VisitException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // HTTP 404
        }
    }

    @GetMapping("/patients/{patientId}")
    public ResponseEntity<Object> getVisitsByPatientId(@PathVariable Long patientId) {
        try {
            List<VisitDTO> visits = visitService.getVisitsByPatientId(patientId);
            return ResponseEntity.ok(visits); // HTTP 200
        } catch (PatientException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // HTTP 404
        }
    }

    @PutMapping("/{visitId}")
    public ResponseEntity<Object> updateVisit(@PathVariable Long visitId, @RequestBody VisitDTO visitDTO) {
        try {
            VisitDTO updatedVisit = visitService.updateVisit(visitId, visitDTO);
            return ResponseEntity.ok(updatedVisit); // HTTP 200
        } catch (VisitException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // HTTP 404
        }
    }

    @PatchMapping("/patients/{patientId}/visits/{visitId}")
    public ResponseEntity<Object> updateVisitByPatient(
            @PathVariable Long patientId,
            @PathVariable Long visitId,
            @RequestBody VisitDTO visitDTO) {
        try {
            VisitDTO updatedVisit = visitService.updateVisitByPatient(patientId, visitId, visitDTO);
            return ResponseEntity.ok(updatedVisit); // HTTP 200
        } catch (PatientException | VisitException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // HTTP 404
        }
    }

    @DeleteMapping("/{visitId}")
    public ResponseEntity<Object> deleteVisit(@PathVariable Long visitId) {
        try {
            visitService.deleteVisit(visitId);
            return ResponseEntity.noContent().build(); // HTTP 204
        } catch (VisitException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // HTTP 404
        }
    }
}
