package com.example.fitness.profile.api;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProfileResponse(
        Long userId,
        String gender,
        BigDecimal heightCm,
        LocalDate birthday,
        BigDecimal currentWeightKg,
        BigDecimal targetWeightKg,
        Integer dailyCalorieTarget,
        BigDecimal weightDiffKg
) {
}
