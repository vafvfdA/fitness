package com.example.fitness.profile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void returnsNullForNewUser() throws Exception {
        mockMvc.perform(get("/api/v1/profile").header("X-User-Id", 4001))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void createsProfileOnFirstUpdate() throws Exception {
        mockMvc.perform(put("/api/v1/profile")
                        .header("X-User-Id", 4002)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "gender": "male",
                                  "heightCm": 175.00,
                                  "birthday": "1995-01-01",
                                  "currentWeightKg": 75.00,
                                  "targetWeightKg": 70.00,
                                  "dailyCalorieTarget": 2000
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(4002))
                .andExpect(jsonPath("$.data.currentWeightKg").value(75.00))
                .andExpect(jsonPath("$.data.targetWeightKg").value(70.00))
                .andExpect(jsonPath("$.data.weightDiffKg").value(5.00));

        mockMvc.perform(get("/api/v1/profile").header("X-User-Id", 4002))
                .andExpect(jsonPath("$.data.currentWeightKg").value(75.00))
                .andExpect(jsonPath("$.data.weightDiffKg").value(5.00));
    }

    @Test
    void updatingWeightInsertsBodyMetricHistory() throws Exception {
        updateProfile(4003, 75.00, 70.00);
        updateProfile(4003, 74.50, 70.00);

        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from body_metric_record where user_id = ?",
                Integer.class, 4003);
        assertThat(count).isEqualTo(2);
    }

    @Test
    void doesNotInsertHistoryWhenWeightUnchanged() throws Exception {
        updateProfile(4009, 75.00, 70.00);
        updateProfile(4009, 75.00, 70.00);

        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from body_metric_record where user_id = ?",
                Integer.class, 4009);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void updatingTargetWeightRefreshesDiff() throws Exception {
        updateProfile(4004, 75.00, 70.00);

        mockMvc.perform(put("/api/v1/profile")
                        .header("X-User-Id", 4004)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "gender": "male",
                                  "heightCm": 175.00,
                                  "currentWeightKg": 75.00,
                                  "targetWeightKg": 72.00,
                                  "dailyCalorieTarget": 2000
                                }
                                """))
                .andExpect(jsonPath("$.data.weightDiffKg").value(3.00));
    }

    @Test
    void rejectsNegativeWeight() throws Exception {
        mockMvc.perform(put("/api/v1/profile")
                        .header("X-User-Id", 4005)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "currentWeightKg": -1
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void rejectsWeightOver500() throws Exception {
        mockMvc.perform(put("/api/v1/profile")
                        .header("X-User-Id", 4006)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "currentWeightKg": 501.00
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void isolatesProfileByUser() throws Exception {
        updateProfile(4007, 80.00, 75.00);

        mockMvc.perform(get("/api/v1/profile").header("X-User-Id", 4008))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    private void updateProfile(long userId, double current, double target) throws Exception {
        mockMvc.perform(put("/api/v1/profile")
                        .header("X-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "currentWeightKg": %.2f,
                                  "targetWeightKg": %.2f
                                }
                                """.formatted(current, target)))
                .andExpect(status().isOk());
    }
}
