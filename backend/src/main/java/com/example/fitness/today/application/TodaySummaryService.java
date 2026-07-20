package com.example.fitness.today.application;

import com.example.fitness.today.api.TodayDietSummaryResponse;
import com.example.fitness.today.api.TodaySummaryResponse;
import com.example.fitness.today.api.TodayWorkoutSummaryResponse;
import com.example.fitness.today.infrastructure.TodaySummaryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class TodaySummaryService {

    private final TodaySummaryRepository todaySummaryRepository;

    public TodaySummaryService(TodaySummaryRepository todaySummaryRepository) {
        this.todaySummaryRepository = todaySummaryRepository;
    }

    @Transactional(readOnly = true)
    public TodaySummaryResponse summary(Long userId, LocalDate date) {
        TodayWorkoutSummaryResponse workout = todaySummaryRepository.workoutSummary(userId, date);
        TodayDietSummaryResponse diet = todaySummaryRepository.dietSummary(userId, date);
        return new TodaySummaryResponse(
                date,
                workout,
                diet,
                diet.totalCalories() - workout.estimatedCalories()
        );
    }
}
