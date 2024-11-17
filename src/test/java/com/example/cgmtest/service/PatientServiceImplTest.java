package com.example.cgmtest.service;

import com.example.cgmtest.dto.PatientDTO;
import com.example.cgmtest.entity.Patient;
import com.example.cgmtest.exception.PatientException;
import com.example.cgmtest.dto.mapper.PatientMapper;
import com.example.cgmtest.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPatients() {
        // Arrange
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("John");
        patient.setSurname("Doe");
        patient.setDateOfBirth(LocalDate.of(1980, 1, 1));
        patient.setSocialSecurityNumber("123-45-6789");

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        patientDTO.setName("John");
        patientDTO.setSurname("Doe");
        patientDTO.setDateOfBirth(LocalDate.of(1980, 1, 1));
        patientDTO.setSocialSecurityNumber("123-45-6789");

        when(patientRepository.findAll()).thenReturn(Collections.singletonList(patient));
        when(patientMapper.toDto(patient)).thenReturn(patientDTO);

        // Act
        List<PatientDTO> result = patientService.getAllPatients();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(patientDTO, result.get(0));

        verify(patientRepository, times(1)).findAll();
        verify(patientMapper, times(1)).toDto(patient);
    }

    @Test
    void testGetPatientById_Success() {
        // Arrange
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("John");

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        patientDTO.setName("John");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientMapper.toDto(patient)).thenReturn(patientDTO);

        // Act
        PatientDTO result = patientService.getPatientById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getName());

        verify(patientRepository, times(1)).findById(1L);
        verify(patientMapper, times(1)).toDto(patient);
    }

    @Test
    void testGetPatientById_NotFound() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> patientService.getPatientById(1L));
        assertEquals("Patient not found with id: 1", exception.getMessage());

        verify(patientRepository, times(1)).findById(1L);
        verifyNoInteractions(patientMapper);
    }

    @Test
    void testCreatePatient_Success() {
        // Arrange
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setSocialSecurityNumber("123-45-6789");

        Patient patient = new Patient();
        patient.setSocialSecurityNumber("123-45-6789");

        Patient savedPatient = new Patient();
        savedPatient.setId(1L);
        savedPatient.setSocialSecurityNumber("123-45-6789");

        PatientDTO savedPatientDTO = new PatientDTO();
        savedPatientDTO.setId(1L);
        savedPatientDTO.setSocialSecurityNumber("123-45-6789");

        when(patientRepository.findBySocialSecurityNumber("123-45-6789")).thenReturn(null);
        when(patientMapper.toEntity(patientDTO)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(savedPatient);
        when(patientMapper.toDto(savedPatient)).thenReturn(savedPatientDTO);

        // Act
        PatientDTO result = patientService.createPatient(patientDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("123-45-6789", result.getSocialSecurityNumber());

        verify(patientRepository, times(1)).findBySocialSecurityNumber("123-45-6789");
        verify(patientMapper, times(1)).toEntity(patientDTO);
        verify(patientRepository, times(1)).save(patient);
        verify(patientMapper, times(1)).toDto(savedPatient);
    }

    @Test
    void testCreatePatient_AlreadyExists() {
        // Arrange
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setSocialSecurityNumber("123-45-6789");

        Patient existingPatient = new Patient();
        existingPatient.setSocialSecurityNumber("123-45-6789");

        when(patientRepository.findBySocialSecurityNumber("123-45-6789")).thenReturn(existingPatient);

        // Act & Assert
        PatientException exception = assertThrows(PatientException.class, () -> patientService.createPatient(patientDTO));
        assertEquals("Patient with social security number 123-45-6789 already exists.", exception.getMessage());

        verify(patientRepository, times(1)).findBySocialSecurityNumber("123-45-6789");
        verifyNoInteractions(patientMapper);
        verify(patientRepository, never()).save(any());
    }

    @Test
    void testUpdatePatient_Success() {
        // Arrange
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName("John Updated");

        Patient existingPatient = new Patient();
        existingPatient.setId(1L);
        existingPatient.setName("John");

        Patient updatedPatient = new Patient();
        updatedPatient.setId(1L);
        updatedPatient.setName("John Updated");

        PatientDTO updatedPatientDTO = new PatientDTO();
        updatedPatientDTO.setId(1L);
        updatedPatientDTO.setName("John Updated");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(existingPatient)).thenReturn(updatedPatient);
        when(patientMapper.toDto(updatedPatient)).thenReturn(updatedPatientDTO);

        // Act
        PatientDTO result = patientService.updatePatient(1L, patientDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John Updated", result.getName());

        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(existingPatient);
        verify(patientMapper, times(1)).toDto(updatedPatient);
    }

    @Test
    void testDeletePatient_Success() {
        // Arrange
        when(patientRepository.existsById(1L)).thenReturn(true);

        // Act
        patientService.deletePatient(1L);

        // Assert
        verify(patientRepository, times(1)).existsById(1L);
        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePatient_NotFound() {
        // Arrange
        when(patientRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        PatientException exception = assertThrows(PatientException.class, () -> patientService.deletePatient(1L));
        assertEquals("Patient not found with id: 1", exception.getMessage());

        verify(patientRepository, times(1)).existsById(1L);
        verify(patientRepository, never()).deleteById(anyLong());
    }
}
