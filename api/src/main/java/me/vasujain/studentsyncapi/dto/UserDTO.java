package me.vasujain.studentsyncapi.dto;

import lombok.Data;

import java.util.UUID;


@Data
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private String role;
    private UUID departmentId;
    private UUID batchId;
    private String firstName;
    private String lastName;
    private Integer admissionYear;
    private Integer currentSemester;
    private String status;
}