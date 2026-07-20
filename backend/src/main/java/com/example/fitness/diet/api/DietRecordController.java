package com.example.fitness.diet.api;

import com.example.fitness.common.response.ApiResponse;
import com.example.fitness.diet.application.DietRecordService;
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
import java.util.List;

@RestController
@RequestMapping("/api/v1/diet-records")
class DietRecordController {

    private final DietRecordService dietRecordService;

    DietRecordController(DietRecordService dietRecordService) {
        this.dietRecordService = dietRecordService;
    }

    @PostMapping
    ApiResponse<DietRecordResponse> create(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateDietRecordRequest request
    ) {
        return ApiResponse.ok(dietRecordService.create(userId, request));
    }

    @GetMapping
    ApiResponse<List<DietRecordResponse>> findByDate(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.ok(dietRecordService.findByDate(userId, date));
    }

    @GetMapping("/summary")
    ApiResponse<DietSummaryResponse> summary(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.ok(dietRecordService.summary(userId, date));
    }
}
