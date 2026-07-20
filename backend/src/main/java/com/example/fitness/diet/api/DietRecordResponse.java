package com.example.fitness.diet.api;

import java.time.LocalDate;
import java.util.List;

public record DietRecordResponse(
        Long id,
        Long userId,
        LocalDate dietDate,
        int totalCalories,
        String note,
        List<DietFoodResponse> foods
) {
}
