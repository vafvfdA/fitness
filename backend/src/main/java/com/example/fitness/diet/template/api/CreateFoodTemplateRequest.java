package com.example.fitness.diet.template.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateFoodTemplateRequest(
        @NotBlank String foodName,
        @NotBlank String defaultUnit,
        @NotNull @Positive BigDecimal caloriesPerUnit,
        @PositiveOrZero BigDecimal proteinPerUnit,
        @PositiveOrZero BigDecimal fatPerUnit,
        @PositiveOrZero BigDecimal carbPerUnit
) {
}
