package me.vasujain.studentsyncapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/superadmin/hello")
    public String sayHello() {
        return "Hello, World!";
    }
}
