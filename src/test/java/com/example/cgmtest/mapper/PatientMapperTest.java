package com.example.cgmtest.mapper;

import com.example.cgmtest.dto.PatientDTO;
import com.example.cgmtest.dto.VisitDTO;
import com.example.cgmtest.dto.mapper.PatientMapper;
import com.example.cgmtest.dto.mapper.VisitMapper;
import com.example.cgmtest.entity.Patient;
import com.example.cgmtest.entity.Visit;
import com.example.cgmtest.entity.enumType.VisitReason;
import com.example.cgmtest.entity.enumType.VisitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PatientMapperTest {

    private PatientMapper patientMapper;
    private VisitMapper visitMapper;

    @BeforeEach
    public void setUp() {
        visitMapper = new VisitMapper();
        patientMapper = new PatientMapper();
        patientMapper.visitMapper = visitMapper;
    }

    @Test
    public void testPatientToDtoMapping() {
        //when
        Visit visit1 = Visit.builder()
                .id(1L)
                .visitDateTime(LocalDateTime.now())
                .type(VisitType.AT_HOME)
                .reason(VisitReason.FIRST_VISIT)
                .familyHistory("No family history")
                .build();

        Visit visit2 = Visit.builder()
                .id(2L)
                .visitDateTime(LocalDateTime.now().plusDays(1))
                .type(VisitType.AT_DOCTOR_OFFICE)
                .reason(VisitReason.RECURRING)
                .familyHistory("Some family history")
                .build();

        Patient patient = Patient.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .dateOfBirth(LocalDate.of(1980, 1, 1))
                .socialSecurityNumber("123-45-6789")
                .visitList(Arrays.asList(visit1, visit2))
                .build();
        //then
        PatientDTO patientDTO = patientMapper.toDto(patient);

        //assert
        assertEquals(patient.getId(), patientDTO.getId());
        assertEquals(patient.getName(), patientDTO.getName());
        assertEquals(patient.getSurname(), patientDTO.getSurname());
        assertEquals(patient.getDateOfBirth(), patientDTO.getDateOfBirth());
        assertEquals(patient.getSocialSecurityNumber(), patientDTO.getSocialSecurityNumber());
        assertEquals(patient.getVisitList().size(), patientDTO.getVisits().size());
        assertEquals(patient.getVisitList().get(0).getId(), patientDTO.getVisits().get(0).getId());
        assertEquals(patient.getVisitList().get(0).getType().name(), patientDTO.getVisits().get(0).getType());
    }

    @Test
    void testDtoToEntityMapping() {
        // Arrange: Create PatientDTO with nested VisitDTO list
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        patientDTO.setName("Jane");
        patientDTO.setSurname("Smith");
        patientDTO.setDateOfBirth(LocalDate.of(1990, 5, 15));
        patientDTO.setSocialSecurityNumber("987-65-4321");

        VisitDTO visitDTO1 = new VisitDTO();
        visitDTO1.setId(1L);
        visitDTO1.setVisitDateTime(LocalDateTime.now());
        visitDTO1.setType("AT_HOME");
        visitDTO1.setReason("FIRST_VISIT");
        visitDTO1.setFamilyHistory("No known issues");

        VisitDTO visitDTO2 = new VisitDTO();
        visitDTO2.setId(2L);
        visitDTO2.setVisitDateTime(LocalDateTime.now().plusDays(2));
        visitDTO2.setType("AT_DOCTOR_OFFICE");
        visitDTO2.setReason("RECURRING");
        visitDTO2.setFamilyHistory("Some family history");

        patientDTO.setVisits(Arrays.asList(visitDTO1, visitDTO2));

        // Act: Map PatientDTO to Patient entity
        Patient patient = patientMapper.toEntity(patientDTO);

        // Assert: Verify fields
        assertNotNull(patient);
        assertEquals(patientDTO.getId(), patient.getId());
        assertEquals(patientDTO.getName(), patient.getName());
        assertEquals(patientDTO.getSurname(), patient.getSurname());
        assertEquals(patientDTO.getDateOfBirth(), patient.getDateOfBirth());
        assertEquals(patientDTO.getSocialSecurityNumber(), patient.getSocialSecurityNumber());

        List<Visit> visitList = patient.getVisitList();
        assertNotNull(visitList);
        assertEquals(2, visitList.size());

        Visit visit1 = visitList.get(0);
        assertEquals(visitDTO1.getId(), visit1.getId());
        assertEquals(visitDTO1.getVisitDateTime(), visit1.getVisitDateTime());
        assertEquals(visitDTO1.getType(), visit1.getType().name());
        assertEquals(visitDTO1.getReason(), visit1.getReason().name());
        assertEquals(visitDTO1.getFamilyHistory(), visit1.getFamilyHistory());
        assertEquals(patient, visit1.getPatient()); // Verify bi-directional mapping

        Visit visit2 = visitList.get(1);
        assertEquals(visitDTO2.getId(), visit2.getId());
        assertEquals(visitDTO2.getVisitDateTime(), visit2.getVisitDateTime());
        assertEquals(visitDTO2.getType(), visit2.getType().name());
        assertEquals(visitDTO2.getReason(), visit2.getReason().name());
        assertEquals(visitDTO2.getFamilyHistory(), visit2.getFamilyHistory());
        assertEquals(patient, visit2.getPatient()); // Verify bi-directional mapping
    }

}