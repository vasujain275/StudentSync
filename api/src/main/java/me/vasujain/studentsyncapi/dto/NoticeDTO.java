package me.vasujain.studentsyncapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {
    private String title;
    private String Notice;
    private LocalDate date;
}
