package com.example.fitness.plan.api;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record TrainingPlanResponse(
        Long id,
        String name,
        String cycleType,
        Integer trainDays,
        Integer restDays,
        LocalDate startDate,
        List<String> muscleRotation,
        Integer dailyCalorieTarget,
        Boolean reminderEnabled,
        LocalTime reminderTime
) {
}
