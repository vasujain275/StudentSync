package me.vasujain.studentsyncapi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SemesterDTO {
    private Integer academicYear;
    private String type;
    private Integer number;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate registrationStart;
    private LocalDate registrationEnd;
    private String status;
}
