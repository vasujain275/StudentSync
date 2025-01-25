package me.vasujain.studentsyncapi.service;

import me.vasujain.studentsyncapi.dto.CreateSchoolDTO;
import me.vasujain.studentsyncapi.model.School;
import me.vasujain.studentsyncapi.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;

    public List<School>  getAllSchools(){
        return schoolRepository.findAll();
    }

    public School createSchool (CreateSchoolDTO dto){
        School school = School.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .description(dto.getDescription())
                .build();

        return schoolRepository.save(school);
    }

    public void deleteSchool (UUID id) {

        Optional<School> existingSchoolOptional = schoolRepository.findById(id);

        if(existingSchoolOptional.isPresent()){
            schoolRepository.delete(existingSchoolOptional.get());
        } else {
            throw new RuntimeException("Notice not found with UUID: " + id);
        }
    }

}
