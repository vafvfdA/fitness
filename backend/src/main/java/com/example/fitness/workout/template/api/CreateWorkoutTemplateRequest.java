package com.example.fitness.workout.template.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;

public record CreateWorkoutTemplateRequest(
        @NotBlank String name,
        @NotBlank String bodyPart,
        String description,
        @Valid @NotEmpty List<CreateWorkoutTemplateItemRequest> items
) {

    public record CreateWorkoutTemplateItemRequest(
            @NotBlank String exerciseName,
            @NotNull @Positive Integer defaultSets,
            @Positive Integer defaultReps,
            @PositiveOrZero BigDecimal defaultWeightKg,
            @Positive Integer defaultDurationSeconds,
            @PositiveOrZero Integer estimatedCalories
    ) {
    }
}
