package me.vasujain.studentsyncapi.controller;

import me.vasujain.studentsyncapi.dto.BatchDTO;
import me.vasujain.studentsyncapi.model.Batch;
import me.vasujain.studentsyncapi.response.ApiResponse;
import me.vasujain.studentsyncapi.service.BatchService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/batch")
public class BatchController {
    private final BatchService batchService;
    private final Logger logger;

    @Autowired
    public BatchController(BatchService batchService, Logger logger){
        this.batchService = batchService;
        this.logger = logger;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllBatches(
            @RequestParam(defaultValue = "false") boolean paginate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        logger.debug("Fetching Batches with pagination={} page={} size={}", paginate, page, size);

        Object result = batchService.getBatches(paginate, PageRequest.of(page, size));

        if(result instanceof Page){
            Page<Batch> batchPage = (Page<Batch>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(batchPage.getContent())
                    .pagination(ApiResponse.PaginationMetadata.builder()
                            .totalElements((int) batchPage.getTotalElements())
                            .currentPage(batchPage.getNumber())
                            .pageSize(batchPage.getSize())
                            .totalPages(batchPage.getTotalPages())
                            .build()
                    )
                    .build()
            );
        } else {
            List<Batch> batches = (List<Batch>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(batches)
                    .build()
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Batch>> getBatch(@PathVariable UUID id){
        logger.debug("Fetching Batch with id={}", id);
        Batch batch = batchService.getBatch(id);
        return ResponseEntity.ok(ApiResponse.<Batch>builder()
                .status(HttpStatus.OK)
                .data(batch)
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Batch>> createBatch(@RequestBody BatchDTO dto){
        logger.debug("Creating Batch with name={}", dto.getName());
        Batch createdBatch = batchService.createBatch(dto);
        return ResponseEntity.ok(ApiResponse.<Batch>builder()
                .status(HttpStatus.CREATED)
                .data(createdBatch)
                .message("Batch created successfully")
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Batch>> updateBatch(@PathVariable UUID id, @RequestBody BatchDTO dto){
        logger.debug("Updating Batch with id={}", id);
        Batch updatedBatch = batchService.updateBatch(id, dto);
        return ResponseEntity.ok(ApiResponse.<Batch>builder()
                .status(HttpStatus.OK)
                .data(updatedBatch)
                .message("Batch updated successfully")
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteBatch(@PathVariable UUID id){
        logger.debug("Deleting Batch with id={}", id);
        batchService.deleteBatch(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("Batch deleted successfully")
                .build()
        );
    }
}
