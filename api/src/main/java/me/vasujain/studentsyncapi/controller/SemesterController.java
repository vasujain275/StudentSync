package me.vasujain.studentsyncapi.controller;

import me.vasujain.studentsyncapi.dto.SemesterDTO;
import me.vasujain.studentsyncapi.model.Semester;
import me.vasujain.studentsyncapi.response.ApiResponse;
import me.vasujain.studentsyncapi.service.SemesterService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/semester")
public class SemesterController {

    private final SemesterService semesterService;
    private final Logger logger;

    @Autowired
    public SemesterController(SemesterService semesterService,
                              Logger logger){
        this.semesterService = semesterService;
        this.logger = logger;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllSemesters(
            @RequestParam(defaultValue = "false") boolean paginate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        logger.debug("Feting Semesters with pagination={} page={} size={}",paginate, page,size);

        Object result = semesterService.getSemesters(paginate, PageRequest.of(page,size));

        if(result instanceof Page){
            Page<Semester> semesterPage = (Page<Semester>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(semesterPage.getContent())
                    .pagination(ApiResponse.PaginationMetadata.builder()
                            .totalElements((int) semesterPage.getTotalElements())
                            .currentPage(semesterPage.getNumber())
                            .pageSize(semesterPage.getSize())
                            .totalPages(semesterPage.getTotalPages())
                            .build()
                    )
                    .build()
            );
        } else {
            List<Semester> semesters = (List<Semester>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(semesters)
                    .build()
            );
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Semester>> getSemester(@PathVariable UUID id){
        Semester semester = semesterService.getSemester(id);
        return ResponseEntity.ok(ApiResponse.<Semester>builder()
                .status(HttpStatus.OK)
                .data(semester)
                .message("Fetched semester successfully")
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Semester>> createSemester(@RequestBody SemesterDTO dto){
        logger.info("Creating new semester");

        Semester semester = semesterService.createSemester(dto);
        return ResponseEntity.ok(ApiResponse.<Semester>builder()
                .status(HttpStatus.CREATED)
                .data(semester)
                .message("Created Semester with id - "+semester.getId())
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Semester>> updateSemester(
            @PathVariable UUID id,
            @RequestBody SemesterDTO dto
            ){
        logger.info("Updating Semester with id={}", id);

        Semester semester = semesterService.updateSemester(id, dto);
        return ResponseEntity.ok(ApiResponse.<Semester>builder()
                .status(HttpStatus.OK)
                .data(semester)
                .message("Updated Semester with id - "+semester.getId())
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSemester(@PathVariable UUID id){
        logger.info("Deleting Semester with id={}", id);
        semesterService.deleteSemester(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Deleted Semester with id- "+ id)
                .build()
        );
    }
}
