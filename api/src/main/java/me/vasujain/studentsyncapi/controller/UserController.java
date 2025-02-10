package me.vasujain.studentsyncapi.controller;

import me.vasujain.studentsyncapi.dto.UserDTO;
import me.vasujain.studentsyncapi.model.User;
import me.vasujain.studentsyncapi.response.ApiResponse;
import me.vasujain.studentsyncapi.service.UserService;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final Logger logger;

    public UserController(UserService userService, Logger logger) {
        this.userService = userService;
        this.logger = logger;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllUsers(
            @RequestParam(defaultValue = "false") boolean paginate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        logger.debug("Fetching Users with pagination={} page={} size={}",paginate, page,size);

        Object result = userService.getAllUsers(paginate, PageRequest.of(page,size));

        if(result instanceof Page){
            Page<User> userPage = (Page<User>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(userPage.getContent())
                    .pagination(ApiResponse.PaginationMetadata.builder()
                            .totalElements((int) userPage.getTotalElements())
                            .currentPage(userPage.getNumber())
                            .pageSize(userPage.getSize())
                            .totalPages(userPage.getTotalPages())
                            .build()
                    )
                    .build()
            );
        } else {
            List<User> users = (List<User>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(users)
                    .build()
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable UUID id){
        logger.debug("Fetching User with id={}", id);

        User user = userService.getUser(id);

        return ResponseEntity.ok(ApiResponse.<User>builder()
                .status(HttpStatus.OK)
                .data(user)
                .build()
        );
    }

    @GetMapping("/u/{username}")
    public ResponseEntity<ApiResponse<User>> getUserByUsername(@PathVariable String username){
        logger.debug("Fetching User with username={}", username);

        User user = userService.getUserByUsername(username);

        return ResponseEntity.ok(ApiResponse.<User>builder()
                .status(HttpStatus.OK)
                .data(user)
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody UserDTO dto){
        logger.info("Creating dto with username - {}", dto.getUsername());

        User createdUser = userService.createUser(dto);

        return ResponseEntity.ok(ApiResponse.<User>builder()
                .status(HttpStatus.CREATED)
                .data(createdUser)
                .message("User created successfully")
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable UUID id, @RequestBody UserDTO dto){
        logger.info("Updating user with id={}", id);

        User updatedUser = userService.updateUser(id, dto);

        return ResponseEntity.ok(ApiResponse.<User>builder()
                .status(HttpStatus.OK)
                .data(updatedUser)
                .message("User updated successfully")
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id){
        logger.info("Deleting user with id={}", id);

        userService.deleteUser(id);

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("User deleted successfully")
                .build()
        );
    }
}
