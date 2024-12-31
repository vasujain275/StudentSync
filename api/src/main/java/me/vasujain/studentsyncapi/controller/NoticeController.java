package me.vasujain.studentsyncapi.controller;

import me.vasujain.studentsyncapi.dto.CreateNoticeDTO;
import me.vasujain.studentsyncapi.model.Notice;
import me.vasujain.studentsyncapi.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    // GET endpoint to retrieve all notices
    @GetMapping
    public ResponseEntity<List<Notice>> getAllNotices() {
        List<Notice> notices = noticeService.getAllNotices();
        return ResponseEntity.ok(notices);
    }

    @PostMapping
    public ResponseEntity<Notice> createNotice(@RequestBody CreateNoticeDTO createNoticeDTO){
        Notice notice = noticeService.createNotice(createNoticeDTO);
        return ResponseEntity.ok(notice);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteNotice(@RequestParam("id") UUID uuid) {
        noticeService.deleteNotice(uuid);
        return ResponseEntity.noContent().build();
    }

}
