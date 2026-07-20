package com.example.fitness.workout.template;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WorkoutTemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsWorkoutTemplateWithItems() throws Exception {
        mockMvc.perform(post("/api/v1/workout-templates")
                        .header("X-User-Id", 3001)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "胸日模板",
                                  "bodyPart": "胸",
                                  "description": "卧推为主",
                                  "items": [
                                    {
                                      "exerciseName": "卧推",
                                      "defaultSets": 3,
                                      "defaultReps": 10,
                                      "defaultWeightKg": 60.0,
                                      "estimatedCalories": 180
                                    },
                                    {
                                      "exerciseName": "上斜哑铃推举",
                                      "defaultSets": 2,
                                      "defaultReps": 12,
                                      "defaultWeightKg": 22.5,
                                      "estimatedCalories": 120
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.userId").value(3001))
                .andExpect(jsonPath("$.data.name").value("胸日模板"))
                .andExpect(jsonPath("$.data.bodyPart").value("胸"))
                .andExpect(jsonPath("$.data.system").value(false))
                .andExpect(jsonPath("$.data.items", hasSize(2)))
                .andExpect(jsonPath("$.data.items[0].exerciseName").value("卧推"))
                .andExpect(jsonPath("$.data.items[0].defaultSets").value(3))
                .andExpect(jsonPath("$.data.items[0].defaultWeightKg").value(60.0))
                .andExpect(jsonPath("$.data.items[0].sortOrder").value(1))
                .andExpect(jsonPath("$.data.items[1].exerciseName").value("上斜哑铃推举"));
    }

    @Test
    void returnsTemplatesByBodyPartAndDetailForCurrentUser() throws Exception {
        long chestTemplateId = createTemplate(3002, "胸日模板", "胸", "卧推", 3, 180);
        createTemplate(3002, "背日模板", "背", "划船", 4, 160);

        mockMvc.perform(get("/api/v1/workout-templates")
                        .header("X-User-Id", 3002)
                        .param("bodyPart", "胸"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id").value(chestTemplateId))
                .andExpect(jsonPath("$.data[0].bodyPart").value("胸"));

        mockMvc.perform(get("/api/v1/workout-templates/{id}", chestTemplateId)
                        .header("X-User-Id", 3002))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(chestTemplateId))
                .andExpect(jsonPath("$.data.items", hasSize(1)))
                .andExpect(jsonPath("$.data.items[0].exerciseName").value("卧推"));
    }

    @Test
    void isolatesPrivateTemplatesByUser() throws Exception {
        long otherUserTemplateId = createTemplate(3003, "腿日模板", "腿", "深蹲", 4, 240);

        mockMvc.perform(get("/api/v1/workout-templates")
                        .header("X-User-Id", 3004))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));

        mockMvc.perform(get("/api/v1/workout-templates/{id}", otherUserTemplateId)
                        .header("X-User-Id", 3004))
                .andExpect(status().isNotFound());
    }

    @Test
    void createsWorkoutRecordFromTemplate() throws Exception {
        long templateId = createTemplate(3005, "胸日模板", "胸", "卧推", 3, 180);

        mockMvc.perform(post("/api/v1/workout-templates/{id}/workout-records", templateId)
                        .header("X-User-Id", 3005)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "workoutDate": "2026-07-21",
                                  "note": "从模板生成"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.data.userId").value(3005))
                .andExpect(jsonPath("$.data.workoutDate").value("2026-07-21"))
                .andExpect(jsonPath("$.data.title").value("胸日模板"))
                .andExpect(jsonPath("$.data.bodyPart").value("胸"))
                .andExpect(jsonPath("$.data.totalSets").value(3))
                .andExpect(jsonPath("$.data.estimatedCalories").value(180))
                .andExpect(jsonPath("$.data.exercises", hasSize(1)))
                .andExpect(jsonPath("$.data.exercises[0].sets", hasSize(3)))
                .andExpect(jsonPath("$.data.exercises[0].sets[0].setNo").value(1))
                .andExpect(jsonPath("$.data.exercises[0].sets[0].reps").value(10));

        mockMvc.perform(get("/api/v1/workout-records")
                        .header("X-User-Id", 3005)
                        .param("date", "2026-07-21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].title").value("胸日模板"));
    }

    @Test
    void rejectsCreatingTemplateWithoutItems() throws Exception {
        mockMvc.perform(post("/api/v1/workout-templates")
                        .header("X-User-Id", 3006)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "空模板",
                                  "bodyPart": "胸",
                                  "items": []
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void rejectsCreatingWorkoutFromOtherUsersTemplate() throws Exception {
        long otherUserTemplateId = createTemplate(3007, "背日模板", "背", "划船", 4, 160);

        mockMvc.perform(post("/api/v1/workout-templates/{id}/workout-records", otherUserTemplateId)
                        .header("X-User-Id", 3008)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "workoutDate": "2026-07-22"
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    private long createTemplate(
            long userId,
            String name,
            String bodyPart,
            String exerciseName,
            int defaultSets,
            int calories
    ) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/workout-templates")
                        .header("X-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "%s",
                                  "bodyPart": "%s",
                                  "description": "测试模板",
                                  "items": [
                                    {
                                      "exerciseName": "%s",
                                      "defaultSets": %d,
                                      "defaultReps": 10,
                                      "defaultWeightKg": 60.0,
                                      "estimatedCalories": %d
                                    }
                                  ]
                                }
                                """.formatted(name, bodyPart, exerciseName, defaultSets, calories)))
                .andExpect(status().isOk())
                .andReturn();

        return JsonNumberExtractor.extractLong(result.getResponse().getContentAsString(), "id");
    }

    private static class JsonNumberExtractor {

        private static long extractLong(String json, String fieldName) {
            String marker = "\"" + fieldName + "\":";
            int start = json.indexOf(marker);
            if (start < 0) {
                throw new IllegalStateException("响应中缺少字段: " + fieldName);
            }
            int valueStart = start + marker.length();
            int valueEnd = valueStart;
            while (valueEnd < json.length() && Character.isDigit(json.charAt(valueEnd))) {
                valueEnd++;
            }
            return Long.parseLong(json.substring(valueStart, valueEnd));
        }
    }
}
