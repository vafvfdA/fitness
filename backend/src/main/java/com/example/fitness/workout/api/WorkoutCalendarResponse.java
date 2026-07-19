package com.example.fitness.workout.api;

import java.util.List;

public record WorkoutCalendarResponse(
        String month,
        List<WorkoutCalendarDayResponse> days
) {
}
