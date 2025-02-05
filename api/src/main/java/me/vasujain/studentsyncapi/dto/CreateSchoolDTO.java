package me.vasujain.studentsyncapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateSchoolDTO {
    private String name;
    private String code;
    private String description;
}
