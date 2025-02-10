package me.vasujain.studentsyncapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CourseDTO {
    private UUID departmentId;
    private String code;
    private String title;
    private String description;
    private Integer credits;
    private Integer lectureClasses;
    private Integer tutorialClasses;
    private Integer practicalClasses;
    private Integer semesterNumber;
    private Boolean isElective;
}
