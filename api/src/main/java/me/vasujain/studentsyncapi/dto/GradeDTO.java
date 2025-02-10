package me.vasujain.studentsyncapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class GradeDTO {
    private UUID enrollmentId;
    private String grade;
    private UUID gradedById;
}
