package com.example.fitness.profile.infrastructure;

import com.example.fitness.profile.api.ProfileResponse;
import com.example.fitness.profile.api.UpdateProfileRequest;
import com.example.fitness.profile.application.WeightGoalCalculator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ProfileRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<ProfileResponse> findByUserId(Long userId) {
        List<ProfileResponse> results = jdbcTemplate.query("""
                select id, user_id, gender, height_cm, birthday, current_weight_kg, target_weight_kg, daily_calorie_target
                from user_profile
                where user_id = ?
                """,
                (rs, rowNum) -> toResponse(
                        rs.getLong("user_id"),
                        rs.getString("gender"),
                        rs.getBigDecimal("height_cm"),
                        rs.getDate("birthday") == null ? null : rs.getDate("birthday").toLocalDate(),
                        rs.getBigDecimal("current_weight_kg"),
                        rs.getBigDecimal("target_weight_kg"),
                        rs.getObject("daily_calorie_target", Integer.class)
                ),
                userId);
        return results.stream().findFirst();
    }

    public BigDecimal findCurrentWeightByUserId(Long userId) {
        List<BigDecimal> results = jdbcTemplate.query("""
                select current_weight_kg from user_profile where user_id = ?
                """,
                (rs, rowNum) -> rs.getBigDecimal("current_weight_kg"),
                userId);
        return results.stream().findFirst().orElse(null);
    }

    public void upsert(Long userId, UpdateProfileRequest request) {
        int updated = jdbcTemplate.update("""
                update user_profile
                set gender = ?, height_cm = ?, birthday = ?, current_weight_kg = ?,
                    target_weight_kg = ?, daily_calorie_target = ?, updated_at = current_timestamp
                where user_id = ?
                """,
                request.gender(),
                request.heightCm(),
                request.birthday() == null ? null : Date.valueOf(request.birthday()),
                request.currentWeightKg(),
                request.targetWeightKg(),
                request.dailyCalorieTarget(),
                userId);
        if (updated == 0) {
            jdbcTemplate.update("""
                    insert into user_profile (user_id, gender, height_cm, birthday, current_weight_kg, target_weight_kg, daily_calorie_target)
                    values (?, ?, ?, ?, ?, ?, ?)
                    """,
                    userId,
                    request.gender(),
                    request.heightCm(),
                    request.birthday() == null ? null : Date.valueOf(request.birthday()),
                    request.currentWeightKg(),
                    request.targetWeightKg(),
                    request.dailyCalorieTarget());
        }
    }

    private ProfileResponse toResponse(Long userId, String gender, BigDecimal heightCm, LocalDate birthday,
                                       BigDecimal currentWeightKg, BigDecimal targetWeightKg, Integer dailyCalorieTarget) {
        return new ProfileResponse(userId, gender, heightCm, birthday, currentWeightKg, targetWeightKg,
                dailyCalorieTarget, WeightGoalCalculator.diff(currentWeightKg, targetWeightKg));
    }
}
