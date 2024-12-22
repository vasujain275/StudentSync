package me.vasujain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id @Getter @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne @Getter @Setter
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne @Getter @Setter
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Getter @Setter
    @Column(name = "enrollment_date", nullable = false)
    private Date enrollmentDate;

    @Getter @Setter private String status;
    @Getter @Setter private Timestamp createdAt;
    @Getter @Setter private Timestamp updatedAt;

}
