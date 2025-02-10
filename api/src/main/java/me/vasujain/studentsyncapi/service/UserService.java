package me.vasujain.studentsyncapi.service;

import jakarta.transaction.Transactional;
import me.vasujain.studentsyncapi.dto.UserDTO;
import me.vasujain.studentsyncapi.enums.UserRole;
import me.vasujain.studentsyncapi.enums.UserStatus;
import me.vasujain.studentsyncapi.exception.ResourceNotFoundException;
import me.vasujain.studentsyncapi.model.User;
import me.vasujain.studentsyncapi.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final Logger logger;
    private final DepartmentService departmentService;
    private final BatchService batchService;

    public UserService(PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       Logger logger,
                       DepartmentService departmentService,
                       @Lazy BatchService batchService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.logger = logger;
        this.departmentService = departmentService;
        this.batchService = batchService;
    }

    public Object getAllUsers(boolean paginate, Pageable pageable) {
        if(paginate){
            return userRepository.findAll(pageable);
        }
        return userRepository.findAll();
    }

    public User getUser(UUID id){
        return userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id -" + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User createUser(UserDTO dto)  {
        logger.info("Creating user with username - {}", dto.getUsername());

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .userRole(UserRole.valueOf(dto.getRole()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .status(UserStatus.valueOf(dto.getStatus()))
                .avatar(null)
                .build();

        if(dto.getDepartmentId() != null ) user.setDepartment(departmentService.getDepartment(dto.getDepartmentId()));
        if(dto.getBatchId() != null ) user.setBatch(batchService.getBatch(dto.getBatchId()));
        if(dto.getAdmissionYear() != null ) user.setAdmissionYear(dto.getAdmissionYear());
        if(dto.getCurrentSemester() != null ) user.setCurrentSemester(dto.getCurrentSemester());

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(UUID id, UserDTO dto) {
        User user = getUser(id);
        if (dto.getUsername() != null)  user.setUsername(dto.getUsername());
        if (dto.getPassword() != null)  user.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (dto.getEmail() != null)  user.setEmail(dto.getEmail());
        if (dto.getRole() != null)  user.setUserRole(UserRole.valueOf(dto.getRole()));
        if (dto.getFirstName() != null)  user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null)  user.setLastName(dto.getLastName());
        if (dto.getStatus() != null)  user.setStatus(UserStatus.valueOf(dto.getStatus()));
        if (dto.getDepartmentId() != null)  user.setDepartment(departmentService.getDepartment(dto.getDepartmentId()));
        if (dto.getBatchId() != null)  user.setBatch(batchService.getBatch(dto.getBatchId()));

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

}
