package com.example.cgmtest.controller;

import com.example.cgmtest.dto.VisitDTO;
import com.example.cgmtest.exception.PatientException;
import com.example.cgmtest.exception.VisitException;
import com.example.cgmtest.service.VisitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VisitController.class)
class VisitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VisitService visitService;

    @Autowired
    private ObjectMapper objectMapper;

    private VisitDTO visitDTO;

    @BeforeEach
    void setUp() {
        visitDTO = new VisitDTO();
        visitDTO.setId(1L);
        visitDTO.setVisitDateTime(LocalDateTime.now());
        visitDTO.setType("AT_HOME");
        visitDTO.setReason("FIRST_VISIT");
        visitDTO.setFamilyHistory("No known issues");
        visitDTO.setPatientId(1L);
    }

    @Test
    void testCreateVisit_Success() throws Exception {
        Mockito.when(visitService.createVisit(anyLong(), any(VisitDTO.class))).thenReturn(visitDTO);

        mockMvc.perform(post("/api/visits/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visitDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(visitDTO.getId()))
                .andExpect(jsonPath("$.type").value(visitDTO.getType()));
    }

    @Test
    void testCreateVisit_PatientNotFound() throws Exception {
        Mockito.when(visitService.createVisit(anyLong(), any(VisitDTO.class))).thenThrow(new PatientException("Patient not found"));

        mockMvc.perform(post("/api/visits/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visitDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Patient not found"));
    }

    @Test
    void testGetVisitById_Success() throws Exception {
        Mockito.when(visitService.getVisitById(anyLong())).thenReturn(visitDTO);

        mockMvc.perform(get("/api/visits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(visitDTO.getId()))
                .andExpect(jsonPath("$.reason").value(visitDTO.getReason()));
    }

    @Test
    void testGetVisitById_NotFound() throws Exception {
        Mockito.when(visitService.getVisitById(anyLong())).thenThrow(new VisitException("Visit not found"));

        mockMvc.perform(get("/api/visits/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Visit not found"));
    }

    @Test
    void testGetVisitsByPatientId_Success() throws Exception {
        List<VisitDTO> visits = Arrays.asList(visitDTO);
        Mockito.when(visitService.getVisitsByPatientId(anyLong())).thenReturn(visits);

        mockMvc.perform(get("/api/visits/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(visitDTO.getId()));
    }

    @Test
    void testGetVisitsByPatientId_PatientNotFound() throws Exception {
        Mockito.when(visitService.getVisitsByPatientId(anyLong())).thenThrow(new PatientException("Patient not found"));

        mockMvc.perform(get("/api/visits/patients/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Patient not found"));
    }

    @Test
    void testUpdateVisit_Success() throws Exception {
        VisitDTO updatedVisit = visitDTO;
        updatedVisit.setReason("RECURRING");
        Mockito.when(visitService.updateVisit(anyLong(), any(VisitDTO.class))).thenReturn(updatedVisit);

        mockMvc.perform(put("/api/visits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedVisit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reason").value("RECURRING"));
    }

    @Test
    void testUpdateVisit_NotFound() throws Exception {
        Mockito.when(visitService.updateVisit(anyLong(), any(VisitDTO.class))).thenThrow(new VisitException("Visit not found"));

        mockMvc.perform(put("/api/visits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visitDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Visit not found"));
    }

    @Test
    void testUpdateVisitByPatient_Success() throws Exception {
        VisitDTO updatedVisit = visitDTO;
        updatedVisit.setReason("URGENT");
        Mockito.when(visitService.updateVisitByPatient(anyLong(), anyLong(), any(VisitDTO.class))).thenReturn(updatedVisit);

        mockMvc.perform(patch("/api/visits/patients/1/visits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedVisit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reason").value("URGENT"));
    }

    @Test
    void testDeleteVisit_Success() throws Exception {
        mockMvc.perform(delete("/api/visits/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(visitService, Mockito.times(1)).deleteVisit(anyLong());
    }

    @Test
    void testDeleteVisit_NotFound() throws Exception {
        Mockito.doThrow(new VisitException("Visit not found")).when(visitService).deleteVisit(anyLong());

        mockMvc.perform(delete("/api/visits/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Visit not found"));
    }
}
