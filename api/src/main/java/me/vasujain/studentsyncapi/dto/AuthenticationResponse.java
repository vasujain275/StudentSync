package me.vasujain.studentsyncapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    private String userRole;
    private String username;
}
