package com.example.fitness.diet.template.api;

import java.math.BigDecimal;

public record FoodTemplateResponse(
        Long id,
        String foodName,
        String defaultUnit,
        BigDecimal caloriesPerUnit,
        BigDecimal proteinPerUnit,
        BigDecimal fatPerUnit,
        BigDecimal carbPerUnit,
        boolean isSystem,
        boolean mine
) {
}
