package me.vasujain.studentsyncapi.service;

import jakarta.transaction.Transactional;
import me.vasujain.studentsyncapi.dto.GradeDTO;
import me.vasujain.studentsyncapi.exception.ResourceNotFoundException;
import me.vasujain.studentsyncapi.model.Grade;
import me.vasujain.studentsyncapi.repository.GradeRepository;
import org.slf4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;
    private final Logger logger;
    private final UserService userService;
    private final EnrollmentService enrollmentService;

    public GradeService(GradeRepository gradeRepository,
                        Logger logger,
                        UserService userService,
                        EnrollmentService enrollmentService){
        this.gradeRepository = gradeRepository;
        this.logger = logger;
        this.userService = userService;
        this.enrollmentService = enrollmentService;
    }

    public Object getGrades(boolean paginate, PageRequest pageRequest){
        logger.info("Fetching Grades with pagination={}", paginate);
        if(paginate){
            return gradeRepository.findAll(pageRequest);
        } else {
            return gradeRepository.findAll();
        }
    }

    public Grade getGrade(UUID id){
        return gradeRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Grade not found with id -" + id));
    }

    @Transactional
    public Grade createGrade(GradeDTO dto){
        logger.info("Creating a new Grade");
        Grade grade = Grade.builder()
                .grade(dto.getGrade())
                .gradedBy(userService.getUser(dto.getGradedById()))
                .enrollment(enrollmentService.getEnrollment(dto.getEnrollmentId()))
                .build();
        return gradeRepository.save(grade);
    }

    @Transactional
    public Grade updateGrade(UUID id, GradeDTO dto){
        logger.info("Updating Grade with id={}", id);
        Grade grade = getGrade(id);
        if(dto.getGrade() != null) grade.setGrade(dto.getGrade());
        if(dto.getGradedById() != null) grade.setGradedBy(userService.getUser(dto.getGradedById()));
        if(dto.getEnrollmentId() != null) grade.setEnrollment(enrollmentService.getEnrollment(dto.getEnrollmentId()));

        return gradeRepository.save(grade);
    }

    @Transactional
    public void deleteGrade(UUID id){
        logger.info("Deleting Grade with id={}", id);
        gradeRepository.delete(getGrade(id));
    }
}
