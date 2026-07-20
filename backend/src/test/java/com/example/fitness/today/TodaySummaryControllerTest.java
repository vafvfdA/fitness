package com.example.fitness.today;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TodaySummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsWorkoutDietAndNetCaloriesForDate() throws Exception {
        createWorkout(3001, "2026-07-21", "胸", "卧推", 320);
        createDiet(3001, "2026-07-21", "breakfast", "燕麦", 300);
        createDiet(3001, "2026-07-21", "lunch", "鸡胸饭", 470);

        mockMvc.perform(get("/api/v1/today/summary")
                        .header("X-User-Id", 3001)
                        .param("date", "2026-07-21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.data.date").value("2026-07-21"))
                .andExpect(jsonPath("$.data.workout.workoutCount").value(1))
                .andExpect(jsonPath("$.data.workout.bodyParts", containsInAnyOrder("胸")))
                .andExpect(jsonPath("$.data.workout.totalSets").value(2))
                .andExpect(jsonPath("$.data.workout.estimatedCalories").value(320))
                .andExpect(jsonPath("$.data.diet.recordCount").value(2))
                .andExpect(jsonPath("$.data.diet.foodCount").value(2))
                .andExpect(jsonPath("$.data.diet.totalCalories").value(770))
                .andExpect(jsonPath("$.data.diet.proteinG").value(20.0))
                .andExpect(jsonPath("$.data.diet.fatG").value(10.0))
                .andExpect(jsonPath("$.data.diet.carbG").value(60.0))
                .andExpect(jsonPath("$.data.netCalories").value(450));
    }

    @Test
    void returnsZeroSummaryWhenDateHasNoData() throws Exception {
        mockMvc.perform(get("/api/v1/today/summary")
                        .header("X-User-Id", 3002)
                        .param("date", "2026-07-22"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.date").value("2026-07-22"))
                .andExpect(jsonPath("$.data.workout.workoutCount").value(0))
                .andExpect(jsonPath("$.data.workout.bodyParts", empty()))
                .andExpect(jsonPath("$.data.workout.totalSets").value(0))
                .andExpect(jsonPath("$.data.workout.estimatedCalories").value(0))
                .andExpect(jsonPath("$.data.diet.recordCount").value(0))
                .andExpect(jsonPath("$.data.diet.foodCount").value(0))
                .andExpect(jsonPath("$.data.diet.totalCalories").value(0))
                .andExpect(jsonPath("$.data.netCalories").value(0));
    }

    @Test
    void isolatesSummaryByUser() throws Exception {
        createWorkout(3003, "2026-07-23", "背", "划船", 200);
        createDiet(3003, "2026-07-23", "breakfast", "鸡蛋", 160);
        createWorkout(3004, "2026-07-23", "腿", "深蹲", 500);
        createDiet(3004, "2026-07-23", "lunch", "汉堡", 900);

        mockMvc.perform(get("/api/v1/today/summary")
                        .header("X-User-Id", 3003)
                        .param("date", "2026-07-23"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.workout.workoutCount").value(1))
                .andExpect(jsonPath("$.data.workout.bodyParts[0]").value("背"))
                .andExpect(jsonPath("$.data.workout.estimatedCalories").value(200))
                .andExpect(jsonPath("$.data.diet.totalCalories").value(160))
                .andExpect(jsonPath("$.data.netCalories").value(-40));
    }

    private void createWorkout(long userId, String date, String bodyPart, String exerciseName, int calories)
            throws Exception {
        mockMvc.perform(post("/api/v1/workout-records")
                        .header("X-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "workoutDate": "%s",
                                  "title": "%s训练",
                                  "bodyPart": "%s",
                                  "estimatedCalories": %d,
                                  "exercises": [
                                    {
                                      "exerciseName": "%s",
                                      "bodyPart": "%s",
                                      "sets": [
                                        { "setNo": 1, "reps": 10, "weightKg": 20.0 },
                                        { "setNo": 2, "reps": 8, "weightKg": 25.0 }
                                      ]
                                    }
                                  ]
                                }
                                """.formatted(date, bodyPart, bodyPart, calories, exerciseName, bodyPart)))
                .andExpect(status().isOk());
    }

    private void createDiet(long userId, String date, String mealType, String foodName, int calories) throws Exception {
        mockMvc.perform(post("/api/v1/diet-records")
                        .header("X-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dietDate": "%s",
                                  "foods": [
                                    {
                                      "mealType": "%s",
                                      "foodName": "%s",
                                      "amount": 1,
                                      "unit": "份",
                                      "calories": %d,
                                      "proteinG": 10,
                                      "fatG": 5,
                                      "carbG": 30
                                    }
                                  ]
                                }
                                """.formatted(date, mealType, foodName, calories)))
                .andExpect(status().isOk());
    }
}
