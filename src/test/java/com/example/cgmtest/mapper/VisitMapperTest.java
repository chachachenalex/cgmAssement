package com.example.cgmtest.mapper;

import com.example.cgmtest.dto.VisitDTO;
import com.example.cgmtest.dto.mapper.VisitMapper;
import com.example.cgmtest.entity.Visit;
import com.example.cgmtest.entity.enumType.VisitReason;
import com.example.cgmtest.entity.enumType.VisitType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class VisitMapperTest {

    private final VisitMapper visitMapper = new VisitMapper();

    @Test
    void testToDtoMapping() {
        // Arrange: Create Visit entity
        Visit visit = new Visit();
        visit.setId(1L);
        visit.setVisitDateTime(LocalDateTime.of(2023, 11, 17, 10, 0));
        visit.setType(VisitType.AT_HOME);
        visit.setReason(VisitReason.FIRST_VISIT);
        visit.setFamilyHistory("No known issues");

        // Act: Map to VisitDTO
        VisitDTO visitDTO = visitMapper.toDto(visit);

        // Assert: Verify all fields are mapped correctly
        assertNotNull(visitDTO);
        assertEquals(visit.getId(), visitDTO.getId());
        assertEquals(visit.getVisitDateTime(), visitDTO.getVisitDateTime());
        assertEquals(visit.getType().name(), visitDTO.getType());
        assertEquals(visit.getReason().name(), visitDTO.getReason());
        assertEquals(visit.getFamilyHistory(), visitDTO.getFamilyHistory());
        assertNull(visitDTO.getPatientId()); // No Patient set in Visit entity
    }

    @Test
    void testToEntityMapping() {
        // Arrange: Create VisitDTO
        VisitDTO visitDTO = new VisitDTO();
        visitDTO.setId(2L);
        visitDTO.setVisitDateTime(LocalDateTime.of(2023, 11, 18, 15, 30));
        visitDTO.setType("AT_DOCTOR_OFFICE");
        visitDTO.setReason("RECURRING");
        visitDTO.setFamilyHistory("Some family history");

        // Act: Map to Visit entity
        Visit visit = visitMapper.toEntity(visitDTO);

        // Assert: Verify all fields are mapped correctly
        assertNotNull(visit);
        assertEquals(visitDTO.getId(), visit.getId());
        assertEquals(visitDTO.getVisitDateTime(), visit.getVisitDateTime());
        assertEquals(VisitType.AT_DOCTOR_OFFICE, visit.getType());
        assertEquals(VisitReason.RECURRING, visit.getReason());
        assertEquals(visitDTO.getFamilyHistory(), visit.getFamilyHistory());
    }

    @Test
    void testToDtoWithNull() {
        // Act: Map null entity to DTO
        VisitDTO visitDTO = visitMapper.toDto(null);

        // Assert: Ensure result is null
        assertNull(visitDTO);
    }

    @Test
    void testToEntityWithNull() {
        // Act: Map null DTO to entity
        Visit visit = visitMapper.toEntity(null);

        // Assert: Ensure result is null
        assertNull(visit);
    }
}
