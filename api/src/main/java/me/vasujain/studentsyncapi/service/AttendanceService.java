package me.vasujain.studentsyncapi.service;

import me.vasujain.studentsyncapi.dto.AttendanceDTO;
import me.vasujain.studentsyncapi.enums.AttendanceStatus;
import me.vasujain.studentsyncapi.exception.ResourceNotFoundException;
import me.vasujain.studentsyncapi.model.Attendance;
import me.vasujain.studentsyncapi.repository.AttendanceRepository;
import org.slf4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final Logger logger;
    private final UserService userService;
    private final EnrollmentService enrollmentService;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             Logger logger,
                             @Lazy UserService userService,
                             EnrollmentService enrollmentService){
        this.attendanceRepository = attendanceRepository;
        this.logger = logger;
        this.userService = userService;
        this.enrollmentService = enrollmentService;
    }

    public Object getAttendances(boolean paginate, PageRequest pageRequest){
        logger.info("Fetching Attendances with pagination={}", paginate);
        if(paginate){
            return attendanceRepository.findAll(pageRequest);
        } else {
            return attendanceRepository.findAll();
        }
    }

    public Attendance getAttendance(UUID id){
        return attendanceRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Attendance not found with id -" + id));
    }

    @Transactional
    public Attendance createAttendance(AttendanceDTO dto){
        logger.info("Creating a new Attendance");
        Attendance attendance = Attendance.builder()
                .enrollment(enrollmentService.getEnrollment(dto.getEnrollmentId()))
                .date(dto.getDate())
                .status(AttendanceStatus.valueOf(dto.getStatus()))
                .markedBy(userService.getUser(dto.getMarkedById()))
                .build();
        return attendanceRepository.save(attendance);
    }


    @Transactional
    public Attendance updateAttendance(UUID id, AttendanceDTO dto){
        logger.info("Updating Attendance with id={}", id);
        Attendance attendance = getAttendance(id);
        if(dto.getEnrollmentId() != null) attendance.setEnrollment(enrollmentService.getEnrollment(dto.getEnrollmentId()));
        if(dto.getDate() != null) attendance.setDate(dto.getDate());
        if(dto.getStatus() != null) attendance.setStatus(AttendanceStatus.valueOf(dto.getStatus()));
        if(dto.getMarkedById() != null) attendance.setMarkedBy(userService.getUser(dto.getMarkedById()));

        return attendanceRepository.save(attendance);
    }

    @Transactional
    public void deleteAttendance(UUID id){
        logger.info("Deleting Attendance with id={}", id);
        attendanceRepository.deleteById(id);
    }
}
