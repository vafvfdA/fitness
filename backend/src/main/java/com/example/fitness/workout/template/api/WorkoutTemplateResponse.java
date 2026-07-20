package com.example.fitness.workout.template.api;

import java.util.List;

public record WorkoutTemplateResponse(
        Long id,
        Long userId,
        String name,
        String bodyPart,
        String description,
        boolean system,
        List<WorkoutTemplateItemResponse> items
) {
}
