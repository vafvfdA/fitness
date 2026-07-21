package com.example.fitness.bodymetric.api;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BodyMetricResponse(
        Long id,
        LocalDate recordDate,
        BigDecimal weightKg,
        BigDecimal bodyFatPercent,
        BigDecimal waistCm,
        String note
) {
}
