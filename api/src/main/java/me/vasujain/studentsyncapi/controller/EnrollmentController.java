package me.vasujain.studentsyncapi.controller;

import me.vasujain.studentsyncapi.dto.EnrollmentDTO;
import me.vasujain.studentsyncapi.model.Enrollment;
import me.vasujain.studentsyncapi.response.ApiResponse;
import me.vasujain.studentsyncapi.service.EnrollmentService;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    private final Logger logger;

    public EnrollmentController(EnrollmentService enrollmentService, Logger logger){
        this.enrollmentService = enrollmentService;
        this.logger = logger;
    }

    @GetMapping
    public Object getAllEnrollments(
            @RequestParam(defaultValue = "false") boolean paginate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        logger.debug("Fetching Enrollments with pagination={} page={} size={}", paginate, page, size);

        Object result = enrollmentService.getEnrollments(paginate, PageRequest.of(page, size));

        if(result instanceof Page){
            Page<Enrollment> enrollmentPage = (Page<Enrollment>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(enrollmentPage.getContent())
                    .pagination(ApiResponse.PaginationMetadata.builder()
                            .totalElements((int) enrollmentPage.getTotalElements())
                            .currentPage(enrollmentPage.getNumber())
                            .pageSize(enrollmentPage.getSize())
                            .totalPages(enrollmentPage.getTotalPages())
                            .build()
                    )
                    .build()
            );
        } else {
            List<Enrollment> enrollments = (List<Enrollment>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(enrollments)
                    .build()
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Enrollment>> getEnrollment(@PathVariable UUID id){
        logger.debug("Fetching Enrollment with id={}", id);
        Enrollment enrollment = enrollmentService.getEnrollment(id);
        return ResponseEntity.ok(ApiResponse.<Enrollment>builder()
                .status(HttpStatus.OK)
                .data(enrollment)
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Enrollment>> createEnrollment(@RequestBody EnrollmentDTO dto){
        logger.debug("Creating a new Enrollment");
        Enrollment enrollment = enrollmentService.createEnrollment(dto);
        return ResponseEntity.ok(ApiResponse.<Enrollment>builder()
                .status(HttpStatus.CREATED)
                .data(enrollment)
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Enrollment>> updateEnrollment(@PathVariable UUID id, @RequestBody EnrollmentDTO dto){
        logger.debug("Updating Enrollment with id={}", id);
        Enrollment enrollment = enrollmentService.updateEnrollment(id, dto);
        return ResponseEntity.ok(ApiResponse.<Enrollment>builder()
                .status(HttpStatus.OK)
                .data(enrollment)
                .message("Enrollment updated successfully")
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Enrollment>> deleteEnrollment(@PathVariable UUID id){
        logger.debug("Deleting Enrollment with id={}", id);
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.ok(ApiResponse.<Enrollment>builder()
                .status(HttpStatus.OK)
                .message("Enrollment deleted successfully")
                .build()
        );
    }
}

