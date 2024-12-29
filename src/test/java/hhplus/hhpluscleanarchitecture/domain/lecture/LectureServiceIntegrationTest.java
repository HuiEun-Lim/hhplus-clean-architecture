package hhplus.hhpluscleanarchitecture.domain.lecture;

import hhplus.hhpluscleanarchitecture.support.exception.lecture.LectureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LectureServiceIntegrationTest {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureEnrollmentRepository lectureEnrollmentRepository;

    private final int MAX_ENROLL_COUNT = 30;

    private Lecture lecture;

    @BeforeEach
    void setUp() {
        lecture = lectureRepository.save(Lecture.create("재영이의 노래 교실", "최재영", LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(2), "zep", LocalDateTime.now().minusDays(1)));
    }

    @Test
    @DisplayName("동시에 40명의 사용자가 한 특강을 신청한다.")
    void enrollLectureFortyUsersConcurrency() throws InterruptedException {
        // given
        int usersCount = 40;
        long lectureId = lecture.getLectureId();

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(usersCount);
        CountDownLatch latch = new CountDownLatch(usersCount);

        for (int i = 1; i <= usersCount; i++) {
            long userId = i;
            executorService.execute(() -> {
                try {
                    lectureService.enrollLecture(LectureEnrollment.create(userId, lectureId));
                } catch (LectureException e) {
                } finally {
                    latch.countDown();
                }
            });

        }

        latch.await();
        executorService.shutdown();

        // then
        int enrollmentCount = lectureEnrollmentRepository.countEnrollmentLecture(lectureId);
        assertThat(enrollmentCount).isEqualTo(MAX_ENROLL_COUNT);

    }

    @Test
    @DisplayName("한 사용자가 같은 특강을 5번 신청한다.")
    void enrollLectureOneUserFiveTimes() throws InterruptedException {
        // given
        int processCount = 5;
        long userId = 1;
        long lectureId = lecture.getLectureId();

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(processCount);
        CountDownLatch latch = new CountDownLatch(processCount);

        for (int i = 0; i < processCount; i++) {
            executorService.execute(() -> {
                try {
                    lectureService.enrollLecture(LectureEnrollment.create(userId, lectureId));
                } catch (LectureException e) {
                } finally {
                    latch.countDown();
                }
            });

        }

        latch.await();
        executorService.shutdown();

        // then
        int enrollmentCount = lectureEnrollmentRepository.countUserEnrolledLecture(LectureEnrollment.create(userId, lectureId));
        assertThat(enrollmentCount).isEqualTo(1);

    }

}