package com.example.fitness.workout.api;

import java.util.List;

public record WorkoutExerciseResponse(
        Long id,
        String exerciseName,
        String bodyPart,
        int sortOrder,
        int estimatedCalories,
        String note,
        List<WorkoutSetResponse> sets
) {
}
