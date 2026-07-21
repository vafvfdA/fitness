package com.example.fitness.plan.api;

import com.example.fitness.common.response.ApiResponse;
import com.example.fitness.plan.application.TrainingPlanService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/plans")
class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    TrainingPlanController(TrainingPlanService trainingPlanService) {
        this.trainingPlanService = trainingPlanService;
    }

    @GetMapping("/current")
    ApiResponse<TrainingPlanResponse> getCurrent(@RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.ok(trainingPlanService.getCurrent(userId));
    }

    @PutMapping("/current")
    ApiResponse<TrainingPlanResponse> updateCurrent(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody UpdateTrainingPlanRequest request
    ) {
        return ApiResponse.ok(trainingPlanService.updateCurrent(userId, request));
    }

    @GetMapping("/current/today")
    ApiResponse<TodayPlanResponse> getToday(@RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.ok(trainingPlanService.getToday(userId));
    }
}
