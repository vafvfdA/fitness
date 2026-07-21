package com.example.fitness.profile.application;

import com.example.fitness.bodymetric.infrastructure.BodyMetricRepository;
import com.example.fitness.profile.api.ProfileResponse;
import com.example.fitness.profile.api.UpdateProfileRequest;
import com.example.fitness.profile.infrastructure.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final BodyMetricRepository bodyMetricRepository;

    public ProfileService(ProfileRepository profileRepository, BodyMetricRepository bodyMetricRepository) {
        this.profileRepository = profileRepository;
        this.bodyMetricRepository = bodyMetricRepository;
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(Long userId) {
        return profileRepository.findByUserId(userId).orElse(null);
    }

    @Transactional
    public ProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        BigDecimal previousWeight = profileRepository.findCurrentWeightByUserId(userId);
        profileRepository.upsert(userId, request);
        BigDecimal newWeight = request.currentWeightKg();
        if (newWeight != null && (previousWeight == null || newWeight.compareTo(previousWeight) != 0)) {
            bodyMetricRepository.insert(userId, LocalDate.now(), newWeight);
        }
        return profileRepository.findByUserId(userId).orElse(null);
    }
}
