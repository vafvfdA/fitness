package com.example.fitness.workout.template.api;

import java.math.BigDecimal;

public record WorkoutTemplateItemResponse(
        Long id,
        String exerciseName,
        int defaultSets,
        Integer defaultReps,
        BigDecimal defaultWeightKg,
        Integer defaultDurationSeconds,
        int sortOrder,
        int estimatedCalories
) {
}
