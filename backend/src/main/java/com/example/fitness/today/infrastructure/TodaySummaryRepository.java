package com.example.fitness.today.infrastructure;

import com.example.fitness.today.api.TodayDietSummaryResponse;
import com.example.fitness.today.api.TodayWorkoutSummaryResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Repository
public class TodaySummaryRepository {

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private final JdbcTemplate jdbcTemplate;

    public TodaySummaryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TodayWorkoutSummaryResponse workoutSummary(Long userId, LocalDate date) {
        List<WorkoutSummaryRow> rows = jdbcTemplate.query("""
                        select body_part, total_sets, estimated_calories
                        from workout_record
                        where user_id = ? and workout_date = ?
                        order by created_at, id
                        """,
                (rs, rowNum) -> new WorkoutSummaryRow(
                        rs.getString("body_part"),
                        rs.getInt("total_sets"),
                        rs.getInt("estimated_calories")),
                userId,
                Date.valueOf(date));

        LinkedHashSet<String> bodyParts = new LinkedHashSet<>();
        int totalSets = 0;
        int estimatedCalories = 0;
        for (WorkoutSummaryRow row : rows) {
            bodyParts.add(row.bodyPart());
            totalSets += row.totalSets();
            estimatedCalories += row.estimatedCalories();
        }

        return new TodayWorkoutSummaryResponse(
                rows.size(),
                new ArrayList<>(bodyParts),
                totalSets,
                estimatedCalories
        );
    }

    public TodayDietSummaryResponse dietSummary(Long userId, LocalDate date) {
        return jdbcTemplate.queryForObject("""
                        select
                            coalesce(sum(f.calories), 0) as total_calories,
                            coalesce(sum(f.protein_g), 0) as protein_g,
                            coalesce(sum(f.fat_g), 0) as fat_g,
                            coalesce(sum(f.carb_g), 0) as carb_g,
                            count(f.id) as food_count,
                            count(distinct d.id) as record_count
                        from diet_record d
                        left join diet_food_entry f on f.diet_record_id = d.id and f.user_id = d.user_id
                        where d.user_id = ? and d.diet_date = ?
                        """,
                (rs, rowNum) -> new TodayDietSummaryResponse(
                        rs.getInt("record_count"),
                        rs.getInt("food_count"),
                        rs.getInt("total_calories"),
                        defaultZero(rs.getBigDecimal("protein_g")),
                        defaultZero(rs.getBigDecimal("fat_g")),
                        defaultZero(rs.getBigDecimal("carb_g"))),
                userId,
                Date.valueOf(date));
    }

    private static BigDecimal defaultZero(BigDecimal value) {
        return value == null ? ZERO : value;
    }

    private record WorkoutSummaryRow(String bodyPart, int totalSets, int estimatedCalories) {
    }
}
