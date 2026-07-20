package com.example.fitness.diet.application;

import com.example.fitness.diet.api.CreateDietRecordRequest;
import com.example.fitness.diet.api.DietRecordResponse;
import com.example.fitness.diet.api.DietSummaryResponse;
import com.example.fitness.diet.infrastructure.DietRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DietRecordService {

    private final DietRecordRepository dietRecordRepository;

    public DietRecordService(DietRecordRepository dietRecordRepository) {
        this.dietRecordRepository = dietRecordRepository;
    }

    @Transactional
    public DietRecordResponse create(Long userId, CreateDietRecordRequest request) {
        int totalCalories = request.foods().stream()
                .mapToInt(CreateDietRecordRequest.CreateDietFoodRequest::calories)
                .sum();
        Long recordId = dietRecordRepository.create(userId, request, totalCalories);
        return dietRecordRepository.findById(userId, recordId)
                .orElseThrow(() -> new IllegalStateException("饮食记录创建后未找到"));
    }

    @Transactional(readOnly = true)
    public List<DietRecordResponse> findByDate(Long userId, LocalDate date) {
        return dietRecordRepository.findByDate(userId, date);
    }

    @Transactional(readOnly = true)
    public DietSummaryResponse summary(Long userId, LocalDate date) {
        return dietRecordRepository.summary(userId, date);
    }
}
