package hhplus.hhpluscleanarchitecture.interfaces.api.lecture.dto.response;

import hhplus.hhpluscleanarchitecture.domain.lecture.LectureEnrollment;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class EnrolledLecturesResponse {
    private List<LectureEnrollment> enrollmentList;

    public static EnrolledLecturesResponse of(List<LectureEnrollment> enrollmentList) {
        return builder().enrollmentList(enrollmentList).build();
    }
}
