package me.vasujain.studentsyncapi.controller;

import me.vasujain.studentsyncapi.dto.RegisterUserDTO;
import me.vasujain.studentsyncapi.model.User;
import me.vasujain.studentsyncapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/api/register")
    public User register(@RequestPart RegisterUserDTO registerUserDTO, @RequestPart MultipartFile avatar) throws IOException {
        return service.register(registerUserDTO, avatar);
    }
}
