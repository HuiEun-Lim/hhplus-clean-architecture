package hhplus.hhpluscleanarchitecture.infrastructure.lecture;

import hhplus.hhpluscleanarchitecture.domain.lecture.Lecture;
import hhplus.hhpluscleanarchitecture.domain.lecture.LectureRepository;
import hhplus.hhpluscleanarchitecture.support.exception.lecture.LectureErrorCode;
import hhplus.hhpluscleanarchitecture.support.exception.lecture.LectureException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class LectureRepositoryImpl implements LectureRepository {

    private final LectureJpaRepository lectureJpaRepository;

    public LectureRepositoryImpl(LectureJpaRepository lectureJpaRepository) {
        this.lectureJpaRepository = lectureJpaRepository;
    }

    @Override
    public List<Lecture> selectEnrollableLectureList(LocalDateTime searchDate) {
        return lectureJpaRepository.findEnrollableLectureList(searchDate);
    }

    @Override
    public Lecture selectLecture(Long lectureId) {
        Optional<Lecture> lecture = lectureJpaRepository.findByLectureIdWithLock(lectureId);

        if(lecture.isEmpty()) {
            throw new LectureException(LectureErrorCode.LECTURE_IS_NOT_EXISTS);
        }

        return lecture.get();
    }

    @Override
    public Lecture save(Lecture lecture) {
        return lectureJpaRepository.save(lecture);
    }
}
