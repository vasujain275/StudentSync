package me.vasujain.studentsyncapi.controller;

import me.vasujain.studentsyncapi.dto.RegisterUserDTO;
import me.vasujain.studentsyncapi.model.User;
import me.vasujain.studentsyncapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/api/register")
    public User register(@RequestBody RegisterUserDTO registerUserDTO){
        return service.register(registerUserDTO);
    }
}
