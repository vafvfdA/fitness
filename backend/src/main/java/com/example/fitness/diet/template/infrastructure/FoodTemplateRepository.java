package com.example.fitness.diet.template.infrastructure;

import com.example.fitness.diet.template.api.CreateFoodTemplateRequest;
import com.example.fitness.diet.template.api.FoodTemplateResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FoodTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    public FoodTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /** 可见模板：系统模板(is_system=true) 或 本人自建(user_id=userId)，可选按 food_name 模糊搜索。 */
    public List<FoodTemplateResponse> findVisible(Long userId, String keyword) {
        String kw = keyword == null ? "" : keyword.trim();
        if (kw.isEmpty()) {
            return jdbcTemplate.query("""
                    SELECT id, food_name, default_unit, calories_per_unit, protein_per_unit, fat_per_unit, carb_per_unit, is_system
                    FROM food_template
                    WHERE is_system = true OR user_id = ?
                    ORDER BY is_system DESC, food_name ASC
                    """,
                    (rs, n) -> map(rs),
                    userId);
        }
        String like = "%" + kw.toLowerCase() + "%";
        return jdbcTemplate.query("""
                SELECT id, food_name, default_unit, calories_per_unit, protein_per_unit, fat_per_unit, carb_per_unit, is_system
                FROM food_template
                WHERE (is_system = true OR user_id = ?) AND LOWER(food_name) LIKE ?
                ORDER BY is_system DESC, food_name ASC
                """,
                (rs, n) -> map(rs),
                userId, like);
    }

    /** 用户自建模板：强制 is_system=false, user_id=当前。 */
    public FoodTemplateResponse insert(Long userId, CreateFoodTemplateRequest r) {
        jdbcTemplate.update("""
                INSERT INTO food_template (user_id, food_name, default_unit, calories_per_unit, protein_per_unit, fat_per_unit, carb_per_unit, is_system)
                VALUES (?, ?, ?, ?, ?, ?, ?, false)
                """,
                userId, r.foodName(), r.defaultUnit(), r.caloriesPerUnit(),
                r.proteinPerUnit(), r.fatPerUnit(), r.carbPerUnit());
        return jdbcTemplate.queryForObject("""
                SELECT id, food_name, default_unit, calories_per_unit, protein_per_unit, fat_per_unit, carb_per_unit, is_system
                FROM food_template
                WHERE user_id = ? AND food_name = ?
                ORDER BY id DESC
                LIMIT 1
                """,
                (rs, n) -> map(rs),
                userId, r.foodName());
    }

    private FoodTemplateResponse map(ResultSet rs) throws SQLException {
        boolean system = rs.getBoolean("is_system");
        return new FoodTemplateResponse(
                rs.getLong("id"),
                rs.getString("food_name"),
                rs.getString("default_unit"),
                rs.getBigDecimal("calories_per_unit"),
                rs.getBigDecimal("protein_per_unit"),
                rs.getBigDecimal("fat_per_unit"),
                rs.getBigDecimal("carb_per_unit"),
                system,
                !system
        );
    }
}
