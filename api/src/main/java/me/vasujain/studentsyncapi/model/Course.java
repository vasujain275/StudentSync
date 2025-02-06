package me.vasujain.studentsyncapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Course extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer credits;

    @Column(name = "lecture_classes")
    private Integer lectureClasses;

    @Column(name = "tutorial_classes")
    private Integer tutorialClasses;

    @Column(name = "practical_classes")
    private Integer practicalClasses;

    @Column(name = "semester_number")
    private Integer semesterNumber;

    @Column(nullable = false)
    private Boolean isElective = false;
}
