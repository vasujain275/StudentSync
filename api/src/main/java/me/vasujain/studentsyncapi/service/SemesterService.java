package me.vasujain.studentsyncapi.service;

import jakarta.transaction.Transactional;
import me.vasujain.studentsyncapi.dto.SemesterDTO;
import me.vasujain.studentsyncapi.enums.SemesterStatus;
import me.vasujain.studentsyncapi.enums.SemesterType;
import me.vasujain.studentsyncapi.exception.ResourceNotFoundException;
import me.vasujain.studentsyncapi.model.Semester;
import me.vasujain.studentsyncapi.repository.SemesterRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SemesterService {

    private SemesterRepository semesterRepository;
    private Logger logger;

    @Autowired
    public SemesterService(SemesterRepository semesterRepository,
                           Logger logger){
        this.semesterRepository = semesterRepository;
        this.logger = logger;
    }

    public Semester getSemester(UUID id){
        return semesterRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Semester not found with id -" + id));
    }

    public Object getSemesters(boolean paginate, Pageable pageable){
        logger.info("Fetching semesters with pagination={}", paginate);

        if (paginate){
            return semesterRepository.findAll(pageable);
        } else {
            return semesterRepository.findAll();
        }
    }

    @Transactional
    public Semester createSemester(SemesterDTO dto){
        logger.info("Creating a new Semester");
        Semester semester = Semester.builder()
                .academicYear(dto.getAcademicYear())
                .type(SemesterType.valueOf(dto.getType()))
                .number(dto.getNumber())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .registrationStart(dto.getRegistrationStart())
                .registrationEnd(dto.getRegistrationEnd())
                .status(SemesterStatus.valueOf(dto.getStatus()))
                .build();

        return semesterRepository.save(semester);
    }

    @Transactional
    public void deleteSemester(UUID id){
        logger.info("Deleting the semester with id - {}", id);
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Can't find the semester with id -"+id));
    }

    @Transactional
    public Semester updateSemester(UUID id, SemesterDTO dto){
        logger.info("Updating the semester with id - {}", id);
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Can't find the semester with id -"+id));

        if(dto.getAcademicYear() != null) semester.setAcademicYear(dto.getAcademicYear());
        if(dto.getType() != null) semester.setType(SemesterType.valueOf(dto.getType()));
        if(dto.getNumber() != null) semester.setNumber(dto.getNumber());
        if(dto.getStartDate() != null) semester.setStartDate(dto.getStartDate());
        if(dto.getEndDate() != null) semester.setEndDate(dto.getEndDate());
        if(dto.getRegistrationStart() != null) semester.setRegistrationStart(dto.getRegistrationStart());
        if(dto.getRegistrationEnd() != null) semester.setRegistrationEnd(dto.getRegistrationEnd());
        if(dto.getStatus() != null) semester.setStatus(SemesterStatus.valueOf(dto.getStatus()));

        return semesterRepository.save(semester);
    }


}
