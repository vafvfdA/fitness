package com.example.fitness.bodymetric.infrastructure;

import com.example.fitness.bodymetric.api.BodyMetricResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public class BodyMetricRepository {

    private final JdbcTemplate jdbcTemplate;

    public BodyMetricRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(Long userId, LocalDate recordDate, BigDecimal weightKg) {
        jdbcTemplate.update("""
                insert into body_metric_record (user_id, record_date, weight_kg)
                values (?, ?, ?)
                """,
                userId, Date.valueOf(recordDate), weightKg);
    }

    public List<BodyMetricResponse> findRecent(Long userId, int limit) {
        return jdbcTemplate.query("""
                select id, record_date, weight_kg, body_fat_percent, waist_cm, note
                from body_metric_record
                where user_id = ?
                order by record_date desc, id desc
                limit ?
                """,
                (rs, rowNum) -> new BodyMetricResponse(
                        rs.getLong("id"),
                        rs.getDate("record_date").toLocalDate(),
                        rs.getBigDecimal("weight_kg"),
                        rs.getBigDecimal("body_fat_percent"),
                        rs.getBigDecimal("waist_cm"),
                        rs.getString("note")
                ),
                userId, limit);
    }
}
