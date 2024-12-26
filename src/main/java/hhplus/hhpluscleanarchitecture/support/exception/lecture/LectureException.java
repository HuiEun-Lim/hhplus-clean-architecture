package hhplus.hhpluscleanarchitecture.support.exception.lecture;

import hhplus.hhpluscleanarchitecture.support.exception.ErrorCode;
import lombok.Getter;

@Getter
public class LectureException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Object[] args;

    public LectureException(ErrorCode errorCode, Object... args) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.args = args;
    }
}
