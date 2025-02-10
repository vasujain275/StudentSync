package me.vasujain.studentsyncapi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class AttendanceDTO {
    private UUID enrollmentId;
    private LocalDate date;
    private String status;
    private UUID markedById;
}
