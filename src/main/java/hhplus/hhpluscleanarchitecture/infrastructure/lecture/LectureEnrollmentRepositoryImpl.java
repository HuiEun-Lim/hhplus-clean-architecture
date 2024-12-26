package hhplus.hhpluscleanarchitecture.infrastructure.lecture;

import hhplus.hhpluscleanarchitecture.domain.lecture.LectureEnrollment;
import hhplus.hhpluscleanarchitecture.domain.lecture.LectureEnrollmentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LectureEnrollmentRepositoryImpl implements LectureEnrollmentRepository {

    private final LectureEnrollmentJpaRepository enrollmentJpaRepository;

    private final long MAX_USER = 30;

    public LectureEnrollmentRepositoryImpl(LectureEnrollmentJpaRepository enrollmentJpaRepository) {
        this.enrollmentJpaRepository = enrollmentJpaRepository;
    }

    @Override
    public LectureEnrollment enrollLecture(LectureEnrollment lectureEnrollment) {
        return enrollmentJpaRepository.save(lectureEnrollment);
    }

    @Override
    public int countUserEnrolledLecture(LectureEnrollment lectureEnrollment) {
        return enrollmentJpaRepository.countByLectureIdAndUserId(lectureEnrollment.getLectureId(), lectureEnrollment.getUserId());
    }

    @Override
    public int countEnrollmentLectureWithLock(Long lectureId) {
        return enrollmentJpaRepository.countByLectureId(lectureId);
    }

    @Override
    public int countEnrollmentLecture(Long lectureId) {
        return enrollmentJpaRepository.countByLectureId(lectureId);
    }

    @Override
    public List<LectureEnrollment> selectMyEnrolledmentList(Long userId) {
        return enrollmentJpaRepository.findAllByUserId(userId);
    }
}
