package com.example.fitness.today.api;

import com.example.fitness.common.response.ApiResponse;
import com.example.fitness.today.application.TodaySummaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/today")
class TodaySummaryController {

    private final TodaySummaryService todaySummaryService;

    TodaySummaryController(TodaySummaryService todaySummaryService) {
        this.todaySummaryService = todaySummaryService;
    }

    @GetMapping("/summary")
    ApiResponse<TodaySummaryResponse> summary(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.ok(todaySummaryService.summary(userId, date));
    }
}
