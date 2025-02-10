package me.vasujain.studentsyncapi.controller;

import me.vasujain.studentsyncapi.dto.AttendanceDTO;
import me.vasujain.studentsyncapi.model.Attendance;
import me.vasujain.studentsyncapi.response.ApiResponse;
import me.vasujain.studentsyncapi.service.AttendanceService;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final Logger logger;

    public AttendanceController(AttendanceService attendanceService, Logger logger){
        this.attendanceService = attendanceService;
        this.logger = logger;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllAttendance(
            @RequestParam(defaultValue = "false") boolean paginate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        logger.debug("Fetching Attendance with pagination={} page={} size={}", paginate, page, size);

        Object result = attendanceService.getAttendances(paginate, PageRequest.of(page, size));

        if(result instanceof Page){
            Page<Attendance> attendancePage = (Page<Attendance>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(attendancePage.getContent())
                    .pagination(ApiResponse.PaginationMetadata.builder()
                            .totalElements((int) attendancePage.getTotalElements())
                            .currentPage(attendancePage.getNumber())
                            .pageSize(attendancePage.getSize())
                            .totalPages(attendancePage.getTotalPages())
                            .build()
                    )
                    .build()
            );
        } else {
            List<Attendance> attendance = (List<Attendance>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(attendance)
                    .build()
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Attendance>> getAttendance(@PathVariable UUID id){
        logger.debug("Fetching Attendance with id={}", id);
        Attendance attendance = attendanceService.getAttendance(id);
        return ResponseEntity.ok(ApiResponse.<Attendance>builder()
                .status(HttpStatus.OK)
                .data(attendance)
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Attendance>> createAttendance(@RequestBody AttendanceDTO dto){
        logger.debug("Creating a new Attendance");
        Attendance newAttendance = attendanceService.createAttendance(dto);
        return ResponseEntity.ok(ApiResponse.<Attendance>builder()
                .status(HttpStatus.CREATED)
                .data(newAttendance)
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Attendance>> updateAttendance(@PathVariable UUID id, @RequestBody AttendanceDTO dto){
        logger.debug("Updating Attendance with id={}", id);
        Attendance updatedAttendance = attendanceService.updateAttendance(id, dto);
        return ResponseEntity.ok(ApiResponse.<Attendance>builder()
                .status(HttpStatus.OK)
                .data(updatedAttendance)
                .message("Attendance updated successfully")
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteAttendance(@PathVariable UUID id){
        logger.debug("Deleting Attendance with id={}", id);
        attendanceService.deleteAttendance(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("Attendance deleted successfully")
                .build()
        );
    }
}
