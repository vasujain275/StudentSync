package me.vasujain.studentsyncapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import me.vasujain.studentsyncapi.enums.SemesterStatus;
import me.vasujain.studentsyncapi.enums.SemesterType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "semesters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Semester extends BaseEntity{

    @Column(nullable = false)
    private Integer academicYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SemesterType type;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalDate registrationStart;

    @Column(nullable = false)
    private LocalDate registrationEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SemesterStatus status;
}
