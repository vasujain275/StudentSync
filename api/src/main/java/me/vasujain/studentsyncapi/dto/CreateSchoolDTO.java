package me.vasujain.studentsyncapi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateSchoolDTO {
    private String name;
    private String code;
    private String description;
}
