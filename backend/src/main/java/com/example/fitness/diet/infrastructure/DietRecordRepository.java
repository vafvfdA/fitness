package com.example.fitness.diet.infrastructure;

import com.example.fitness.diet.api.CreateDietRecordRequest;
import com.example.fitness.diet.api.DietFoodResponse;
import com.example.fitness.diet.api.DietRecordResponse;
import com.example.fitness.diet.api.DietSummaryResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class DietRecordRepository {

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private final JdbcTemplate jdbcTemplate;

    public DietRecordRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long create(Long userId, CreateDietRecordRequest request, int totalCalories) {
        Long recordId = insertDietRecord(userId, request, totalCalories);
        for (int index = 0; index < request.foods().size(); index++) {
            insertFood(userId, recordId, request.foods().get(index), index + 1);
        }
        return recordId;
    }

    public Optional<DietRecordResponse> findById(Long userId, Long recordId) {
        List<DietRecordResponse> records = jdbcTemplate.query("""
                        select id, user_id, diet_date, total_calories, note
                        from diet_record
                        where user_id = ? and id = ?
                        """,
                (rs, rowNum) -> toDietRecord(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getDate("diet_date").toLocalDate(),
                        rs.getInt("total_calories"),
                        rs.getString("note")),
                userId,
                recordId);
        return records.stream().findFirst();
    }

    public List<DietRecordResponse> findByDate(Long userId, LocalDate date) {
        return jdbcTemplate.query("""
                        select id, user_id, diet_date, total_calories, note
                        from diet_record
                        where user_id = ? and diet_date = ?
                        order by created_at, id
                        """,
                (rs, rowNum) -> toDietRecord(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getDate("diet_date").toLocalDate(),
                        rs.getInt("total_calories"),
                        rs.getString("note")),
                userId,
                Date.valueOf(date));
    }

    public DietSummaryResponse summary(Long userId, LocalDate date) {
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
                (rs, rowNum) -> new DietSummaryResponse(
                        date,
                        rs.getInt("total_calories"),
                        defaultZero(rs.getBigDecimal("protein_g")),
                        defaultZero(rs.getBigDecimal("fat_g")),
                        defaultZero(rs.getBigDecimal("carb_g")),
                        rs.getInt("food_count"),
                        rs.getInt("record_count")),
                userId,
                Date.valueOf(date));
    }

    private Long insertDietRecord(Long userId, CreateDietRecordRequest request, int totalCalories) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("""
                    insert into diet_record (user_id, diet_date, total_calories, note)
                    values (?, ?, ?, ?)
                    """, new String[]{"id"});
            statement.setLong(1, userId);
            statement.setDate(2, Date.valueOf(request.dietDate()));
            statement.setInt(3, totalCalories);
            statement.setString(4, request.note());
            return statement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    private void insertFood(
            Long userId,
            Long recordId,
            CreateDietRecordRequest.CreateDietFoodRequest food,
            int sortOrder
    ) {
        jdbcTemplate.update("""
                        insert into diet_food_entry
                            (diet_record_id, user_id, meal_type, food_name, amount, unit, calories,
                             protein_g, fat_g, carb_g, sort_order)
                        values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """,
                recordId,
                userId,
                food.mealType(),
                food.foodName(),
                food.amount(),
                food.unit(),
                food.calories(),
                food.proteinG(),
                food.fatG(),
                food.carbG(),
                sortOrder);
    }

    private DietRecordResponse toDietRecord(
            Long id,
            Long userId,
            LocalDate dietDate,
            int totalCalories,
            String note
    ) {
        return new DietRecordResponse(
                id,
                userId,
                dietDate,
                totalCalories,
                note,
                findFoods(id)
        );
    }

    private List<DietFoodResponse> findFoods(Long recordId) {
        return jdbcTemplate.query("""
                        select id, meal_type, food_name, amount, unit, calories, protein_g, fat_g, carb_g, sort_order
                        from diet_food_entry
                        where diet_record_id = ?
                        order by sort_order, id
                        """,
                (rs, rowNum) -> new DietFoodResponse(
                        rs.getLong("id"),
                        rs.getString("meal_type"),
                        rs.getString("food_name"),
                        rs.getBigDecimal("amount"),
                        rs.getString("unit"),
                        rs.getInt("calories"),
                        rs.getBigDecimal("protein_g"),
                        rs.getBigDecimal("fat_g"),
                        rs.getBigDecimal("carb_g"),
                        rs.getInt("sort_order")
                ),
                recordId);
    }

    private static BigDecimal defaultZero(BigDecimal value) {
        return value == null ? ZERO : value;
    }
}
