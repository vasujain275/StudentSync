package me.vasujain.studentsyncapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import me.vasujain.studentsyncapi.enums.EnrollmentStatus;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Enrollment extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @Column(nullable = false)
    private LocalDate enrollmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    private Integer totalClasses;
    private Integer attendedClasses;
    private Integer absentClasses;
    private Float attendancePercentage;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "enrollment")
    private Set<Attendance> attendanceRecords = new HashSet<>();

    @OneToOne(mappedBy = "enrollment")
    private Grade grade;

}
