package me.vasujain.studentsyncapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DepartmentDTO {
    private String name;
    private String code;
    private String description;
    private UUID schoolId;
}
