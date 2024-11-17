package com.example.cgmtest.service;

import com.example.cgmtest.dto.VisitDTO;

import java.util.List;

public interface VisitService {

    VisitDTO createVisit(Long patientId, VisitDTO visitDTO);

    VisitDTO getVisitById(Long visitId);

    List<VisitDTO> getVisitsByPatientId(Long patientId);

    VisitDTO updateVisit(Long visitId, VisitDTO visitDTO);

    VisitDTO updateVisitByPatient(Long patientId, Long visitId, VisitDTO visitDTO);

    void deleteVisit(Long visitId);
}