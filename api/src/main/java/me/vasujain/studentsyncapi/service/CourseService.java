package me.vasujain.studentsyncapi.service;

import jakarta.transaction.Transactional;
import me.vasujain.studentsyncapi.dto.CourseDTO;
import me.vasujain.studentsyncapi.exception.ResourceNotFoundException;
import me.vasujain.studentsyncapi.model.Course;
import me.vasujain.studentsyncapi.repository.BatchRepository;
import me.vasujain.studentsyncapi.repository.CourseRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final Logger logger;
    private final DepartmentService departmentService;

    @Autowired
    public CourseService(CourseRepository courseRepository,
                         Logger logger,
                         DepartmentService departmentService, BatchRepository batchRepository){
        this.courseRepository = courseRepository;
        this.logger = logger;
        this.departmentService = departmentService;
    }

    public Object getCourses(boolean paginate, PageRequest pageRequest){
        logger.info("Fetching Courses with pagination={} page={} size={}", paginate, pageRequest.getPageNumber(), pageRequest.getPageSize());
        if(paginate){
            return courseRepository.findAll(pageRequest);
        } else {
            return courseRepository.findAll();
        }
    }

    public Course getCourse(UUID courseId){
        logger.info("Fetching Course with ID: {}", courseId);
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find Course with ID: " + courseId));
    }

    @Transactional
    public Course createCourse(CourseDTO dto){
        logger.info("Creating Course with Code: {}", dto.getCode());
        Course course = Course.builder()
                .department(departmentService.getDepartment(dto.getDepartmentId()))
                .code(dto.getCode())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .credits(dto.getCredits())
                .lectureClasses(dto.getLectureClasses())
                .tutorialClasses(dto.getTutorialClasses())
                .practicalClasses(dto.getPracticalClasses())
                .semesterNumber(dto.getSemesterNumber())
                .isElective(dto.getIsElective())
                .build();
        return courseRepository.save(course);
    }

    @Transactional
    public Course updateCourse(UUID id, CourseDTO dto){
        logger.info("Updating Course with ID: {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find Course with ID: " + id));

        if (dto.getDepartmentId() != null) course.setDepartment(departmentService.getDepartment(dto.getDepartmentId()));
        if (dto.getCode() != null) course.setCode(dto.getCode());
        if (dto.getTitle() != null) course.setTitle(dto.getTitle());
        if (dto.getDescription() != null) course.setDescription(dto.getDescription());
        if (dto.getCredits() != null) course.setCredits(dto.getCredits());
        if (dto.getLectureClasses() != null) course.setLectureClasses(dto.getLectureClasses());
        if (dto.getTutorialClasses() != null) course.setTutorialClasses(dto.getTutorialClasses());
        if (dto.getPracticalClasses() != null) course.setPracticalClasses(dto.getPracticalClasses());
        if (dto.getSemesterNumber() != null) course.setSemesterNumber(dto.getSemesterNumber());
        if (dto.getIsElective() != null) course.setIsElective(dto.getIsElective());

        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(UUID id){
        logger.info("Deleting Course with ID: {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find Course with ID: " + id));
        courseRepository.delete(course);
    }
}
