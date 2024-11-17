package com.example.cgmtest.repository;

import com.example.cgmtest.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByPatientId(Long patientId);

    List<Visit> findByVisitDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}