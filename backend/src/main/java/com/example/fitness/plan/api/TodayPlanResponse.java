package com.example.fitness.plan.api;

import java.time.LocalDate;

public record TodayPlanResponse(
        LocalDate date,
        boolean isTrainingDay,
        int cycleDayIndex,
        String todayBodyPart,
        long daysSinceStart
) {
}
