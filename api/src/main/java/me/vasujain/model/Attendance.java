package me.vasujain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "attendance")
public class Attendance {
    @Id @Getter @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    @Getter @Setter private Enrollment enrollment;

    @Column(nullable = false)
    @Getter @Setter private Date date;

    @Column(nullable = false)
    @Getter @Setter private String status;

    @Getter @Setter private Timestamp createdAt;
}
