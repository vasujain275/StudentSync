package me.vasujain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "courses")
public class Course {
    @Id @Getter @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Getter @Setter
    @Column(unique = true, nullable = false)
    private String code;

    @Getter @Setter
    @Column(nullable = false)
    private String title;

    @Getter @Setter private String description;
    @Getter @Setter private int credits;
    @Getter @Setter private Timestamp createdAt;
    @Getter @Setter private Timestamp updatedAt;
}
