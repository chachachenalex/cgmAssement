package com.example.cgmtest.service;

import com.example.cgmtest.dto.VisitDTO;
import com.example.cgmtest.entity.Patient;
import com.example.cgmtest.entity.Visit;
import com.example.cgmtest.exception.PatientException;
import com.example.cgmtest.exception.VisitException;
import com.example.cgmtest.dto.mapper.VisitMapper;
import com.example.cgmtest.repository.PatientRepository;
import com.example.cgmtest.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VisitServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private VisitRepository visitRepository;

    @Mock
    private VisitMapper visitMapper;

    @InjectMocks
    private VisitServiceImpl visitService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateVisit_Success() {
        // Arrange
        Long patientId = 1L;
        VisitDTO visitDTO = new VisitDTO();
        visitDTO.setVisitDateTime(LocalDateTime.now());
        visitDTO.setType("AT_HOME");
        visitDTO.setReason("FIRST_VISIT");
        visitDTO.setFamilyHistory("No known issues");

        Patient patient = new Patient();
        patient.setId(patientId);

        Visit visit = new Visit();
        Visit savedVisit = new Visit();
        savedVisit.setId(1L);

        VisitDTO savedVisitDTO = new VisitDTO();
        savedVisitDTO.setId(1L);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(visitMapper.toEntity(visitDTO)).thenReturn(visit);
        when(visitRepository.save(visit)).thenReturn(savedVisit);
        when(visitMapper.toDto(savedVisit)).thenReturn(savedVisitDTO);

        // Act
        VisitDTO result = visitService.createVisit(patientId, visitDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(patientRepository, times(1)).findById(patientId);
        verify(visitMapper, times(1)).toEntity(visitDTO);
        verify(visitRepository, times(1)).save(visit);
        verify(visitMapper, times(1)).toDto(savedVisit);
    }

    @Test
    void testCreateVisit_PatientNotFound() {
        // Arrange
        Long patientId = 1L;
        VisitDTO visitDTO = new VisitDTO();

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // Act & Assert
        PatientException exception = assertThrows(PatientException.class, () -> visitService.createVisit(patientId, visitDTO));
        assertEquals("Patient not found with id: " + patientId, exception.getMessage());

        verify(patientRepository, times(1)).findById(patientId);
        verifyNoInteractions(visitMapper);
        verify(visitRepository, never()).save(any());
    }

    @Test
    void testGetVisitById_Success() {
        // Arrange
        Long visitId = 1L;
        Visit visit = new Visit();
        visit.setId(visitId);

        VisitDTO visitDTO = new VisitDTO();
        visitDTO.setId(visitId);

        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));
        when(visitMapper.toDto(visit)).thenReturn(visitDTO);

        // Act
        VisitDTO result = visitService.getVisitById(visitId);

        // Assert
        assertNotNull(result);
        assertEquals(visitId, result.getId());

        verify(visitRepository, times(1)).findById(visitId);
        verify(visitMapper, times(1)).toDto(visit);
    }

    @Test
    void testGetVisitById_NotFound() {
        // Arrange
        Long visitId = 1L;

        when(visitRepository.findById(visitId)).thenReturn(Optional.empty());

        // Act & Assert
        VisitException exception = assertThrows(VisitException.class, () -> visitService.getVisitById(visitId));
        assertEquals("Visit not found with id: " + visitId, exception.getMessage());

        verify(visitRepository, times(1)).findById(visitId);
        verifyNoInteractions(visitMapper);
    }

    @Test
    void testGetVisitsByPatientId_Success() {
        // Arrange
        Long patientId = 1L;

        Patient patient = new Patient();
        patient.setId(patientId);

        Visit visit = new Visit();
        visit.setId(1L);

        VisitDTO visitDTO = new VisitDTO();
        visitDTO.setId(1L);

        when(patientRepository.existsById(patientId)).thenReturn(true);
        when(visitRepository.findByPatientId(patientId)).thenReturn(Collections.singletonList(visit));
        when(visitMapper.toDto(visit)).thenReturn(visitDTO);

        // Act
        List<VisitDTO> result = visitService.getVisitsByPatientId(patientId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());

        verify(patientRepository, times(1)).existsById(patientId);
        verify(visitRepository, times(1)).findByPatientId(patientId);
        verify(visitMapper, times(1)).toDto(visit);
    }

    @Test
    void testGetVisitsByPatientId_PatientNotFound() {
        // Arrange
        Long patientId = 1L;

        when(patientRepository.existsById(patientId)).thenReturn(false);

        // Act & Assert
        PatientException exception = assertThrows(PatientException.class, () -> visitService.getVisitsByPatientId(patientId));
        assertEquals("Patient not found with id: " + patientId, exception.getMessage());

        verify(patientRepository, times(1)).existsById(patientId);
        verifyNoInteractions(visitRepository);
        verifyNoInteractions(visitMapper);
    }

    @Test
    void testDeleteVisit_Success() {
        // Arrange
        Long visitId = 1L;
        Visit visit = new Visit();
        visit.setId(visitId);

        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));

        // Act
        visitService.deleteVisit(visitId);

        // Assert
        verify(visitRepository, times(1)).findById(visitId);
        verify(visitRepository, times(1)).delete(visit);
    }

    @Test
    void testDeleteVisit_NotFound() {
        // Arrange
        Long visitId = 1L;

        when(visitRepository.findById(visitId)).thenReturn(Optional.empty());

        // Act & Assert
        VisitException exception = assertThrows(VisitException.class, () -> visitService.deleteVisit(visitId));
        assertEquals("Visit not found with id: " + visitId, exception.getMessage());

        verify(visitRepository, times(1)).findById(visitId);
        verify(visitRepository, never()).delete(any());
    }
}
