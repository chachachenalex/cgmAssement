package com.example.cgmtest.entity;

import com.example.cgmtest.entity.enumType.VisitReason;
import com.example.cgmtest.entity.enumType.VisitType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime visitDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VisitType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VisitReason reason;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String familyHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;


}
