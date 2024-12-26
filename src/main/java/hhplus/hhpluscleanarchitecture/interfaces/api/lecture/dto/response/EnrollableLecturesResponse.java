package hhplus.hhpluscleanarchitecture.interfaces.api.lecture.dto.response;

import hhplus.hhpluscleanarchitecture.domain.lecture.Lecture;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class EnrollableLecturesResponse {
    private List<Lecture> lectureList;

    public static EnrollableLecturesResponse of(List<Lecture> lectureList) {
        return builder().lectureList(lectureList).build();
    }
}
