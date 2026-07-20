package com.example.fitness.diet.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateDietRecordRequest(
        @NotNull LocalDate dietDate,
        String note,
        @Valid @NotEmpty List<CreateDietFoodRequest> foods
) {

    public record CreateDietFoodRequest(
            @NotBlank String mealType,
            @NotBlank String foodName,
            @NotNull @Positive BigDecimal amount,
            @NotBlank String unit,
            @NotNull @PositiveOrZero Integer calories,
            @PositiveOrZero BigDecimal proteinG,
            @PositiveOrZero BigDecimal fatG,
            @PositiveOrZero BigDecimal carbG
    ) {
    }
}
