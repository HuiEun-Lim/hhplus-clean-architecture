package hhplus.hhpluscleanarchitecture.infrastructure.lecture;

import hhplus.hhpluscleanarchitecture.domain.lecture.Lecture;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {

    @Query("SELECT l FROM Lecture l WHERE l.enrollStartDtime >= :searchDate AND l.lectureStartDtime < :searchDate")
    List<Lecture> findEnrollableLectureList(LocalDateTime searchDate);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Lecture l WHERE l.lectureId = :lectureId")
    Optional<Lecture> findByLectureIdWithLock(Long lectureId);
}
