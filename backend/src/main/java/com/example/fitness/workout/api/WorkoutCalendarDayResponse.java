package com.example.fitness.workout.api;

import java.time.LocalDate;
import java.util.List;

public record WorkoutCalendarDayResponse(
        LocalDate date,
        List<String> bodyParts,
        int totalSets,
        int estimatedCalories,
        int workoutCount
) {
}
