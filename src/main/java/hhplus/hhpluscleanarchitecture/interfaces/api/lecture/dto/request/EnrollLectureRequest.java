package hhplus.hhpluscleanarchitecture.interfaces.api.lecture.dto.request;

import hhplus.hhpluscleanarchitecture.domain.lecture.LectureEnrollment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@NoArgsConstructor
public class EnrollLectureRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long lectureId;

    public EnrollLectureRequest(long userId, long lectureId) {
        this.userId = userId;
        this.lectureId = lectureId;
    }

    public LectureEnrollment toEntity() {
        LectureEnrollment entity = new LectureEnrollment();
        return LectureEnrollment.builder()
                .userId(this.userId)
                .lectureId(this.lectureId)
                .build();
    }
}
