package hhplus.hhpluscleanarchitecture.interfaces.api.lecture.dto.response;

import hhplus.hhpluscleanarchitecture.domain.lecture.LectureEnrollment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EnrollLectureResponse {
    private Long id;
    private Long userId;
    private Long lectureId;
    private String lectureName;
    private String instructorName;
    private LocalDateTime lectureStartDtime;
    private LocalDateTime lectureEndDtime;
    private String location;

    public static EnrollLectureResponse of(LectureEnrollment entity) {
        return EnrollLectureResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .lectureId(entity.getLectureId())
                .lectureName(entity.getLectureName())
                .instructorName(entity.getInstructorName())
                .lectureStartDtime(entity.getLectureStartDtime())
                .lectureEndDtime(entity.getLectureEndDtime())
                .location(entity.getLocation())
                .build();
    }
}
