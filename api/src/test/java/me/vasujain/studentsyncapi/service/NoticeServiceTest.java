package me.vasujain.studentsyncapi.service;

import me.vasujain.studentsyncapi.dto.NoticeDTO;
import me.vasujain.studentsyncapi.exception.ResourceNotFoundException;
import me.vasujain.studentsyncapi.model.Notice;
import me.vasujain.studentsyncapi.repository.NoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private NoticeService noticeService;

    private UUID noticeId;
    private Notice notice;
    private NoticeDTO noticeDTO;

    @BeforeEach
    void setUp() {
        noticeId = UUID.randomUUID();
        notice = Notice.builder()
                .id(noticeId)
                .title("Test Notice")
                .notice("This is a test notice")
                .date(LocalDate.now())
                .build();

        noticeDTO = new NoticeDTO();
        noticeDTO.setTitle("Test Notice");
        noticeDTO.setNotice("This is a test notice");
        noticeDTO.setDate(LocalDate.now());
    }

    @Test
    void shouldGetAllNoticesWithoutPagination() {
        // Arrange
        List<Notice> notices = Arrays.asList(notice);
        when(noticeRepository.findAll()).thenReturn(notices);

        // Act
        Object result = noticeService.getNotices(false, PageRequest.of(0, 10));

        // Assert
        assertThat(result).isInstanceOf(List.class);
        assertThat((List<Notice>) result).hasSize(1);
        verify(logger).info("Fetching Notices with pagination={}", false);
    }

    @Test
    void shouldGetAllNoticesWithPagination() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Notice> notices = Arrays.asList(notice);
        Page<Notice> page = new PageImpl<>(notices, pageRequest, notices.size());
        when(noticeRepository.findAll(pageRequest)).thenReturn(page);

        // Act
        Object result = noticeService.getNotices(true, pageRequest);

        // Assert
        assertThat(result).isInstanceOf(Page.class);
        Page<Notice> resultPage = (Page<Notice>) result;
        assertThat(resultPage.getContent()).hasSize(1);
        verify(logger).info("Fetching Notices with pagination={}", true);
    }

    @Test
    void shouldGetNoticeById() {
        // Arrange
        when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));

        // Act
        Notice result = noticeService.getNotice(noticeId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(noticeId);
    }

    @Test
    void shouldThrowExceptionWhenNoticeNotFound() {
        // Arrange
        when(noticeRepository.findById(noticeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            noticeService.getNotice(noticeId);
        });
    }

    @Test
    void shouldCreateNotice() {
        // Arrange
        when(noticeRepository.save(any(Notice.class))).thenReturn(notice);

        // Act
        Notice result = noticeService.createNotice(noticeDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(noticeDTO.getTitle());
        verify(logger).info("Creating a new Notice");
    }

    @Test
    void shouldDeleteNotice() {
        // Act
        noticeService.deleteNotice(noticeId);

        // Assert
        verify(noticeRepository).deleteById(noticeId);
    }

    @Test
    void shouldUpdateNotice() {
        // Arrange
        when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));
        noticeDTO.setTitle("Updated Notice");
        when(noticeRepository.save(any(Notice.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Notice result = noticeService.updateNotice(noticeId, noticeDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Notice");
    }

    @Test
    void shouldPartiallyUpdateNotice() {
        // Arrange
        when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));
        NoticeDTO partialDTO = new NoticeDTO();
        partialDTO.setTitle("Updated Title");
        when(noticeRepository.save(any(Notice.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Notice result = noticeService.updateNotice(noticeId, partialDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        // Other fields should remain unchanged
        assertThat(result.getNotice()).isEqualTo("This is a test notice");
    }
}