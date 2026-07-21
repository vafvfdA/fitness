package com.example.fitness.bodymetric.api;

import com.example.fitness.bodymetric.application.BodyMetricService;
import com.example.fitness.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/body-metrics")
class BodyMetricController {

    private final BodyMetricService bodyMetricService;

    BodyMetricController(BodyMetricService bodyMetricService) {
        this.bodyMetricService = bodyMetricService;
    }

    @GetMapping
    ApiResponse<List<BodyMetricResponse>> findRecent(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(defaultValue = "30") int limit
    ) {
        return ApiResponse.ok(bodyMetricService.findRecent(userId, limit));
    }
}
