package com.example.fitness.workout.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateWorkoutRecordRequest(
        @NotNull LocalDate workoutDate,
        String title,
        @NotBlank String bodyPart,
        @PositiveOrZero Integer estimatedCalories,
        String note,
        @Valid @NotEmpty List<CreateWorkoutExerciseRequest> exercises
) {

    public record CreateWorkoutExerciseRequest(
            @NotBlank String exerciseName,
            String bodyPart,
            @PositiveOrZero Integer estimatedCalories,
            String note,
            @Valid @NotEmpty List<CreateWorkoutSetRequest> sets
    ) {
    }

    public record CreateWorkoutSetRequest(
            @NotNull @Positive Integer setNo,
            @Positive Integer reps,
            @PositiveOrZero BigDecimal weightKg,
            @Positive Integer durationSeconds,
            @Positive Integer distanceMeters
    ) {
    }
}
