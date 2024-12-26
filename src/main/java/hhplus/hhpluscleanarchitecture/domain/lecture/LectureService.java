package hhplus.hhpluscleanarchitecture.domain.lecture;

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

        int userLectureCount = lectureEnrollmentRepository.countUserEnrolledLecture(lectureEnrollment);

        lectureEnrollment.checkEnrolledLecture(userLectureCount);

        int enrolledUserCount = lectureEnrollmentRepository.countEnrollmentLectureWithLock(lectureEnrollment.getLectureId());

        lectureEnrollment.checkMaxEnrollLecture(enrolledUserCount, MAX_ENROLL_COUNT);

        Lecture lecture = lectureRepository.selectLecture(lectureEnrollment.getLectureId());

        LectureEnrollment insertEnrollment = lecture.toLectureEnrollment(lectureEnrollment.getUserId());

        return lectureEnrollmentRepository.enrollLecture(insertEnrollment);
    }

}
