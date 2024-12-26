package hhplus.hhpluscleanarchitecture.interfaces.api.lecture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hhplus.hhpluscleanarchitecture.domain.lecture.Lecture;
import hhplus.hhpluscleanarchitecture.domain.lecture.LectureEnrollment;
import hhplus.hhpluscleanarchitecture.domain.lecture.LectureService;
import hhplus.hhpluscleanarchitecture.interfaces.api.lecture.dto.request.EnrollLectureRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith(MockitoExtension.class)
class LectureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LectureService lectureService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("특강 신청 API를 실행한다")
    void enrollLectureAPI() throws Exception {
        // given
        EnrollLectureRequest request = new EnrollLectureRequest(1L, 1L);

        LectureEnrollment mockEnrollment = LectureEnrollment.builder()
                .id(1L)
                .userId(1L)
                .lectureId(1L)
                .lectureName("컨트롤러 테스트")
                .instructorName("구글링")
                .lectureStartDtime(LocalDateTime.now())
                .lectureEndDtime(LocalDateTime.now().plusHours(1))
                .location("구글")
                .build();

        when(lectureService.enrollLecture(any())).thenReturn(mockEnrollment);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/lectures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("신청 가능한 특강 목록 조회 API를 실행한다")
    void getEnrollableLectureList() throws Exception {
        // Given
        List<Lecture> mockLectures = List.of(
                Lecture.builder()
                        .lectureId(1L)
                        .lectureName("Lecture 1")
                        .instructorName("Instructor 1")
                        .lectureStartDtime(LocalDateTime.now().plusDays(1))
                        .lectureEndDtime(LocalDateTime.now().plusDays(2))
                        .location("Room A")
                        .build(),
                Lecture.builder()
                        .lectureId(2L)
                        .lectureName("Lecture 2")
                        .instructorName("Instructor 2")
                        .lectureStartDtime(LocalDateTime.now().plusDays(3))
                        .lectureEndDtime(LocalDateTime.now().plusDays(4))
                        .location("Room B")
                        .build()
        );

        when(lectureService.selectEnrollabeLectureList(any())).thenReturn(mockLectures);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/lectures")
                        .param("searchDate", LocalDateTime.now().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자가 신청한 특강 목록 조회 API를 실행한다.")
    void getMyEnrolledLectureList() throws Exception {
        // Given
        List<LectureEnrollment> mockEnrollments = List.of(
                LectureEnrollment.builder()
                        .id(1L)
                        .userId(1L)
                        .lectureId(1L)
                        .lectureName("Lecture 1")
                        .instructorName("Instructor 1")
                        .lectureStartDtime(LocalDateTime.now())
                        .lectureEndDtime(LocalDateTime.now().plusHours(1))
                        .location("Room A")
                        .build(),
                LectureEnrollment.builder()
                        .id(2L)
                        .userId(1L)
                        .lectureId(2L)
                        .lectureName("Lecture 2")
                        .instructorName("Instructor 2")
                        .lectureStartDtime(LocalDateTime.now())
                        .lectureEndDtime(LocalDateTime.now().plusHours(2))
                        .location("Room B")
                        .build()
        );

        when(lectureService.selectMyEnrolledLectureList(any())).thenReturn(mockEnrollments);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/lectures/enrollments")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}