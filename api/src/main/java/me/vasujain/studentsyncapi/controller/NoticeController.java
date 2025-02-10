package me.vasujain.studentsyncapi.controller;

import me.vasujain.studentsyncapi.dto.NoticeDTO;
import me.vasujain.studentsyncapi.model.Notice;
import me.vasujain.studentsyncapi.response.ApiResponse;
import me.vasujain.studentsyncapi.service.NoticeService;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;
    private final Logger logger;

    public NoticeController(NoticeService noticeService, Logger logger) {
        this.noticeService = noticeService;
        this.logger = logger;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllNotices(
            @RequestParam(defaultValue = "false") boolean paginate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        logger.debug("Fetching Notices with pagination={} page={} size={}", paginate, page, size);

        Object result = noticeService.getNotices(paginate, PageRequest.of(page, size));

        if (result instanceof Page) {
            Page<Notice> noticePage = (Page<Notice>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(noticePage.getContent())
                    .pagination(ApiResponse.PaginationMetadata.builder()
                            .totalElements((int) noticePage.getTotalElements())
                            .currentPage(noticePage.getNumber())
                            .pageSize(noticePage.getSize())
                            .totalPages(noticePage.getTotalPages())
                            .build()
                    )
                    .build()
            );
        } else {
            List<Notice> notices = (List<Notice>) result;
            return ResponseEntity.ok(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(notices)
                    .build()
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Notice>> getNotice(@PathVariable UUID id){
        logger.debug("Fetching Notice with id={}", id);
        Notice notice = noticeService.getNotice(id);
        return ResponseEntity.ok(ApiResponse.<Notice>builder()
                .status(HttpStatus.OK)
                .data(notice)
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Notice>> createNotice(@RequestBody NoticeDTO dto){
        logger.debug("Creating a new Notice");
        Notice notice = noticeService.createNotice(dto);
        return ResponseEntity.ok(ApiResponse.<Notice>builder()
                .status(HttpStatus.CREATED)
                .data(notice)
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteNotice(@PathVariable UUID id){
        logger.debug("Deleting Notice with id={}", id);
        noticeService.deleteNotice(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("Notice deleted successfully")
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Notice>> updateNotice(@PathVariable UUID id, @RequestBody NoticeDTO dto){
        logger.debug("Updating Notice with id={}", id);
        Notice notice = noticeService.updateNotice(id, dto);
        return ResponseEntity.ok(ApiResponse.<Notice>builder()
                .status(HttpStatus.OK)
                .data(notice)
                .message("Notice updated successfully")
                .build()
        );
    }

}
