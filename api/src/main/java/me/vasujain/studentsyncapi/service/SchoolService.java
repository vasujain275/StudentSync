package me.vasujain.studentsyncapi.service;

import jakarta.transaction.Transactional;
import me.vasujain.studentsyncapi.dto.SchoolDTO;
import me.vasujain.studentsyncapi.exception.ResourceNotFoundException;
import me.vasujain.studentsyncapi.model.School;
import me.vasujain.studentsyncapi.repository.SchoolRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final Logger logger;

    @Autowired
    public SchoolService(SchoolRepository schoolRepository,
                         Logger logger){
        this.schoolRepository = schoolRepository;
        this.logger = logger;
    }

    public Object getSchools(boolean paginate, Pageable pageable){
        logger.info("Fetching schools with pagination={}", paginate);

        if (paginate) {
            return schoolRepository.findAll(pageable);
        } else {
            return schoolRepository.findAll();
        }
    }

    public School getSchool(UUID id){
        return schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("School not found " + id));
    }

    @Transactional
    public School createSchool (SchoolDTO dto){
        logger.info("Creating new school with name: {}", dto.getName());

        School school = School.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .description(dto.getDescription())
                .build();

        return schoolRepository.save(school);
    }

    @Transactional
    public School updateSchool(UUID id, SchoolDTO dto){
        logger.info("Update School with id: {}", id);

        School school = schoolRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("School not found with id - " + id));

        school.setName(dto.getName());
        school.setCode(dto.getCode());
        school.setDescription(dto.getDescription());

        return schoolRepository.save(school);
    }

    @Transactional
    public void deleteSchool (UUID id) {

        logger.info("Deleting school by id {}", id);
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("School not found: " + id));
        schoolRepository.delete(school);
    }

}
