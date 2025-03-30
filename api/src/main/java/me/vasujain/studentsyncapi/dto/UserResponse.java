package me.vasujain.studentsyncapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String username;
    private String email;
    private String userRole;
    private String firstName;
    private String lastName;
    private String avatar;
}
