package me.vasujain.studentsyncapi.service;

import me.vasujain.studentsyncapi.annotation.TrackPerformance;
import me.vasujain.studentsyncapi.dto.NoticeDTO;
import me.vasujain.studentsyncapi.exception.ResourceNotFoundException;
import me.vasujain.studentsyncapi.model.Notice;
import me.vasujain.studentsyncapi.repository.NoticeRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final Logger logger;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository,
                         Logger logger){
        this.noticeRepository = noticeRepository;
        this.logger = logger;
    }

    public Object getNotices(boolean paginate, PageRequest pageRequest){
        logger.info("Fetching Notices with pagination={}", paginate);
        if(paginate){
            return noticeRepository.findAll(pageRequest);
        } else {
            return noticeRepository.findAll();
        }
    }


    @TrackPerformance(value = "notice.findAll",
            description = "Get all notices performance",
            tags = {"entity:notice", "operation:findAll"})
    public Notice getNotice(UUID id){
        return noticeRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Notice not found with id -" + id));
    }

    public Notice createNotice(NoticeDTO dto){
        logger.info("Creating a new Notice");
        Notice notice = Notice.builder()
                .title(dto.getTitle())
                .notice(dto.getNotice())
                .date(dto.getDate())
                .build();
        return noticeRepository.save(notice);
    }

    public void deleteNotice(UUID id){
        noticeRepository.deleteById(id);
    }

    public Notice updateNotice(UUID id, NoticeDTO dto){
        Notice notice = getNotice(id);
        if(dto.getTitle() != null) notice.setTitle(dto.getTitle());
        if(dto.getNotice() != null) notice.setNotice(dto.getNotice());
        if(dto.getDate() != null) notice.setDate(dto.getDate());

        return noticeRepository.save(notice);
    }
}
