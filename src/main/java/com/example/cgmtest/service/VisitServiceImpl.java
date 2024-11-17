package com.example.cgmtest.service;

import com.example.cgmtest.dto.VisitDTO;
import com.example.cgmtest.dto.mapper.VisitMapper;
import com.example.cgmtest.entity.Patient;
import com.example.cgmtest.entity.Visit;
import com.example.cgmtest.entity.enumType.VisitReason;
import com.example.cgmtest.entity.enumType.VisitType;
import com.example.cgmtest.exception.PatientException;
import com.example.cgmtest.exception.VisitException;
import com.example.cgmtest.repository.PatientRepository;
import com.example.cgmtest.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitServiceImpl implements VisitService {
    @Autowired
    private final PatientRepository patientRepository;
    @Autowired
    private final VisitRepository visitRepository;
    @Autowired
    private final VisitMapper visitMapper;

    public VisitServiceImpl(PatientRepository patientRepository, VisitRepository visitRepository, VisitMapper visitMapper) {
        this.patientRepository = patientRepository;
        this.visitRepository = visitRepository;
        this.visitMapper = visitMapper;
    }


    @Override
    public VisitDTO createVisit(Long patientId, VisitDTO visitDTO) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientException("Patient not found with id: " + patientId));

        Visit visit = visitMapper.toEntity(visitDTO);
        visit.setPatient(patient);

        Visit savedVisit = visitRepository.save(visit);
        return visitMapper.toDto(savedVisit);
    }

    @Override
    public VisitDTO getVisitById(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitException("Visit not found with id: " + visitId));

        return visitMapper.toDto(visit);
    }

    @Override
    public List<VisitDTO> getVisitsByPatientId(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new PatientException("Patient not found with id: " + patientId);
        }

        List<Visit> visits = visitRepository.findByPatientId(patientId);
        return visits.stream()
                .map(visitMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public VisitDTO updateVisit(Long visitId, VisitDTO visitDTO) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitException("Visit not found with id: " + visitId));

        visit.setVisitDateTime(visitDTO.getVisitDateTime());
        visit.setType(VisitType.valueOf(visitDTO.getType()));
        visit.setReason(VisitReason.valueOf(visitDTO.getReason()));
        visit.setFamilyHistory(visitDTO.getFamilyHistory());

        Visit updatedVisit = visitRepository.save(visit);
        return visitMapper.toDto(updatedVisit);
    }

    @Override
    public VisitDTO updateVisitByPatient(Long patientId, Long visitId, VisitDTO visitDTO) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientException("Patient not found with id: " + patientId));

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitException("Visit not found with id: " + visitId));

        if (!visit.getPatient().getId().equals(patientId)) {
            throw new VisitException("Visit with id: " + visitId + " does not belong to Patient with id: " + patientId);
        }

        visit.setVisitDateTime(visitDTO.getVisitDateTime());
        visit.setType(VisitType.valueOf(visitDTO.getType()));
        visit.setReason(VisitReason.valueOf(visitDTO.getReason()));
        visit.setFamilyHistory(visitDTO.getFamilyHistory());

        Visit updatedVisit = visitRepository.save(visit);
        return visitMapper.toDto(updatedVisit);
    }

    @Override
    public void deleteVisit(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitException("Visit not found with id: " + visitId));

        visitRepository.delete(visit);
    }
}