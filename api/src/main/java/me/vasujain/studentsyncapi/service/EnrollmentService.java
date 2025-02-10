package me.vasujain.studentsyncapi.service;

import me.vasujain.studentsyncapi.dto.EnrollmentDTO;
import me.vasujain.studentsyncapi.enums.EnrollmentStatus;
import me.vasujain.studentsyncapi.exception.ResourceNotFoundException;
import me.vasujain.studentsyncapi.model.Enrollment;
import me.vasujain.studentsyncapi.repository.EnrollmentRepository;
import org.slf4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final Logger logger;
    private final UserService userService;
    private final CourseService courseService;
    private final SemesterService semesterService;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             Logger logger, UserService userService,
                             CourseService courseService,
                             SemesterService semesterService){
        this.enrollmentRepository = enrollmentRepository;
        this.logger = logger;
        this.userService = userService;
        this.courseService = courseService;
        this.semesterService = semesterService;
    }

    public Object getEnrollments(boolean paginate, PageRequest pageRequest){
        logger.info("Fetching Enrollments with pagination={}", paginate);
        if(paginate){
            return enrollmentRepository.findAll(pageRequest);
        } else {
            return enrollmentRepository.findAll();
        }
    }

    public Enrollment getEnrollment(UUID id){
        return enrollmentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Enrollment not found with id -" + id));
    }

    @Transactional
    public Enrollment createEnrollment(EnrollmentDTO dto){
        logger.info("Creating a new Enrollment");
        Enrollment enrollment = Enrollment.builder()
                .course(courseService.getCourse(dto.getCourseId()))
                .semester(semesterService.getSemester(dto.getSemesterId()))
                .student(userService.getUser(dto.getStudentId()))
                .build();
        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public Enrollment updateEnrollment(UUID id, EnrollmentDTO dto){
        logger.info("Updating Enrollment with id={}", id);
        Enrollment enrollment = getEnrollment(id);
        if (dto.getCourseId() != null) enrollment.setCourse(courseService.getCourse(dto.getCourseId()));
        if (dto.getSemesterId() != null) enrollment.setSemester(semesterService.getSemester(dto.getSemesterId()));
        if(dto.getStudentId() != null) enrollment.setStudent(userService.getUser(dto.getStudentId()));
        if(dto.getTeacherId() != null) enrollment.setTeacher(userService.getUser(dto.getTeacherId()));
        if(dto.getEnrollmentDate() != null) enrollment.setEnrollmentDate(dto.getEnrollmentDate());
        if(dto.getTotalClasses() != null) enrollment.setTotalClasses(dto.getTotalClasses());
        if(dto.getStatus() != null) enrollment.setStatus(EnrollmentStatus.valueOf(dto.getStatus()));

        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public void deleteEnrollment(UUID id){
        logger.info("Deleting Enrollment with id={}", id);
        Enrollment enrollment = getEnrollment(id);
        enrollmentRepository.delete(enrollment);
    }
}
