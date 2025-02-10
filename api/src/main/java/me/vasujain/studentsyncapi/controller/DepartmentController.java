package me.vasujain.studentsyncapi.controller;

import me.vasujain.studentsyncapi.dto.DepartmentDTO;
import me.vasujain.studentsyncapi.model.Department;
import me.vasujain.studentsyncapi.response.ApiResponse;
import me.vasujain.studentsyncapi.service.DepartmentService;
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
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final Logger logger;

    @Autowired
    public DepartmentController(DepartmentService departmentService,
                                Logger logger){
        this.departmentService = departmentService;
        this.logger = logger;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getDepartments(
            @RequestParam(defaultValue = "false") boolean paginate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        logger.debug("Fetching Departments with pagination={} page={} size={}", paginate,page,size);

        Object result = departmentService.getDepartments(paginate, PageRequest.of(page,size));

        if( result instanceof Page){
            Page<Department> departmentPage = (Page<Department>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(departmentPage.getContent())
                    .pagination(ApiResponse.PaginationMetadata.builder()
                            .totalElements((int) departmentPage.getTotalElements())
                            .totalPages(departmentPage.getTotalPages())
                            .currentPage(departmentPage.getNumber())
                            .pageSize(departmentPage.getSize())
                            .build())

                    .build());
        } else {
            List<Department> departments = (List<Department>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(departments)
                    .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Department>> getDepartment(@PathVariable UUID id){
        Department department = departmentService.getDepartment(id);
        return ResponseEntity.ok(ApiResponse.<Department>builder()
                .status(HttpStatus.OK)
                .data(department)
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Department>> createDepartment(@RequestBody DepartmentDTO dto){
        logger.debug("Creating Department with code={}", dto.getCode());

        Department department = departmentService.createDepartment(dto);
        return ResponseEntity.ok(ApiResponse.<Department>builder()
                .status(HttpStatus.CREATED)
                .message("Department created successfully")
                .data(department)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Department>> updateDepartment(@PathVariable UUID id, @RequestBody DepartmentDTO dto){
        logger.debug("Updating Department with id={}", id);
        Department updateDepartment = departmentService.updateDepartment(id,dto);
        return ResponseEntity.ok(ApiResponse.<Department>builder()
                .status(HttpStatus.OK)
                .message("Department updated successfully")
                .data(updateDepartment).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable UUID id){
        logger.debug("Deleting Department with id={}",id);
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Deleted Department successfully")
                .build());
    }

}
