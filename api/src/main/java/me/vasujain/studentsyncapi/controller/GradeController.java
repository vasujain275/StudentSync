package me.vasujain.studentsyncapi.controller;

import me.vasujain.studentsyncapi.dto.GradeDTO;
import me.vasujain.studentsyncapi.model.Grade;
import me.vasujain.studentsyncapi.response.ApiResponse;
import me.vasujain.studentsyncapi.service.GradeService;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/grade")
public class GradeController {
    private final GradeService gradeService;
    private final Logger logger;

    public GradeController(GradeService gradeService, Logger logger){
        this.gradeService = gradeService;
        this.logger = logger;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllGrades(
            @RequestParam(defaultValue = "false") boolean paginate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        logger.debug("Fetching Grades with pagination={} page={} size={}", paginate, page, size);

        Object result = gradeService.getGrades(paginate, PageRequest.of(page, size));

        if(result instanceof Page){
            Page<Grade> gradePage = (Page<Grade>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(gradePage.getContent())
                    .pagination(ApiResponse.PaginationMetadata.builder()
                            .totalElements((int) gradePage.getTotalElements())
                            .currentPage(gradePage.getNumber())
                            .pageSize(gradePage.getSize())
                            .totalPages(gradePage.getTotalPages())
                            .build()
                    )
                    .build()
            );
        } else {
            List<Grade> grades = (List<Grade>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(grades)
                    .build()
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Grade>> getGrade(@PathVariable UUID id){
        logger.debug("Fetching Grade with id={}", id);
        Grade grade = gradeService.getGrade(id);
        return ResponseEntity.ok(ApiResponse.<Grade>builder()
                .status(HttpStatus.OK)
                .data(grade)
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Grade>> createGrade(@RequestBody GradeDTO dto){
        logger.debug("Creating a new Grade");
        Grade grade = gradeService.createGrade(dto);
        return ResponseEntity.ok(ApiResponse.<Grade>builder()
                .status(HttpStatus.CREATED)
                .data(grade)
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Grade>> updateGrade(@PathVariable UUID id, @RequestBody GradeDTO dto){
        logger.debug("Updating Grade with id={}", id);
        Grade grade = gradeService.updateGrade(id, dto);
        return ResponseEntity.ok(ApiResponse.<Grade>builder()
                .status(HttpStatus.OK)
                .data(grade)
                .message("Grade updated successfully")
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteGrade(@PathVariable UUID id){
        logger.debug("Deleting Grade with id={}", id);
        gradeService.deleteGrade(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("Grade deleted successfully")
                .build()
        );
    }
}
