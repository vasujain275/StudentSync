package me.vasujain.studentsyncapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import me.vasujain.studentsyncapi.enums.AcademicYearStatus;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "acadmic_year")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AcademicYear extends BaseEntity{
    @Column(nullable = false, unique = true)
    private String year;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcademicYearStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "academicYear", cascade = CascadeType.ALL)
    private Set<Semester> semesters = new HashSet<>();
}
