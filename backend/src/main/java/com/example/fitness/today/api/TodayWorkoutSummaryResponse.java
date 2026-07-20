package com.example.fitness.today.api;

import java.util.List;

public record TodayWorkoutSummaryResponse(
        int workoutCount,
        List<String> bodyParts,
        int totalSets,
        int estimatedCalories
) {
}
