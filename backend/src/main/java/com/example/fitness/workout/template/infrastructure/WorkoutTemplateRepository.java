package com.example.fitness.workout.template.infrastructure;

import com.example.fitness.workout.template.api.CreateWorkoutTemplateRequest;
import com.example.fitness.workout.template.api.WorkoutTemplateItemResponse;
import com.example.fitness.workout.template.api.WorkoutTemplateResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class WorkoutTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    public WorkoutTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long create(Long userId, CreateWorkoutTemplateRequest request) {
        Long templateId = insertTemplate(userId, request);
        for (int index = 0; index < request.items().size(); index++) {
            insertItem(templateId, request.items().get(index), index + 1);
        }
        return templateId;
    }

    public List<WorkoutTemplateResponse> findAll(Long userId, String bodyPart) {
        if (StringUtils.hasText(bodyPart)) {
            return jdbcTemplate.query("""
                            select id, user_id, name, body_part, description, is_system
                            from workout_template
                            where user_id = ? and body_part = ?
                            order by created_at, id
                            """,
                    (rs, rowNum) -> toTemplate(
                            rs.getLong("id"),
                            rs.getLong("user_id"),
                            rs.getString("name"),
                            rs.getString("body_part"),
                            rs.getString("description"),
                            rs.getBoolean("is_system")
                    ),
                    userId,
                    bodyPart);
        }

        return jdbcTemplate.query("""
                        select id, user_id, name, body_part, description, is_system
                        from workout_template
                        where user_id = ?
                        order by created_at, id
                        """,
                (rs, rowNum) -> toTemplate(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("name"),
                        rs.getString("body_part"),
                        rs.getString("description"),
                        rs.getBoolean("is_system")
                ),
                userId);
    }

    public Optional<WorkoutTemplateResponse> findById(Long userId, Long templateId) {
        List<WorkoutTemplateResponse> templates = jdbcTemplate.query("""
                        select id, user_id, name, body_part, description, is_system
                        from workout_template
                        where user_id = ? and id = ?
                        """,
                (rs, rowNum) -> toTemplate(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("name"),
                        rs.getString("body_part"),
                        rs.getString("description"),
                        rs.getBoolean("is_system")
                ),
                userId,
                templateId);
        return templates.stream().findFirst();
    }

    private Long insertTemplate(Long userId, CreateWorkoutTemplateRequest request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("""
                    insert into workout_template
                        (user_id, name, body_part, description, is_system)
                    values (?, ?, ?, ?, false)
                    """, new String[]{"id"});
            statement.setLong(1, userId);
            statement.setString(2, request.name());
            statement.setString(3, request.bodyPart());
            statement.setString(4, request.description());
            return statement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    private void insertItem(
            Long templateId,
            CreateWorkoutTemplateRequest.CreateWorkoutTemplateItemRequest item,
            int sortOrder
    ) {
        jdbcTemplate.update("""
                        insert into workout_template_item
                            (template_id, exercise_name, default_sets, default_reps, default_weight_kg,
                             default_duration_seconds, sort_order, estimated_calories)
                        values (?, ?, ?, ?, ?, ?, ?, ?)
                        """,
                templateId,
                item.exerciseName(),
                item.defaultSets(),
                item.defaultReps(),
                item.defaultWeightKg(),
                item.defaultDurationSeconds(),
                sortOrder,
                zeroIfNull(item.estimatedCalories()));
    }

    private WorkoutTemplateResponse toTemplate(
            Long id,
            Long userId,
            String name,
            String bodyPart,
            String description,
            boolean system
    ) {
        return new WorkoutTemplateResponse(
                id,
                userId,
                name,
                bodyPart,
                description,
                system,
                findItems(id)
        );
    }

    private List<WorkoutTemplateItemResponse> findItems(Long templateId) {
        return jdbcTemplate.query("""
                        select id, exercise_name, default_sets, default_reps, default_weight_kg,
                               default_duration_seconds, sort_order, estimated_calories
                        from workout_template_item
                        where template_id = ?
                        order by sort_order, id
                        """,
                (rs, rowNum) -> new WorkoutTemplateItemResponse(
                        rs.getLong("id"),
                        rs.getString("exercise_name"),
                        rs.getInt("default_sets"),
                        rs.getObject("default_reps", Integer.class),
                        rs.getBigDecimal("default_weight_kg"),
                        rs.getObject("default_duration_seconds", Integer.class),
                        rs.getInt("sort_order"),
                        rs.getInt("estimated_calories")
                ),
                templateId);
    }

    private int zeroIfNull(Integer value) {
        return value == null ? 0 : value;
    }
}
