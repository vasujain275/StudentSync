package me.vasujain.studentsyncapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import me.vasujain.studentsyncapi.enums.CourseOfferingStatus;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "course_offerings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CourseOffering extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @Column(nullable = false)
    private Integer maxStudents;

    @Column(columnDefinition = "text")
    private String scheduleInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseOfferingStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "courseOffering")
    private Set<Enrollment> enrollments = new HashSet<>();

}
