package com.example.fitness.workout.template.api;

import com.example.fitness.common.response.ApiResponse;
import com.example.fitness.workout.api.WorkoutRecordResponse;
import com.example.fitness.workout.template.application.WorkoutTemplateService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workout-templates")
class WorkoutTemplateController {

    private final WorkoutTemplateService workoutTemplateService;

    WorkoutTemplateController(WorkoutTemplateService workoutTemplateService) {
        this.workoutTemplateService = workoutTemplateService;
    }

    @PostMapping
    ApiResponse<WorkoutTemplateResponse> create(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateWorkoutTemplateRequest request
    ) {
        return ApiResponse.ok(workoutTemplateService.create(userId, request));
    }

    @GetMapping
    ApiResponse<List<WorkoutTemplateResponse>> findAll(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) String bodyPart
    ) {
        return ApiResponse.ok(workoutTemplateService.findAll(userId, bodyPart));
    }

    @GetMapping("/{id}")
    ApiResponse<WorkoutTemplateResponse> findById(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id
    ) {
        return ApiResponse.ok(workoutTemplateService.findById(userId, id));
    }

    @PostMapping("/{id}/workout-records")
    ApiResponse<WorkoutRecordResponse> createWorkoutRecord(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id,
            @RequestBody CreateWorkoutFromTemplateRequest request
    ) {
        return ApiResponse.ok(workoutTemplateService.createWorkoutRecord(userId, id, request));
    }
}
