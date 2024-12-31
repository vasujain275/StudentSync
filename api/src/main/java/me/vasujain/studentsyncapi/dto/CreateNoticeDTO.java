package me.vasujain.studentsyncapi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateNoticeDTO {
    private String title;
    private String Notice;
    private LocalDate date;
}
