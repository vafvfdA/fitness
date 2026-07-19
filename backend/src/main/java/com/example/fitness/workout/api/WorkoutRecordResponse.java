package com.example.fitness.workout.api;

import java.time.LocalDate;
import java.util.List;

public record WorkoutRecordResponse(
        Long id,
        Long userId,
        LocalDate workoutDate,
        String title,
        String bodyPart,
        int totalSets,
        int estimatedCalories,
        String note,
        List<WorkoutExerciseResponse> exercises
) {
}
