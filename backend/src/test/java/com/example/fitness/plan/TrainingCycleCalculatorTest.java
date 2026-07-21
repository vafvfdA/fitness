package com.example.fitness.plan;

import com.example.fitness.plan.application.TrainingCycleCalculator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TrainingCycleCalculatorTest {

    private final TrainingCycleCalculator calculator = new TrainingCycleCalculator();
    private final LocalDate startDate = LocalDate.of(2026, 7, 1);
    private final List<String> rotation = List.of("胸", "肩", "背");

    @Test
    void computesFirstTrainingDayAsChest() {
        TrainingCycleCalculator.CycleDay day = calculator.compute(startDate, 3, 1, rotation, startDate);

        assertThat(day.isTrainingDay()).isTrue();
        assertThat(day.cycleDayIndex()).isEqualTo(1);
        assertThat(day.todayBodyPart()).isEqualTo("胸");
        assertThat(day.daysSinceStart()).isEqualTo(0);
    }

    @Test
    void computesSecondTrainingDayAsShoulder() {
        TrainingCycleCalculator.CycleDay day = calculator.compute(startDate, 3, 1, rotation, startDate.plusDays(1));

        assertThat(day.isTrainingDay()).isTrue();
        assertThat(day.cycleDayIndex()).isEqualTo(2);
        assertThat(day.todayBodyPart()).isEqualTo("肩");
    }

    @Test
    void computesThirdTrainingDayAsBack() {
        TrainingCycleCalculator.CycleDay day = calculator.compute(startDate, 3, 1, rotation, startDate.plusDays(2));

        assertThat(day.isTrainingDay()).isTrue();
        assertThat(day.cycleDayIndex()).isEqualTo(3);
        assertThat(day.todayBodyPart()).isEqualTo("背");
    }

    @Test
    void computesRestDayAtEndOfCycle() {
        TrainingCycleCalculator.CycleDay day = calculator.compute(startDate, 3, 1, rotation, startDate.plusDays(3));

        assertThat(day.isTrainingDay()).isFalse();
        assertThat(day.cycleDayIndex()).isEqualTo(4);
        assertThat(day.todayBodyPart()).isEmpty();
    }

    @Test
    void wrapsToNextCycleAsChest() {
        TrainingCycleCalculator.CycleDay day = calculator.compute(startDate, 3, 1, rotation, startDate.plusDays(4));

        assertThat(day.isTrainingDay()).isTrue();
        assertThat(day.cycleDayIndex()).isEqualTo(1);
        assertThat(day.todayBodyPart()).isEqualTo("胸");
    }

    @Test
    void handlesDateBeforeStartAsNotStarted() {
        TrainingCycleCalculator.CycleDay day = calculator.compute(startDate, 3, 1, rotation, startDate.minusDays(1));

        assertThat(day.isTrainingDay()).isFalse();
        assertThat(day.cycleDayIndex()).isEqualTo(0);
        assertThat(day.todayBodyPart()).isEmpty();
        assertThat(day.daysSinceStart()).isEqualTo(-1);
    }

    @Test
    void handlesEmptyMuscleRotation() {
        TrainingCycleCalculator.CycleDay day = calculator.compute(startDate, 3, 1, List.of(), startDate);

        assertThat(day.isTrainingDay()).isTrue();
        assertThat(day.todayBodyPart()).isEmpty();
    }

    @Test
    void handlesNullMuscleRotation() {
        TrainingCycleCalculator.CycleDay day = calculator.compute(startDate, 3, 1, null, startDate);

        assertThat(day.isTrainingDay()).isTrue();
        assertThat(day.todayBodyPart()).isEmpty();
    }

    @Test
    void computesAcrossMultipleCycles() {
        TrainingCycleCalculator.CycleDay day = calculator.compute(startDate, 3, 1, rotation, startDate.plusDays(20));

        assertThat(day.isTrainingDay()).isTrue();
        assertThat(day.cycleDayIndex()).isEqualTo(1);
        assertThat(day.todayBodyPart()).isEqualTo("胸");
        assertThat(day.daysSinceStart()).isEqualTo(20);
    }

    @Test
    void supportsDifferentCycleLength() {
        List<String> twoRotation = List.of("胸", "肩");

        assertThat(calculator.compute(startDate, 2, 1, twoRotation, startDate).todayBodyPart()).isEqualTo("胸");
        assertThat(calculator.compute(startDate, 2, 1, twoRotation, startDate.plusDays(1)).todayBodyPart()).isEqualTo("肩");
        assertThat(calculator.compute(startDate, 2, 1, twoRotation, startDate.plusDays(2)).isTrainingDay()).isFalse();
        assertThat(calculator.compute(startDate, 2, 1, twoRotation, startDate.plusDays(3)).todayBodyPart()).isEqualTo("胸");
    }
}
