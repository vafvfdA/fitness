package com.example.fitness.workout;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WorkoutRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsWorkoutRecordWithExercisesAndSets() throws Exception {
        mockMvc.perform(post("/api/v1/workout-records")
                        .header("X-User-Id", 1001)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "workoutDate": "2026-07-20",
                                  "title": "胸肩训练",
                                  "bodyPart": "胸",
                                  "estimatedCalories": 320,
                                  "note": "状态不错",
                                  "exercises": [
                                    {
                                      "exerciseName": "卧推",
                                      "bodyPart": "胸",
                                      "estimatedCalories": 180,
                                      "note": "最后一组力竭",
                                      "sets": [
                                        { "setNo": 1, "reps": 10, "weightKg": 60.0 },
                                        { "setNo": 2, "reps": 8, "weightKg": 65.0 }
                                      ]
                                    },
                                    {
                                      "exerciseName": "推肩",
                                      "bodyPart": "肩",
                                      "estimatedCalories": 140,
                                      "sets": [
                                        { "setNo": 1, "reps": 12, "weightKg": 20.0 }
                                      ]
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.userId").value(1001))
                .andExpect(jsonPath("$.data.workoutDate").value("2026-07-20"))
                .andExpect(jsonPath("$.data.bodyPart").value("胸"))
                .andExpect(jsonPath("$.data.totalSets").value(3))
                .andExpect(jsonPath("$.data.estimatedCalories").value(320))
                .andExpect(jsonPath("$.data.exercises", hasSize(2)))
                .andExpect(jsonPath("$.data.exercises[0].id").isNumber())
                .andExpect(jsonPath("$.data.exercises[0].sets", hasSize(2)))
                .andExpect(jsonPath("$.data.exercises[0].sets[0].id").isNumber());
    }

    @Test
    void returnsWorkoutRecordsByDateForCurrentUser() throws Exception {
        createSimpleWorkout(1002, "2026-07-21", "背", "引体向上", 220);
        createSimpleWorkout(1003, "2026-07-21", "腿", "深蹲", 260);
        createSimpleWorkout(1002, "2026-07-22", "胸", "卧推", 200);

        mockMvc.perform(get("/api/v1/workout-records")
                        .header("X-User-Id", 1002)
                        .param("date", "2026-07-21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].userId").value(1002))
                .andExpect(jsonPath("$.data[0].workoutDate").value("2026-07-21"))
                .andExpect(jsonPath("$.data[0].bodyPart").value("背"))
                .andExpect(jsonPath("$.data[0].exercises[0].exerciseName").value("引体向上"));
    }

    @Test
    void returnsCalendarSummaryByMonth() throws Exception {
        createSimpleWorkout(1004, "2026-08-01", "胸", "卧推", 210);
        createSimpleWorkout(1004, "2026-08-01", "肩", "推肩", 160);
        createSimpleWorkout(1004, "2026-08-02", "腿", "深蹲", 260);
        createSimpleWorkout(1004, "2026-09-01", "背", "划船", 190);

        mockMvc.perform(get("/api/v1/workout-records/calendar")
                        .header("X-User-Id", 1004)
                        .param("month", "2026-08"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.month").value("2026-08"))
                .andExpect(jsonPath("$.data.days", hasSize(2)))
                .andExpect(jsonPath("$.data.days[0].date").value("2026-08-01"))
                .andExpect(jsonPath("$.data.days[0].bodyParts", containsInAnyOrder("胸", "肩")))
                .andExpect(jsonPath("$.data.days[0].totalSets").value(2))
                .andExpect(jsonPath("$.data.days[0].estimatedCalories").value(370))
                .andExpect(jsonPath("$.data.days[0].workoutCount").value(2))
                .andExpect(jsonPath("$.data.days[1].date").value("2026-08-02"))
                .andExpect(jsonPath("$.data.days[1].bodyParts[0]").value("腿"));
    }

    @Test
    void rejectsWorkoutRecordWithoutBodyPart() throws Exception {
        mockMvc.perform(post("/api/v1/workout-records")
                        .header("X-User-Id", 1005)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "workoutDate": "2026-07-23",
                                  "estimatedCalories": 100,
                                  "exercises": []
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    private void createSimpleWorkout(long userId, String date, String bodyPart, String exerciseName, int calories)
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
                                        { "setNo": 1, "reps": 10, "weightKg": 20.0 }
                                      ]
                                    }
                                  ]
                                }
                                """.formatted(date, bodyPart, bodyPart, calories, exerciseName, bodyPart)))
                .andExpect(status().isOk());
    }
}
