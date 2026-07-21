package com.example.fitness.profile.application;

import java.math.BigDecimal;

public final class WeightGoalCalculator {

    private WeightGoalCalculator() {
    }

    public static BigDecimal diff(BigDecimal currentWeightKg, BigDecimal targetWeightKg) {
        if (currentWeightKg == null || targetWeightKg == null) {
            return null;
        }
        return currentWeightKg.subtract(targetWeightKg);
    }
}
