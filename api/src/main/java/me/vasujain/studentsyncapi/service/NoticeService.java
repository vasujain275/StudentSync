package me.vasujain.studentsyncapi.service;

import me.vasujain.studentsyncapi.dto.CreateNoticeDTO;
import me.vasujain.studentsyncapi.model.Notice;
import me.vasujain.studentsyncapi.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    public List<Notice> getAllNotices(){
        return noticeRepository.findAll();
    }

    public Notice createNotice(CreateNoticeDTO dto){
        Notice notice = Notice.builder()
                .title(dto.getTitle())
                .notice(dto.getNotice())
                .date(dto.getDate())
                .build();

        return noticeRepository.save(notice);
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


}
