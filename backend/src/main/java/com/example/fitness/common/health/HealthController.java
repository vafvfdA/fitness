package com.example.fitness.common.health;

import com.example.fitness.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
class HealthController {

    @GetMapping
    ApiResponse<HealthStatus> health() {
        return ApiResponse.ok(new HealthStatus("UP", "fitness-backend"));
    }
}
