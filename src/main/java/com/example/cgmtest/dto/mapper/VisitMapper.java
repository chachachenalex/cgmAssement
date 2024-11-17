package com.example.cgmtest.dto.mapper;
import com.example.cgmtest.dto.VisitDTO;
import com.example.cgmtest.entity.Visit;
import com.example.cgmtest.entity.enumType.VisitReason;
import com.example.cgmtest.entity.enumType.VisitType;
import org.springframework.stereotype.Component;

@Component
public class VisitMapper {

    public  VisitDTO toDto(Visit visit) {
        if (visit == null) {
            return null;
        }

        VisitDTO visitDTO = new VisitDTO();
        visitDTO.setId(visit.getId());
        visitDTO.setVisitDateTime(visit.getVisitDateTime());
        visitDTO.setType(visit.getType() != null ? visit.getType().name() : null);
        visitDTO.setReason(visit.getReason() != null ? visit.getReason().name() : null);
        visitDTO.setFamilyHistory(visit.getFamilyHistory());
        visitDTO.setPatientId(visit.getPatient() != null ? visit.getPatient().getId() : null);

        return visitDTO;
    }

    public  Visit toEntity(VisitDTO visitDTO) {
        if (visitDTO == null) {
            return null;
        }

        Visit visit = new Visit();
        visit.setId(visitDTO.getId());
        visit.setVisitDateTime(visitDTO.getVisitDateTime());
        visit.setType(visitDTO.getType() != null ? VisitType.valueOf(visitDTO.getType()) : null);
        visit.setReason(visitDTO.getReason() != null ? VisitReason.valueOf(visitDTO.getReason()) : null);
        visit.setFamilyHistory(visitDTO.getFamilyHistory());

        return visit;
    }
}

