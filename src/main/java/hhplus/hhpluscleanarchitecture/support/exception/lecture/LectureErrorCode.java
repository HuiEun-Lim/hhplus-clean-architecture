package hhplus.hhpluscleanarchitecture.support.exception.lecture;

import hhplus.hhpluscleanarchitecture.support.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LectureErrorCode implements ErrorCode {

    MAX_ENROLLMENT_LECTURE(HttpStatus.BAD_REQUEST, "해당 특강은 신청 가능한 인원 수를 초과하였습니다."),
    USER_ENROLLED_LECTURE(HttpStatus.BAD_REQUEST, "이미 신청한 특강입니다."),
    LECTURE_IS_NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 특강입니다.")
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
