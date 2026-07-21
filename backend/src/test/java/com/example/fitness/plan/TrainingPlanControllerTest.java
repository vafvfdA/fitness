package com.example.fitness.plan;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TrainingPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCurrentReturnsNullForNewUser() throws Exception {
        mockMvc.perform(get("/api/v1/plans/current").header("X-User-Id", 4201))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void updateCurrentCreatesAndReturnsPlan() throws Exception {
        mockMvc.perform(put("/api/v1/plans/current")
                        .header("X-User-Id", 4202)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "练三休一",
                                  "cycleType": "TRAIN_REST",
                                  "trainDays": 3,
                                  "restDays": 1,
                                  "startDate": "2026-07-01",
                                  "muscleRotation": ["胸", "肩", "背"],
                                  "dailyCalorieTarget": 2000,
                                  "reminderEnabled": false
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("练三休一"))
                .andExpect(jsonPath("$.data.trainDays").value(3))
                .andExpect(jsonPath("$.data.restDays").value(1))
                .andExpect(jsonPath("$.data.startDate").value("2026-07-01"))
                .andExpect(jsonPath("$.data.muscleRotation[0]").value("胸"))
                .andExpect(jsonPath("$.data.muscleRotation[1]").value("肩"))
                .andExpect(jsonPath("$.data.muscleRotation[2]").value("背"));
    }

    @Test
    void getTodayReturnsTrainingDayWithBodyPart() throws Exception {
        LocalDate today = LocalDate.now();
        createPlan(4203, today, 3, 1, "[\"胸\", \"肩\", \"背\"]");

        mockMvc.perform(get("/api/v1/plans/current/today").header("X-User-Id", 4203))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.date").value(today.toString()))
                .andExpect(jsonPath("$.data.isTrainingDay").value(true))
                .andExpect(jsonPath("$.data.cycleDayIndex").value(1))
                .andExpect(jsonPath("$.data.todayBodyPart").value("胸"))
                .andExpect(jsonPath("$.data.daysSinceStart").value(0));
    }

    @Test
    void getTodayReturnsRestDay() throws Exception {
        LocalDate today = LocalDate.now();
        createPlan(4204, today.minusDays(3), 3, 1, "[\"胸\", \"肩\", \"背\"]");

        mockMvc.perform(get("/api/v1/plans/current/today").header("X-User-Id", 4204))
                .andExpect(jsonPath("$.data.isTrainingDay").value(false))
                .andExpect(jsonPath("$.data.cycleDayIndex").value(4))
                .andExpect(jsonPath("$.data.todayBodyPart").isEmpty());
    }

    @Test
    void getTodayReturnsNullWhenNoPlan() throws Exception {
        mockMvc.perform(get("/api/v1/plans/current/today").header("X-User-Id", 4205))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void getTodayHandlesDateBeforeStart() throws Exception {
        LocalDate today = LocalDate.now();
        createPlan(4206, today.plusDays(2), 3, 1, "[\"胸\", \"肩\", \"背\"]");

        mockMvc.perform(get("/api/v1/plans/current/today").header("X-User-Id", 4206))
                .andExpect(jsonPath("$.data.isTrainingDay").value(false))
                .andExpect(jsonPath("$.data.cycleDayIndex").value(0));
    }

    @Test
    void isolatesPlanByUser() throws Exception {
        createPlan(4207, LocalDate.now(), 3, 1, "[\"胸\", \"肩\", \"背\"]");

        mockMvc.perform(get("/api/v1/plans/current").header("X-User-Id", 4208))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    private void createPlan(long userId, LocalDate startDate, int trainDays, int restDays, String rotationJson) throws Exception {
        mockMvc.perform(put("/api/v1/plans/current")
                        .header("X-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "测试计划",
                                  "cycleType": "TRAIN_REST",
                                  "trainDays": %d,
                                  "restDays": %d,
                                  "startDate": "%s",
                                  "muscleRotation": %s,
                                  "dailyCalorieTarget": 2000,
                                  "reminderEnabled": false
                                }
                                """.formatted(trainDays, restDays, startDate.toString(), rotationJson)))
                .andExpect(status().isOk());
    }
}
