package hhplus.hhpluscleanarchitecture.domain.lecture;

import hhplus.hhpluscleanarchitecture.support.exception.lecture.LectureErrorCode;
import hhplus.hhpluscleanarchitecture.support.exception.lecture.LectureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final LectureEnrollmentRepository lectureEnrollmentRepository;

    private final int MAX_ENROLL_COUNT = 30;

    @Transactional
    public List<Lecture> selectEnrollabeLectureList (LocalDateTime searchDate) {

        List<Lecture> lectureList = lectureRepository.selectEnrollableLectureList(searchDate);

        // 신청 인원 초과 여부 확인
        return lectureList.stream()
                .filter(lecture -> {
                    return lectureEnrollmentRepository.countEnrollmentLecture(lecture.getLectureId()) < MAX_ENROLL_COUNT;
                })
                .toList();
    }

    @Transactional
    public List<LectureEnrollment> selectMyEnrolledLectureList (Long userId) {
        return lectureEnrollmentRepository.selectMyEnrolledmentList(userId);
    }

    @Transactional
    public LectureEnrollment enrollLecture (LectureEnrollment lectureEnrollment) {

        Lecture lecture = lectureRepository.selectLecture(lectureEnrollment.getLectureId());

        int userLectureCount = lectureEnrollmentRepository.countUserEnrolledLecture(lectureEnrollment);

        this.checkEnrolledLecture(userLectureCount);

        int enrolledUserCount = lectureEnrollmentRepository.countEnrollmentLecture(lectureEnrollment.getLectureId());

        this.checkMaxEnrollLecture(enrolledUserCount);

        LectureEnrollment insertEnrollment = lecture.toLectureEnrollment(lectureEnrollment.getUserId());

        return lectureEnrollmentRepository.enrollLecture(insertEnrollment);
    }

    private void checkEnrolledLecture(int count) {
        if (count > 0) {
            throw new LectureException(LectureErrorCode.USER_ENROLLED_LECTURE);
        }
    }

    private void checkMaxEnrollLecture(int count) {
        if (count >= MAX_ENROLL_COUNT) {
            throw new LectureException(LectureErrorCode.MAX_ENROLLMENT_LECTURE);
        }
    }

}
