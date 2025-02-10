package me.vasujain.studentsyncapi.service;

import jakarta.transaction.Transactional;
import me.vasujain.studentsyncapi.dto.BatchDTO;
import me.vasujain.studentsyncapi.exception.ResourceNotFoundException;
import me.vasujain.studentsyncapi.model.Batch;
import me.vasujain.studentsyncapi.repository.BatchRepository;
import org.slf4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BatchService {
    private final BatchRepository batchRepository;
    private final Logger logger;
    private final UserService userService;
    private final DepartmentService departmentService;
    private final SemesterService semesterService;

    public BatchService(BatchRepository batchRepository,
                        Logger logger,
                        @Lazy UserService userService,
                        DepartmentService departmentService,
                        SemesterService semesterService){
        this.batchRepository = batchRepository;
        this.logger = logger;
        this.userService = userService;
        this.departmentService = departmentService;
        this.semesterService = semesterService;
    }

    public Object getBatches(boolean paginate, PageRequest pageRequest){
        logger.info("Fetching Batches with pagination={}", paginate);
        if(paginate){
            return batchRepository.findAll(pageRequest);
        } else {
            return batchRepository.findAll();
        }
    }

    public Batch getBatch(UUID id){
        return batchRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Batch not found with id -" + id));
    }

    @Transactional
    public Batch createBatch(BatchDTO dto){
        logger.info("Creating a new Batch");
        Batch batch = Batch.builder()
                .department(departmentService.getDepartment(dto.getDepartmentId()))
                .semester(semesterService.getSemester(dto.getSemesterId()))
                .name(dto.getName())
                .scheduleInfo(dto.getScheduleInfo())
                .capacity(dto.getCapacity())
                .coordinator(userService.getUser(dto.getCoordinatorId()))
                .build();
        return batchRepository.save(batch);
    }

    @Transactional
    public Batch updateBatch(UUID id, BatchDTO dto){
        logger.info("Updating Batch with id={}", id);
        Batch batch = batchRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Batch not found with id -" + id));

        if (dto.getDepartmentId() != null) batch.setDepartment(departmentService.getDepartment(dto.getDepartmentId()));
        if (dto.getSemesterId() != null) batch.setSemester(semesterService.getSemester(dto.getSemesterId()));
        if (dto.getName() != null) batch.setName(dto.getName());
        if (dto.getScheduleInfo() != null) batch.setScheduleInfo(dto.getScheduleInfo());
        if (dto.getCapacity() != null) batch.setCapacity(dto.getCapacity());
        if (dto.getCoordinatorId() != null) batch.setCoordinator(userService.getUser(dto.getCoordinatorId()));

        return batchRepository.save(batch);
    }

    @Transactional
    public void deleteBatch(UUID id){
        logger.info("Deleting Batch with id={}", id);
        Batch batch = batchRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Batch not found with id -" + id));
        batchRepository.delete(batch);
    }
}