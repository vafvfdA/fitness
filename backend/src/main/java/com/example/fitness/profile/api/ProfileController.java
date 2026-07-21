package com.example.fitness.profile.api;

import com.example.fitness.common.response.ApiResponse;
import com.example.fitness.profile.application.ProfileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
class ProfileController {

    private final ProfileService profileService;

    ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    ApiResponse<ProfileResponse> getProfile(@RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.ok(profileService.getProfile(userId));
    }

    @PutMapping
    ApiResponse<ProfileResponse> updateProfile(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ApiResponse.ok(profileService.updateProfile(userId, request));
    }
}
