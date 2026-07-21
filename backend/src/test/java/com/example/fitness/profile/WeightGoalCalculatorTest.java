package com.example.fitness.profile;

import com.example.fitness.profile.application.WeightGoalCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class WeightGoalCalculatorTest {

    @Test
    void computesPositiveDiffWhenCurrentAboveTarget() {
        assertThat(WeightGoalCalculator.diff(new BigDecimal("75.00"), new BigDecimal("70.00")))
                .isEqualByComparingTo("5.00");
    }

    @Test
    void computesNegativeDiffWhenCurrentBelowTarget() {
        assertThat(WeightGoalCalculator.diff(new BigDecimal("68.00"), new BigDecimal("70.00")))
                .isEqualByComparingTo("-2.00");
    }

    @Test
    void returnsZeroWhenCurrentEqualsTarget() {
        assertThat(WeightGoalCalculator.diff(new BigDecimal("70.00"), new BigDecimal("70.00")))
                .isEqualByComparingTo("0.00");
    }

    @Test
    void returnsNullWhenCurrentIsNull() {
        assertThat(WeightGoalCalculator.diff(null, new BigDecimal("70.00"))).isNull();
    }

    @Test
    void returnsNullWhenTargetIsNull() {
        assertThat(WeightGoalCalculator.diff(new BigDecimal("75.00"), null)).isNull();
    }
}
