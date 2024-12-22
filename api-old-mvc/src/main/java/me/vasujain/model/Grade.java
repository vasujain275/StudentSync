package me.vasujain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "grades")
public class Grade {
    @Id @Getter @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    @Getter @Setter private Enrollment enrollment;

    @Getter @Setter private String grade;
    @Getter @Setter private Timestamp createdAt;
}
