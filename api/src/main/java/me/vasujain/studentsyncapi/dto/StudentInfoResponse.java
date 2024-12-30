package me.vasujain.studentsyncapi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class StudentInfoResponse {
    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String avatar;
    private LocalDateTime createdAt;
}
