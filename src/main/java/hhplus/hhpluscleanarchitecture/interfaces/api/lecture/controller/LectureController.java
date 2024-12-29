package hhplus.hhpluscleanarchitecture.interfaces.api.lecture.controller;

import hhplus.hhpluscleanarchitecture.domain.lecture.Lecture;
import hhplus.hhpluscleanarchitecture.domain.lecture.LectureEnrollment;
import hhplus.hhpluscleanarchitecture.domain.lecture.LectureService;
import hhplus.hhpluscleanarchitecture.interfaces.api.lecture.ApiResponse;
import hhplus.hhpluscleanarchitecture.interfaces.api.lecture.dto.request.EnrollLectureRequest;
import hhplus.hhpluscleanarchitecture.interfaces.api.lecture.dto.response.EnrollLectureResponse;
import hhplus.hhpluscleanarchitecture.interfaces.api.lecture.dto.response.EnrollableLecturesResponse;
import hhplus.hhpluscleanarchitecture.interfaces.api.lecture.dto.response.EnrolledLecturesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;

    @PostMapping
    public ApiResponse<EnrollLectureResponse> enrollLecture(
        @RequestBody EnrollLectureRequest req
    ) {
        LectureEnrollment lectureEnrollment = lectureService.enrollLecture(req.toEntity());
        return ApiResponse.ok(EnrollLectureResponse.of(lectureEnrollment));
    }

    @GetMapping
    public ApiResponse<EnrollableLecturesResponse> getEnrollableLectureList(
        @RequestParam LocalDateTime searchDate
    ) {
        if(searchDate == null) { searchDate = LocalDateTime.now(); }

        return ApiResponse.ok(EnrollableLecturesResponse.of(lectureService.selectEnrollabeLectureList(searchDate)));
    }

    @GetMapping("/enrollments")
    public ApiResponse<EnrolledLecturesResponse> getMyEnrolledLectureList(
        @RequestParam Long userId
    ) {
        return ApiResponse.ok(EnrolledLecturesResponse.of(lectureService.selectMyEnrolledLectureList(userId)));
    }
}
