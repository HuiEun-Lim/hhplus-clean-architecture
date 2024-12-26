package hhplus.hhpluscleanarchitecture.domain.lecture;

import hhplus.hhpluscleanarchitecture.support.exception.lecture.LectureErrorCode;
import hhplus.hhpluscleanarchitecture.support.exception.lecture.LectureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private LectureEnrollmentRepository lectureEnrollmentRepository;

    @InjectMocks
    private LectureService lectureService;

    private final int MAX_ENROLL_COUNT = 30;

    @Test
    @DisplayName("신청 인원이 30 이상인 특강을 제외하고 특강 목록을 조회한다.")
    void selectLessThanThirtyEnrollmnetsLectureList() {
        // given
        LocalDateTime mockTime = LocalDateTime.of(2024, 12, 27, 12, 0);
        List<Lecture> mockLectures = List.of(
                new Lecture(1L, "Lecture 1", "Instructor 1", LocalDateTime.of(2024, 12, 30, 13, 0), LocalDateTime.of(2024, 12, 30, 14, 30), "Room A", LocalDateTime.of(2024, 12, 27, 11, 0)),
                new Lecture(2L, "Lecture 2", "Instructor 2", LocalDateTime.of(2024, 12, 31, 14, 0), LocalDateTime.of(2024, 12, 31, 15, 0), "Room B", LocalDateTime.of(2024, 12, 26, 12, 0))
        );

        // when
        when(lectureRepository.selectEnrollableLectureList(mockTime)).thenReturn(mockLectures);
        when(lectureEnrollmentRepository.countEnrollmentLecture(1L)).thenReturn(20);
        when(lectureEnrollmentRepository.countEnrollmentLecture(2L)).thenReturn(31);

        List<Lecture> result = lectureService.selectEnrollabeLectureList(mockTime);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Lecture 1", result.get(0).getLectureName());

        verify(lectureRepository, times(1)).selectEnrollableLectureList(mockTime);
        verify(lectureEnrollmentRepository, times(1)).countEnrollmentLecture(1L);
        verify(lectureEnrollmentRepository, times(1)).countEnrollmentLecture(2L);
    }

    @Test
    @DisplayName("사용자가 신청한 특강 목록을 조회한다.")
    void selectMyEnrolledLectureList() {
        // Given
        Long userId = 1L;
        List<LectureEnrollment> mockEnrollments = List.of(
                new LectureEnrollment(1L, userId, 1L,"Lecture 1", "Instructor 1", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Room A"),
                new LectureEnrollment(2L, userId, 2L, "Lecture 2", "Instructor 2", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Room B")
        );

        when(lectureEnrollmentRepository.selectMyEnrolledmentList(userId)).thenReturn(mockEnrollments);

        // When
        List<LectureEnrollment> result = lectureService.selectMyEnrolledLectureList(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Lecture 1", result.get(0).getLectureName());
        assertEquals("Lecture 2", result.get(1).getLectureName());

        verify(lectureEnrollmentRepository, times(1)).selectMyEnrolledmentList(userId);
    }

    @Test
    @DisplayName("사용자가 특강을 신청한다.")
    void enrollLecture() {
        // Given
        LectureEnrollment mockEnrollment = new LectureEnrollment(1L, 1L, 1L, "Lecture 1", "Instructor 1", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Room A");
        when(lectureEnrollmentRepository.countUserEnrolledLecture(mockEnrollment)).thenReturn(0);
        when(lectureEnrollmentRepository.countEnrollmentLectureWithLock(1L)).thenReturn(25);
        Lecture mockLecture = new Lecture(1L, "Lecture 1", "Instructor 1", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Room A", LocalDateTime.now().minusDays(1));
        when(lectureRepository.selectLecture(1L)).thenReturn(mockLecture);
        when(lectureEnrollmentRepository.enrollLecture(any())).thenReturn(mockEnrollment);

        // When
        LectureEnrollment result = lectureService.enrollLecture(mockEnrollment);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getLectureId());
        assertEquals("Lecture 1", result.getLectureName());

        verify(lectureEnrollmentRepository, times(1)).countUserEnrolledLecture(mockEnrollment);
        verify(lectureEnrollmentRepository, times(1)).countEnrollmentLectureWithLock(1L);
        verify(lectureRepository, times(1)).selectLecture(1L);
        verify(lectureEnrollmentRepository, times(1)).enrollLecture(any());
    }

    @Test
    @DisplayName("사용자가 이미 신청한 특강을 신청한다.")
    void enrollAlreadyEnrolledLecture() {
        // Given
        LectureEnrollment mockEnrollment = new LectureEnrollment(1L, 1L, 1L, "Lecture 1", "Instructor 1", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Room A");
        when(lectureEnrollmentRepository.countUserEnrolledLecture(mockEnrollment)).thenReturn(1); // 이미 신청한 상태

        // When & Then
        assertThatThrownBy(() -> lectureService.enrollLecture(mockEnrollment))
                .isInstanceOf(LectureException.class)
                .hasMessage(LectureErrorCode.USER_ENROLLED_LECTURE.getMessage());

        verify(lectureEnrollmentRepository, times(1)).countUserEnrolledLecture(mockEnrollment);
        verifyNoMoreInteractions(lectureEnrollmentRepository);
        verifyNoInteractions(lectureRepository);
    }

    @Test
    @DisplayName("사용자가 신청 최대 인원이 초과된 특강을 신청한다.")
    void enrollLectureWhenMaxEnrollment() {
        // Given
        LectureEnrollment mockEnrollment = new LectureEnrollment(1L, 1L, 1L, "Lecture 1", "Instructor 1", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Room A");
        when(lectureEnrollmentRepository.countUserEnrolledLecture(mockEnrollment)).thenReturn(0);
        when(lectureEnrollmentRepository.countEnrollmentLectureWithLock(1L)).thenReturn(30);

        // When & Then
        assertThatThrownBy(() -> lectureService.enrollLecture(mockEnrollment))
                .isInstanceOf(LectureException.class)
                .hasMessage(LectureErrorCode.MAX_ENROLLMENT_LECTURE.getMessage());

        verify(lectureEnrollmentRepository, times(1)).countUserEnrolledLecture(mockEnrollment);
        verify(lectureEnrollmentRepository, times(1)).countEnrollmentLectureWithLock(1L);
        verifyNoMoreInteractions(lectureRepository);
        verify(lectureEnrollmentRepository, never()).enrollLecture(any());
    }

}