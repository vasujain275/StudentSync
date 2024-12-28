package me.vasujain.studentsyncapi.dto;

import lombok.Data;

@Data
public class RegisterUserDTO {
    private String username;
    private String email;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private String avatar;
}