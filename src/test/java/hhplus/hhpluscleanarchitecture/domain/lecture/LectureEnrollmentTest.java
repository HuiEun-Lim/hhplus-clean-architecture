package hhplus.hhpluscleanarchitecture.domain.lecture;

import hhplus.hhpluscleanarchitecture.support.exception.lecture.LectureErrorCode;
import hhplus.hhpluscleanarchitecture.support.exception.lecture.LectureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LectureEnrollmentTest {

    @DisplayName("사용자가 이미 신청한 특강이다.")
    @Test
    void isEnrolledLecture() {
        // given
        LectureEnrollment enrollment = LectureEnrollment.create(1L, 1L);
        int count = 1;

        // when
        // then
        assertThatThrownBy(() -> enrollment.checkEnrolledLecture(count))
                .isInstanceOf(LectureException.class)
                .hasMessage(LectureErrorCode.USER_ENROLLED_LECTURE.getMessage());
    }

    @DisplayName("사용자가 신청하지 않은 특강이다.")
    @Test
    void isNotEnrolledLecture() {
        // given
        LectureEnrollment enrollment = LectureEnrollment.create(1L, 1L);
        int count = 0;

        // when
        boolean result = enrollment.checkEnrolledLecture(count);

        // then
        assertThat(result);
    }

    @DisplayName("사용자가 신청한 특강의 신청 수가 30개 이상이다.")
    @Test
    void MoreThanOrEqualsToThirtyEnrollments() {
        // given
        int count = 30;
        int max = 30;
        LectureEnrollment enrollment = LectureEnrollment.create(1L, 1L);

        // when
        // then
        assertThatThrownBy(() -> enrollment.checkMaxEnrollLecture(count, max))
                .isInstanceOf(LectureException.class)
                .hasMessage(LectureErrorCode.MAX_ENROLLMENT_LECTURE.getMessage());
    }

    @DisplayName("사용자가 신청한 특강의 신청 수가 30개 미만이다.")
    @Test
    void LessThanThirtyEnrollments() {
        // given
        int count = 29;
        int max = 30;
        LectureEnrollment enrollment = LectureEnrollment.create(1L, 1L);

        // when
        boolean result = enrollment.checkMaxEnrollLecture(count, max);

        // then
        assertThat(result);
    }
}