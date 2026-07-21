package com.example.fitness.plan.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record UpdateTrainingPlanRequest(
        @NotBlank String name,
        @NotBlank String cycleType,
        @NotNull @Positive Integer trainDays,
        @NotNull @Positive Integer restDays,
        @NotNull LocalDate startDate,
        List<String> muscleRotation,
        @PositiveOrZero Integer dailyCalorieTarget,
        Boolean reminderEnabled,
        LocalTime reminderTime
) {
}
