package com.example.fitness.diet.template.application;

import com.example.fitness.diet.template.api.CreateFoodTemplateRequest;
import com.example.fitness.diet.template.api.FoodTemplateResponse;
import com.example.fitness.diet.template.infrastructure.FoodTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FoodTemplateService {

    private final FoodTemplateRepository foodTemplateRepository;

    public FoodTemplateService(FoodTemplateRepository foodTemplateRepository) {
        this.foodTemplateRepository = foodTemplateRepository;
    }

    @Transactional(readOnly = true)
    public List<FoodTemplateResponse> findVisible(Long userId, String keyword) {
        return foodTemplateRepository.findVisible(userId, keyword);
    }

    @Transactional
    public FoodTemplateResponse create(Long userId, CreateFoodTemplateRequest request) {
        return foodTemplateRepository.insert(userId, request);
    }
}
