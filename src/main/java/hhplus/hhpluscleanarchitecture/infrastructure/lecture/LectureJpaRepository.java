package hhplus.hhpluscleanarchitecture.infrastructure.lecture;

import hhplus.hhpluscleanarchitecture.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {

    @Query("SELECT l FROM Lecture l WHERE l.enrollStartDtime >= :searchDate AND l.lectureStartDtime < :searchDate")
    List<Lecture> findEnrollableLectureList(LocalDateTime searchDate);

}
