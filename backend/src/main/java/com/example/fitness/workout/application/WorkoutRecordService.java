package com.example.fitness.workout.application;

import com.example.fitness.workout.api.CreateWorkoutRecordRequest;
import com.example.fitness.workout.api.WorkoutCalendarDayResponse;
import com.example.fitness.workout.api.WorkoutCalendarResponse;
import com.example.fitness.workout.api.WorkoutRecordResponse;
import com.example.fitness.workout.infrastructure.WorkoutRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

@Service
public class WorkoutRecordService {

    private final WorkoutRecordRepository workoutRecordRepository;

    public WorkoutRecordService(WorkoutRecordRepository workoutRecordRepository) {
        this.workoutRecordRepository = workoutRecordRepository;
    }

    @Transactional
    public WorkoutRecordResponse create(Long userId, CreateWorkoutRecordRequest request) {
        int totalSets = request.exercises().stream()
                .mapToInt(exercise -> exercise.sets().size())
                .sum();
        Long recordId = workoutRecordRepository.create(userId, request, totalSets);
        return workoutRecordRepository.findById(userId, recordId)
                .orElseThrow(() -> new IllegalStateException("训练记录创建后未找到"));
    }

    @Transactional(readOnly = true)
    public List<WorkoutRecordResponse> findByDate(Long userId, LocalDate date) {
        return workoutRecordRepository.findByDate(userId, date);
    }

    @Transactional(readOnly = true)
    public WorkoutCalendarResponse calendar(Long userId, YearMonth month) {
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.plusMonths(1).atDay(1);
        List<WorkoutRecordResponse> records = workoutRecordRepository.findBetween(userId, startDate, endDate);

        Map<LocalDate, DayAccumulator> days = new LinkedHashMap<>();
        for (WorkoutRecordResponse record : records) {
            DayAccumulator accumulator = days.computeIfAbsent(record.workoutDate(), ignored -> new DayAccumulator());
            accumulator.bodyParts.add(record.bodyPart());
            accumulator.totalSets += record.totalSets();
            accumulator.estimatedCalories += record.estimatedCalories();
            accumulator.workoutCount++;
        }

        List<WorkoutCalendarDayResponse> summaries = new ArrayList<>();
        for (Map.Entry<LocalDate, DayAccumulator> entry : days.entrySet()) {
            DayAccumulator accumulator = entry.getValue();
            summaries.add(new WorkoutCalendarDayResponse(
                    entry.getKey(),
                    List.copyOf(accumulator.bodyParts),
                    accumulator.totalSets,
                    accumulator.estimatedCalories,
                    accumulator.workoutCount
            ));
        }

        return new WorkoutCalendarResponse(month.toString(), summaries);
    }

    private static class DayAccumulator {
        private final LinkedHashSet<String> bodyParts = new LinkedHashSet<>();
        private int totalSets;
        private int estimatedCalories;
        private int workoutCount;
    }
}
