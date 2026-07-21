package com.example.fitness.plan.application;

import com.example.fitness.plan.api.TodayPlanResponse;
import com.example.fitness.plan.api.TrainingPlanResponse;
import com.example.fitness.plan.api.UpdateTrainingPlanRequest;
import com.example.fitness.plan.infrastructure.TrainingPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final TrainingCycleCalculator trainingCycleCalculator;

    public TrainingPlanService(TrainingPlanRepository trainingPlanRepository,
                               TrainingCycleCalculator trainingCycleCalculator) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.trainingCycleCalculator = trainingCycleCalculator;
    }

    @Transactional(readOnly = true)
    public TrainingPlanResponse getCurrent(Long userId) {
        return trainingPlanRepository.findByUserId(userId).orElse(null);
    }

    @Transactional
    public TrainingPlanResponse updateCurrent(Long userId, UpdateTrainingPlanRequest request) {
        trainingPlanRepository.upsert(userId, request);
        return trainingPlanRepository.findByUserId(userId).orElse(null);
    }

    @Transactional(readOnly = true)
    public TodayPlanResponse getToday(Long userId) {
        TrainingPlanResponse plan = trainingPlanRepository.findByUserId(userId).orElse(null);
        if (plan == null) {
            return null;
        }
        LocalDate today = LocalDate.now();
        TrainingCycleCalculator.CycleDay day = trainingCycleCalculator.compute(
                plan.startDate(), plan.trainDays(), plan.restDays(), plan.muscleRotation(), today);
        return new TodayPlanResponse(today, day.isTrainingDay(), day.cycleDayIndex(),
                day.todayBodyPart(), day.daysSinceStart());
    }
}
