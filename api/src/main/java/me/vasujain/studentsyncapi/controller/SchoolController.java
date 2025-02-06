package me.vasujain.studentsyncapi.controller;

import me.vasujain.studentsyncapi.dto.SchoolDTO;
import me.vasujain.studentsyncapi.model.School;
import me.vasujain.studentsyncapi.response.ApiResponse;
import me.vasujain.studentsyncapi.service.SchoolService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/school")
public class SchoolController {

    // Injecting required dependencies
    private final SchoolService schoolService;
    private final Logger logger;

    @Autowired
    public SchoolController(SchoolService schoolService, Logger logger) {
        this.schoolService = schoolService;
        this.logger = logger;
    }

    /**
     * Get all schools with optional pagination
     * GET /api/v1/school?paginate=false
     * GET /api/v1/school?paginate=true&page=0&size=10
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllSchools(
            @RequestParam(defaultValue = "false") boolean paginate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.debug("Fetching schools with pagination={}, page={}, size={}", paginate, page, size);

        Object result = schoolService.getSchools(paginate, PageRequest.of(page, size));

        // Handle both paginated and non-paginated responses
        if (result instanceof Page) {
            Page<School> schoolPage = (Page<School>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(schoolPage.getContent())
                    .pagination(ApiResponse.PaginationMetadata.builder()
                            .totalElements((int) schoolPage.getTotalElements())
                            .totalPages(schoolPage.getTotalPages())
                            .currentPage(schoolPage.getNumber())
                            .pageSize(schoolPage.getSize())
                            .build())
                    .build());
        } else {
            List<School> schools = (List<School>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(schools)
                    .build());
        }
    }

    /**
     * Get a specific school by ID
     * GET /api/v1/school/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<School>> getSchool(@PathVariable UUID id) {
        logger.debug("Fetching school with id: {}", id);

        School school = schoolService.getSchool(id);
        return ResponseEntity.ok(ApiResponse.<School>builder()
                .status(HttpStatus.OK)
                .data(school)
                .build());
    }

    /**
     * Create a new school
     * POST /api/v1/school
     */
    @PostMapping
    public ResponseEntity<ApiResponse<School>> createSchool(@RequestBody SchoolDTO dto) {
        logger.debug("Creating new school with name: {}", dto.getName());

        School createdSchool = schoolService.createSchool(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.<School>builder()
                        .status(HttpStatus.CREATED)
                        .message("School created successfully")
                        .data(createdSchool)
                        .build());
    }

    /**
     * Update an existing school
     * PUT /api/v1/school/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<School>> updateSchool(
            @PathVariable UUID id,
            @RequestBody SchoolDTO dto) {
        logger.debug("Updating school with id: {}", id);

        School updatedSchool = schoolService.updateSchool(id, dto);
        return ResponseEntity.ok(ApiResponse.<School>builder()
                .status(HttpStatus.OK)
                .message("School updated successfully")
                .data(updatedSchool)
                .build());
    }

    /**
     * Delete a school
     * DELETE /api/v1/school/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSchool(@PathVariable UUID id) {
        logger.debug("Deleting school with id: {}", id);

        schoolService.deleteSchool(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("School deleted successfully")
                .build());
    }
}