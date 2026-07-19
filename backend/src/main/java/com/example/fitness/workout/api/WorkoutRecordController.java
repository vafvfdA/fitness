package com.example.fitness.workout.api;

import com.example.fitness.common.response.ApiResponse;
import com.example.fitness.workout.application.WorkoutRecordService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/v1/workout-records")
class WorkoutRecordController {

    private final WorkoutRecordService workoutRecordService;

    WorkoutRecordController(WorkoutRecordService workoutRecordService) {
        this.workoutRecordService = workoutRecordService;
    }

    @PostMapping
    ApiResponse<WorkoutRecordResponse> create(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateWorkoutRecordRequest request
    ) {
        return ApiResponse.ok(workoutRecordService.create(userId, request));
    }

    @GetMapping
    ApiResponse<List<WorkoutRecordResponse>> findByDate(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.ok(workoutRecordService.findByDate(userId, date));
    }

    @GetMapping("/calendar")
    ApiResponse<WorkoutCalendarResponse> calendar(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        return ApiResponse.ok(workoutRecordService.calendar(userId, month));
    }
}
