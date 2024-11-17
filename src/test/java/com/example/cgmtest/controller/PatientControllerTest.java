package com.example.cgmtest.controller;

import com.example.cgmtest.dto.PatientDTO;
import com.example.cgmtest.exception.PatientException;
import com.example.cgmtest.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllPatients_Success() throws Exception {
        // Arrange
        PatientDTO patient1 = new PatientDTO();
        patient1.setId(1L);
        patient1.setName("John");
        patient1.setSurname("Doe");
        patient1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient1.setSocialSecurityNumber("123-45-6789");

        PatientDTO patient2 = new PatientDTO();
        patient2.setId(2L);
        patient2.setName("Jane");
        patient2.setSurname("Smith");
        patient2.setDateOfBirth(LocalDate.of(1995, 5, 5));
        patient2.setSocialSecurityNumber("987-65-4321");

        List<PatientDTO> patients = Arrays.asList(patient1, patient2);
        when(patientService.getAllPatients()).thenReturn(patients);

        // Act & Assert
        mockMvc.perform(get("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].name").value("Jane"));

        verify(patientService, times(1)).getAllPatients();
    }

    @Test
    void testGetPatientById_Success() throws Exception {
        // Arrange
        PatientDTO patient = new PatientDTO();
        patient.setId(1L);
        patient.setName("John");
        patient.setSurname("Doe");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setSocialSecurityNumber("123-45-6789");

        when(patientService.getPatientById(1L)).thenReturn(patient);

        // Act & Assert
        mockMvc.perform(get("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"));

        verify(patientService, times(1)).getPatientById(1L);
    }

    @Test
    void testGetPatientById_NotFound() throws Exception {
        // Arrange
        when(patientService.getPatientById(99L)).thenThrow(new PatientException("Patient not found with id: 99"));

        // Act & Assert
        mockMvc.perform(get("/api/patients/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Patient not found with id: 99"));

        verify(patientService, times(1)).getPatientById(99L);
    }

    @Test
    void testCreatePatient_Success() throws Exception {
        // Arrange
        ObjectMapper customObjectMapper = new ObjectMapper();
        customObjectMapper.registerModule(new JavaTimeModule());
        customObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        PatientDTO patient = new PatientDTO();
        patient.setId(1L);
        patient.setName("John");
        patient.setSurname("Doe");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setSocialSecurityNumber("123-45-6789");

        when(patientService.createPatient(Mockito.any(PatientDTO.class))).thenReturn(patient);

        // Act & Assert
        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customObjectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"));

        verify(patientService, times(1)).createPatient(Mockito.any(PatientDTO.class));
    }

    @Test
    void testCreatePatient_Conflict() throws Exception {
        // Arrange
        ObjectMapper customObjectMapper = new ObjectMapper();
        customObjectMapper.registerModule(new JavaTimeModule());
        customObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        PatientDTO patient = new PatientDTO();
        patient.setName("John");
        patient.setSurname("Doe");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setSocialSecurityNumber("123-45-6789");

        when(patientService.createPatient(Mockito.any(PatientDTO.class)))
                .thenThrow(new PatientException("Patient with social security number already exists."));

        // Act & Assert
        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customObjectMapper.writeValueAsString(patient)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Patient with social security number already exists."));

        verify(patientService, times(1)).createPatient(Mockito.any(PatientDTO.class));
    }

    @Test
    void testDeletePatient_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNoContent());

        verify(patientService, times(1)).deletePatient(1L);
    }

    @Test
    void testDeletePatient_NotFound() throws Exception {
        // Arrange
        doThrow(new PatientException("Patient not found with id: 99")).when(patientService).deletePatient(99L);

        // Act & Assert
        mockMvc.perform(delete("/api/patients/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Patient not found with id: 99"));

        verify(patientService, times(1)).deletePatient(99L);
    }
}
