package me.vasujain.studentsyncapi.service;

import me.vasujain.studentsyncapi.dto.RegisterUserDTO;
import me.vasujain.studentsyncapi.model.Role;
import me.vasujain.studentsyncapi.model.User;
import me.vasujain.studentsyncapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Autowired
    private UserRepository userRepository;

    public User register(RegisterUserDTO dto)  {


        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .role(Role.valueOf(dto.getRole()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .avatar(null)
                .build();
        return userRepository.save(user);
    }
}
