package hhplus.hhpluscleanarchitecture.domain.lecture;

import hhplus.hhpluscleanarchitecture.domain.BaseEntity;
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
public class Lecture extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureId;

    private String lectureName;
    private String instructorName;
    private LocalDateTime lectureStartDtime;
    private LocalDateTime lectureEndDtime;
    private String location;

    private LocalDateTime enrollStartDtime;

    public static Lecture create(String lectureName, String instructorName, LocalDateTime lectureStartDtime, LocalDateTime lectureEndDtime, String location, LocalDateTime enrollStartDtime) {
        return Lecture.builder()
                .lectureName(lectureName)
                .instructorName(instructorName)
                .lectureStartDtime(lectureStartDtime)
                .lectureEndDtime(lectureEndDtime)
                .location(location)
                .enrollStartDtime(enrollStartDtime)
                .build();
    }

    public LectureEnrollment toLectureEnrollment(long userId) {

        return LectureEnrollment.builder()
                .userId(userId)
                .lectureId(this.lectureId)
                .lectureName(this.lectureName)
                .instructorName(this.instructorName)
                .lectureStartDtime(this.lectureStartDtime)
                .lectureEndDtime(this.lectureEndDtime)
                .location(this.location)
                .build();
    }

}
