package me.vasujain.controller;

import me.vasujain.model.Notice;
import me.vasujain.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    // GET endpoint to retrieve all notices
    @GetMapping
    public ResponseEntity<List<Notice>> getAllNotices() {
        List<Notice> notices = noticeService.getAllNotices();
        return ResponseEntity.ok(notices);
    }

    // POST endpoint to create a new notice
    @PostMapping
    public ResponseEntity<Notice> createNotice(@RequestBody Notice notice) {
        Notice createdNotice = noticeService.createNotice(notice);
        return ResponseEntity.ok(createdNotice);
    }

    // PUT endpoint to update an existing notice by UUID query param
    @PutMapping
    public ResponseEntity<Notice> updateNotice(
            @RequestParam("id") UUID uuid,
            @RequestBody Notice noticeDetails
    ) {
        Notice updatedNotice = noticeService.updateNotice(uuid, noticeDetails);
        return ResponseEntity.ok(updatedNotice);
    }

    // DELETE endpoint to delete a notice by UUID query param
    @DeleteMapping
    public ResponseEntity<Void> deleteNotice(@RequestParam("id") UUID uuid) {
        noticeService.deleteNotice(uuid);
        return ResponseEntity.noContent().build();
    }

    // GET endpoint to retrieve a single notice by UUID query param
    @GetMapping("/details")
    public ResponseEntity<Notice> getNoticeByUuid(@RequestParam("id") UUID uuid) {
        Notice notice = noticeService.getNoticeByUuid(uuid);
        return ResponseEntity.ok(notice);
    }
}