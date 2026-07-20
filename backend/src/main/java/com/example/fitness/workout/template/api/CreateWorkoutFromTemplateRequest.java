package com.example.fitness.workout.template.api;

import java.time.LocalDate;

public record CreateWorkoutFromTemplateRequest(
        LocalDate workoutDate,
        String note
) {
}
