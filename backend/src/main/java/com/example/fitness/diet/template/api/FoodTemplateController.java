package com.example.fitness.diet.template.api;

import com.example.fitness.common.response.ApiResponse;
import com.example.fitness.diet.template.application.FoodTemplateService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/food-templates")
class FoodTemplateController {

    private final FoodTemplateService foodTemplateService;

    FoodTemplateController(FoodTemplateService foodTemplateService) {
        this.foodTemplateService = foodTemplateService;
    }

    @GetMapping
    ApiResponse<List<FoodTemplateResponse>> findVisible(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) String keyword
    ) {
        return ApiResponse.ok(foodTemplateService.findVisible(userId, keyword));
    }

    @PostMapping
    ApiResponse<FoodTemplateResponse> create(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateFoodTemplateRequest request
    ) {
        return ApiResponse.ok(foodTemplateService.create(userId, request));
    }
}
