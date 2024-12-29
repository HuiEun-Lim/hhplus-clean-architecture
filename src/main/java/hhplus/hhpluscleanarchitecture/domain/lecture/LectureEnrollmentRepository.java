package hhplus.hhpluscleanarchitecture.domain.lecture;

import java.util.List;

public interface LectureEnrollmentRepository {

    LectureEnrollment enrollLecture(LectureEnrollment lectureEnrollment);

    int countUserEnrolledLecture(LectureEnrollment lectureEnrollment);

    int countEnrollmentLecture(Long lectureId);

    List<LectureEnrollment> selectMyEnrolledmentList(Long userId);

}
