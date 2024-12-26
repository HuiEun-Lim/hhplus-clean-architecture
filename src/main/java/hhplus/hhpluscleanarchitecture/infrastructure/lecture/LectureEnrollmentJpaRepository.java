package hhplus.hhpluscleanarchitecture.infrastructure.lecture;

import hhplus.hhpluscleanarchitecture.domain.lecture.LectureEnrollment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LectureEnrollmentJpaRepository extends JpaRepository<LectureEnrollment, Long> {

    /* 특강 아이디와 사용자 아이디로 신청 수 조회 */
    int countByLectureIdAndUserId(Long lectureId, Long userId);

    /* 특강 아이디로 특강 신청 수 조회 */
    int countByLectureId(Long lectureId);

    /* 사용자 아이디로 신청한 특강 목록 조회 */
    List<LectureEnrollment> findAllByUserId(Long userId);

}
