package com.example.fitness.bodymetric.application;

import com.example.fitness.bodymetric.api.BodyMetricResponse;
import com.example.fitness.bodymetric.infrastructure.BodyMetricRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BodyMetricService {

    private final BodyMetricRepository bodyMetricRepository;

    public BodyMetricService(BodyMetricRepository bodyMetricRepository) {
        this.bodyMetricRepository = bodyMetricRepository;
    }

    @Transactional(readOnly = true)
    public List<BodyMetricResponse> findRecent(Long userId, int limit) {
        int safeLimit = Math.min(Math.max(limit, 1), 90);
        return bodyMetricRepository.findRecent(userId, safeLimit);
    }
}
