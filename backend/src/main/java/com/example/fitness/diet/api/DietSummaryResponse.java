package com.example.fitness.diet.api;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DietSummaryResponse(
        LocalDate date,
        int totalCalories,
        BigDecimal proteinG,
        BigDecimal fatG,
        BigDecimal carbG,
        int foodCount,
        int recordCount
) {
}
