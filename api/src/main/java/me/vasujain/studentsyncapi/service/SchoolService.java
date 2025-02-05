package me.vasujain.studentsyncapi.service;

import jakarta.transaction.Transactional;
import me.vasujain.studentsyncapi.dto.CreateSchoolDTO;
import me.vasujain.studentsyncapi.exception.ResourceNotFoundException;
import me.vasujain.studentsyncapi.model.School;
import me.vasujain.studentsyncapi.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;

    @Autowired
    public SchoolService(SchoolRepository schoolRepository){
        this.schoolRepository = schoolRepository;
    }

    public List<School>  getAllSchools(){
        return schoolRepository.findAll();
    }

    public Optional<School> getSchool(UUID id){
        return schoolRepository.findById(id);
    }

    @Transactional
    public School createSchool (CreateSchoolDTO dto){
        School school = School.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .description(dto.getDescription())
                .build();

        return schoolRepository.save(school);
    }

    @Transactional
    public void deleteSchool (UUID id) {

        Optional<School> existingSchoolOptional = schoolRepository.findById(id);

        if(existingSchoolOptional.isPresent()){
            schoolRepository.delete(existingSchoolOptional.get());
        } else {
            throw new ResourceNotFoundException("Notice not found with UUID: " + id);
        }
    }

}
