package com.example.fitness.plan.application;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class TrainingCycleCalculator {

    public record CycleDay(
            boolean isTrainingDay,
            int cycleDayIndex,
            String todayBodyPart,
            long daysSinceStart
    ) {
    }

    public CycleDay compute(LocalDate startDate, int trainDays, int restDays,
                            List<String> muscleRotation, LocalDate today) {
        long daysSinceStart = ChronoUnit.DAYS.between(startDate, today);

        if (daysSinceStart < 0) {
            return new CycleDay(false, 0, "", daysSinceStart);
        }

        int cycleLength = trainDays + restDays;
        if (cycleLength <= 0) {
            return new CycleDay(false, 0, "", daysSinceStart);
        }

        int cycleDayIndex = (int) (daysSinceStart % cycleLength) + 1;
        boolean isTrainingDay = cycleDayIndex <= trainDays;

        String todayBodyPart = "";
        if (isTrainingDay && muscleRotation != null && !muscleRotation.isEmpty()) {
            long elapsedTrainingDays = daysSinceStart - (daysSinceStart / cycleLength) * restDays;
            int rotationIndex = (int) (elapsedTrainingDays % muscleRotation.size());
            todayBodyPart = muscleRotation.get(rotationIndex);
        }

        return new CycleDay(isTrainingDay, cycleDayIndex, todayBodyPart, daysSinceStart);
    }
}
