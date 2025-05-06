package me.vasujain.studentsyncapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.vasujain.studentsyncapi.dto.NoticeDTO;
import me.vasujain.studentsyncapi.exception.ResourceNotFoundException;
import me.vasujain.studentsyncapi.model.Notice;
import me.vasujain.studentsyncapi.service.NoticeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = NoticeController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class NoticeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
    void createNotice() throws Exception {
        when(noticeService.createNotice(any(NoticeDTO.class))).thenReturn(notice);

        mockMvc.perform(post("/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noticeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CREATED")))
                .andExpect(jsonPath("$.data.title", is("Test Notice")));
    }

    @Test
    void getNoticeById_notFound() throws Exception {
        when(noticeService.getNotice(noticeId)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/notice/{id}", noticeId))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNotice() throws Exception {
        doNothing().when(noticeService).deleteNotice(noticeId);

        mockMvc.perform(delete("/notice/{id}", noticeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("OK")));
    }

    @Test
    void listNotices() throws Exception {
        List<Notice> notices = Arrays.asList(notice);
        when(noticeService.getNotices(eq(false), any())).thenReturn(notices);

        mockMvc.perform(get("/notice").param("paginate", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].title", is("Test Notice")));
    }
}
