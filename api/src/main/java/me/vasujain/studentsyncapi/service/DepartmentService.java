package me.vasujain.studentsyncapi.service;

import jakarta.transaction.Transactional;
import me.vasujain.studentsyncapi.dto.DepartmentDTO;
import me.vasujain.studentsyncapi.exception.ResourceNotFoundException;
import me.vasujain.studentsyncapi.model.Department;
import me.vasujain.studentsyncapi.model.School;
import me.vasujain.studentsyncapi.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final Logger logger;
    private final SchoolService schoolService;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository,
                             Logger logger,
                             SchoolService schoolService){
        this.departmentRepository = departmentRepository;
        this.logger = logger;
        this.schoolService = schoolService;
    }

    public Object getDepartments(boolean paginate, Pageable pageable){
        logger.info("Fetching all the Departments with pagination {}", paginate);

       if(paginate){
           return departmentRepository.findAll(pageable);
       } else {
           return departmentRepository.findAll();
       }
    }

    public Department getDepartment(UUID id){
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find the department with id - "+ id));
    }

    @Transactional
    public Department createDepartment(DepartmentDTO dto){

        School school = schoolService.getSchool(dto.getSchoolId());

        Department department = Department.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .description(dto.getDescription())
                .school(school)
                .build();

        return departmentRepository.save(department);
    }

    @Transactional
    public Department updateDepartment(UUID id, DepartmentDTO dto){
        Department department = departmentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Can't find the department with id - " + id));

        if (dto.getName() != null) department.setName(dto.getName());
        if (dto.getCode() != null) department.setCode(dto.getCode());
        if (dto.getDescription() != null) department.setDescription(dto.getDescription());

        if (dto.getSchoolId() != null) {
            School school = schoolService.getSchool(dto.getSchoolId());
            department.setSchool(school);
        }

        return departmentRepository.save(department);
    }

    @Transactional
    public void deleteDepartment(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find the department with id - " + id));

        departmentRepository.delete(department);
    }
}
