package me.vasujain.studentsyncapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "batches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Batch extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer startSemester;

    @Column(nullable = false)
    private Integer capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordinator_id")
    private User coordinator;

    @Builder.Default
    @OneToMany(mappedBy = "batch")
    private Set<CourseOffering> courseOfferings = new HashSet<>();
}
