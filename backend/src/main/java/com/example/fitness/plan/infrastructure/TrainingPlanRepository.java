package com.example.fitness.plan.infrastructure;

import com.example.fitness.plan.api.TrainingPlanResponse;
import com.example.fitness.plan.api.UpdateTrainingPlanRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainingPlanRepository {

    private static final TypeReference<List<String>> STRING_LIST = new TypeReference<>() {
    };

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TrainingPlanRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<TrainingPlanResponse> findByUserId(Long userId) {
        List<TrainingPlanResponse> results = jdbcTemplate.query("""
                select id, user_id, name, cycle_type, train_days, rest_days, start_date,
                       muscle_rotation_json, daily_calorie_target, reminder_enabled, reminder_time
                from training_plan
                where user_id = ?
                """,
                (rs, rowNum) -> {
                    Time reminderTime = rs.getTime("reminder_time");
                    return new TrainingPlanResponse(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("cycle_type"),
                            rs.getInt("train_days"),
                            rs.getInt("rest_days"),
                            rs.getDate("start_date").toLocalDate(),
                            parseRotation(rs.getString("muscle_rotation_json")),
                            rs.getObject("daily_calorie_target", Integer.class),
                            rs.getBoolean("reminder_enabled"),
                            reminderTime == null ? null : reminderTime.toLocalTime()
                    );
                },
                userId);
        return results.stream().findFirst();
    }

    public void upsert(Long userId, UpdateTrainingPlanRequest request) {
        String rotationJson = writeRotation(request.muscleRotation());
        boolean reminderEnabled = request.reminderEnabled() != null && request.reminderEnabled();
        Time reminderTime = request.reminderTime() == null ? null : Time.valueOf(request.reminderTime());

        int updated = jdbcTemplate.update("""
                update training_plan
                set name = ?, cycle_type = ?, train_days = ?, rest_days = ?, start_date = ?,
                    muscle_rotation_json = ?, daily_calorie_target = ?, reminder_enabled = ?, reminder_time = ?,
                    updated_at = current_timestamp
                where user_id = ?
                """,
                request.name(),
                request.cycleType(),
                request.trainDays(),
                request.restDays(),
                Date.valueOf(request.startDate()),
                rotationJson,
                request.dailyCalorieTarget(),
                reminderEnabled,
                reminderTime,
                userId);
        if (updated == 0) {
            jdbcTemplate.update("""
                    insert into training_plan (user_id, name, cycle_type, train_days, rest_days, start_date,
                        muscle_rotation_json, daily_calorie_target, reminder_enabled, reminder_time)
                    values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    userId,
                    request.name(),
                    request.cycleType(),
                    request.trainDays(),
                    request.restDays(),
                    Date.valueOf(request.startDate()),
                    rotationJson,
                    request.dailyCalorieTarget(),
                    reminderEnabled,
                    reminderTime);
        }
    }

    private List<String> parseRotation(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }
        try {
            return objectMapper.readValue(json, STRING_LIST);
        } catch (JsonProcessingException exception) {
            return List.of();
        }
    }

    private String writeRotation(List<String> rotation) {
        if (rotation == null || rotation.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(rotation);
        } catch (JsonProcessingException exception) {
            return null;
        }
    }
}
