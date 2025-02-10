package me.vasujain.studentsyncapi.controller;

import me.vasujain.studentsyncapi.dto.CourseDTO;
import me.vasujain.studentsyncapi.model.Course;
import me.vasujain.studentsyncapi.response.ApiResponse;
import me.vasujain.studentsyncapi.service.CourseService;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;
    private final Logger logger;

    public CourseController(CourseService courseService, Logger logger){
        this.courseService = courseService;
        this.logger = logger;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllCourses(
            @RequestParam(defaultValue = "false") boolean paginate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        logger.debug("Fetching Courses with pagination={} page={} size={}", paginate, page, size);

        Object result = courseService.getCourses(paginate, PageRequest.of(page, size));

        if(result instanceof Page){
            Page<Course> coursePage = (Page<Course>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(coursePage.getContent())
                    .pagination(ApiResponse.PaginationMetadata.builder()
                            .totalElements((int) coursePage.getTotalElements())
                            .currentPage(coursePage.getNumber())
                            .pageSize(coursePage.getSize())
                            .totalPages(coursePage.getTotalPages())
                            .build()
                    )
                    .build()
            );
        } else {
            List<Course> courses = (List<Course>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(courses)
                    .build()
            );
        }
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<ApiResponse<Course>> getCourse(@PathVariable UUID courseId){
        logger.debug("Fetching Course with ID: {}", courseId);
        Course course = courseService.getCourse(courseId);
        return ResponseEntity.ok(ApiResponse.<Course>builder()
                .status(HttpStatus.OK)
                .data(course)
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Course>> createCourse(@RequestBody CourseDTO dto){
        logger.info("Creating Course with Code: {}", dto.getCode());
        Course createdCourse = courseService.createCourse(dto);
        return ResponseEntity.ok(ApiResponse.<Course>builder()
                .status(HttpStatus.CREATED)
                .data(createdCourse)
                .build()
        );
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<ApiResponse<Course>> updateCourse(@PathVariable UUID courseId, @RequestBody CourseDTO dto){
        logger.info("Updating Course with ID: {}", courseId);
        Course updatedCourse = courseService.updateCourse(courseId, dto);
        return ResponseEntity.ok(ApiResponse.<Course>builder()
                .status(HttpStatus.OK)
                .data(updatedCourse)
                .message("Course updated successfully")
                .build()
        );
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable UUID courseId){
        logger.info("Deleting Course with ID: {}", courseId);
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Course deleted successfully")
                .build()
        );
    }
}
