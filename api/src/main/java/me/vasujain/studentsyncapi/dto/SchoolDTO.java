package me.vasujain.studentsyncapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SchoolDTO {
    private String name;
    private String code;
    private String description;
}
