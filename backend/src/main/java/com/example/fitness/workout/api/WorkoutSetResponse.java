package com.example.fitness.workout.api;

import java.math.BigDecimal;

public record WorkoutSetResponse(
        Long id,
        int setNo,
        Integer reps,
        BigDecimal weightKg,
        Integer durationSeconds,
        Integer distanceMeters
) {
}
