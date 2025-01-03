package hhplus.hhpluscleanarchitecture.domain.lecture;

import hhplus.hhpluscleanarchitecture.domain.BaseEntity;
import hhplus.hhpluscleanarchitecture.support.exception.lecture.LectureErrorCode;
import hhplus.hhpluscleanarchitecture.support.exception.lecture.LectureException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureEnrollment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long lectureId;
    private String lectureName;
    private String instructorName;
    private LocalDateTime lectureStartDtime;
    private LocalDateTime lectureEndDtime;
    private String location;

    public static LectureEnrollment create(Long userId, Long lectureId) {
        return LectureEnrollment.builder()
                .userId(userId)
                .lectureId(lectureId)
                .build();
    }

}
