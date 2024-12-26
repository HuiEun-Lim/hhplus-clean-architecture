package hhplus.hhpluscleanarchitecture.domain.lecture;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureRepository {

    public List<Lecture> selectEnrollableLectureList(LocalDateTime searchDate);

    public Lecture selectLecture(Long lectureId);

}
