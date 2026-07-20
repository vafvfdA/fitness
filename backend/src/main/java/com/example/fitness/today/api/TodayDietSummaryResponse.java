package com.example.fitness.today.api;

import java.math.BigDecimal;

public record TodayDietSummaryResponse(
        int recordCount,
        int foodCount,
        int totalCalories,
        BigDecimal proteinG,
        BigDecimal fatG,
        BigDecimal carbG
) {
}
