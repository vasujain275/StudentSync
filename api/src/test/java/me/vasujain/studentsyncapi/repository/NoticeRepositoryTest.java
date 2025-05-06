package me.vasujain.studentsyncapi.repository;

import me.vasujain.studentsyncapi.model.Notice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class NoticeRepositoryTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    public void shouldSaveNotice() {
        // Arrange
        Notice notice = Notice.builder()
                .title("Test Notice")
                .notice("This is a test notice")
                .date(LocalDate.now())
                .build();

        // Act
        Notice savedNotice = noticeRepository.save(notice);

        // Assert
        assertThat(savedNotice).isNotNull();
        assertThat(savedNotice.getId()).isNotNull();
        assertThat(savedNotice.getTitle()).isEqualTo("Test Notice");
    }

    @Test
    public void shouldFindNoticeById() {
        // Arrange
        Notice notice = Notice.builder()
                .title("Test Notice")
                .notice("This is a test notice")
                .date(LocalDate.now())
                .build();
        Notice savedNotice = noticeRepository.save(notice);
        UUID id = savedNotice.getId();

        // Act
        Optional<Notice> foundNotice = noticeRepository.findById(id);

        // Assert
        assertThat(foundNotice).isPresent();
        assertThat(foundNotice.get().getTitle()).isEqualTo("Test Notice");
    }

    @Test
    public void shouldFindAllNotices() {
        // Arrange
        Notice notice1 = Notice.builder()
                .title("Test Notice 1")
                .notice("This is test notice 1")
                .date(LocalDate.now())
                .build();

        Notice notice2 = Notice.builder()
                .title("Test Notice 2")
                .notice("This is test notice 2")
                .date(LocalDate.now())
                .build();

        noticeRepository.save(notice1);
        noticeRepository.save(notice2);

        // Act
        List<Notice> notices = noticeRepository.findAll();

        // Assert
        assertThat(notices).isNotEmpty();
        assertThat(notices).hasSize(2);
    }

    @Test
    public void shouldUpdateNotice() {
        // Arrange
        Notice notice = Notice.builder()
                .title("Test Notice")
                .notice("This is a test notice")
                .date(LocalDate.now())
                .build();
        Notice savedNotice = noticeRepository.save(notice);

        // Act
        savedNotice.setTitle("Updated Test Notice");
        Notice updatedNotice = noticeRepository.save(savedNotice);

        // Assert
        assertThat(updatedNotice.getTitle()).isEqualTo("Updated Test Notice");
    }

    @Test
    public void shouldDeleteNotice() {
        // Arrange
        Notice notice = Notice.builder()
                .title("Test Notice")
                .notice("This is a test notice")
                .date(LocalDate.now())
                .build();
        Notice savedNotice = noticeRepository.save(notice);
        UUID id = savedNotice.getId();

        // Act
        noticeRepository.deleteById(id);
        Optional<Notice> deletedNotice = noticeRepository.findById(id);

        // Assert
        assertThat(deletedNotice).isEmpty();
    }
}