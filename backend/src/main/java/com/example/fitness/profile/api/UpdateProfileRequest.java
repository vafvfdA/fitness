package com.example.fitness.profile.api;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateProfileRequest(
        String gender,
        @Positive @DecimalMax("300.00") BigDecimal heightCm,
        LocalDate birthday,
        @Positive @DecimalMax("500.00") BigDecimal currentWeightKg,
        @Positive @DecimalMax("500.00") BigDecimal targetWeightKg,
        @PositiveOrZero Integer dailyCalorieTarget
) {
}
