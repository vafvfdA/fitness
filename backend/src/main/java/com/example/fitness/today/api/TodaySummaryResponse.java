package com.example.fitness.today.api;

import java.time.LocalDate;

public record TodaySummaryResponse(
        LocalDate date,
        TodayWorkoutSummaryResponse workout,
        TodayDietSummaryResponse diet,
        int netCalories
) {
}
