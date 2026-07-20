package com.example.fitness.diet.api;

import java.math.BigDecimal;

public record DietFoodResponse(
        Long id,
        String mealType,
        String foodName,
        BigDecimal amount,
        String unit,
        int calories,
        BigDecimal proteinG,
        BigDecimal fatG,
        BigDecimal carbG,
        int sortOrder
) {
}
