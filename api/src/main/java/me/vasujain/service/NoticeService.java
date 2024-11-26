package me.vasujain.service;

import me.vasujain.model.Notice;
import me.vasujain.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    public Notice createNotice(Notice notice) {
        return noticeRepository.save(notice);
    }

    public Notice updateNotice(UUID uuid, Notice noticeDetails) {
        // Find the existing notice
        Optional<Notice> existingNoticeOptional = noticeRepository.findById(uuid);

        // If notice exists, update its details
        if (existingNoticeOptional.isPresent()) {
            Notice existingNotice = existingNoticeOptional.get();
            existingNotice.setTitle(noticeDetails.getTitle());
            existingNotice.setNotice(noticeDetails.getNotice());
            existingNotice.setDate(noticeDetails.getDate());

            return noticeRepository.save(existingNotice);
        } else {
            // If no notice found with given UUID, you can either throw an exception
            // or create a new notice
            throw new RuntimeException("Notice not found with UUID: " + uuid);
        }
    }

    public void deleteNotice(UUID uuid) {
        // Check if notice exists before deleting
        Optional<Notice> existingNoticeOptional = noticeRepository.findById(uuid);

        if (existingNoticeOptional.isPresent()) {
            noticeRepository.delete(existingNoticeOptional.get());
        } else {
            throw new RuntimeException("Notice not found with UUID: " + uuid);
        }
    }

    public Notice getNoticeByUuid(UUID uuid) {
        return noticeRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("Notice not found with UUID: " + uuid));
    }
}