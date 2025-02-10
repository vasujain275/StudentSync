package me.vasujain.studentsyncapi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class EnrollmentDTO {
    private UUID studentId;
    private UUID teacherId;
    private UUID courseId;
    private LocalDate enrollmentDate;
    private UUID semesterId;
    private Integer totalClasses;
    private String status;
}
