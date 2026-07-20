package com.example.fitness.diet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DietRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsDietRecordWithFoodsAndCalculatedTotalCalories() throws Exception {
        mockMvc.perform(post("/api/v1/diet-records")
                        .header("X-User-Id", 2001)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dietDate": "2026-07-20",
                                  "note": "训练日饮食",
                                  "foods": [
                                    {
                                      "mealType": "breakfast",
                                      "foodName": "燕麦",
                                      "amount": 80,
                                      "unit": "g",
                                      "calories": 300,
                                      "proteinG": 10,
                                      "fatG": 6,
                                      "carbG": 50
                                    },
                                    {
                                      "mealType": "lunch",
                                      "foodName": "鸡胸饭",
                                      "amount": 1,
                                      "unit": "份",
                                      "calories": 470,
                                      "proteinG": 30,
                                      "fatG": 14,
                                      "carbG": 50
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.userId").value(2001))
                .andExpect(jsonPath("$.data.dietDate").value("2026-07-20"))
                .andExpect(jsonPath("$.data.totalCalories").value(770))
                .andExpect(jsonPath("$.data.foods", hasSize(2)))
                .andExpect(jsonPath("$.data.foods[0].id").isNumber())
                .andExpect(jsonPath("$.data.foods[0].mealType").value("breakfast"))
                .andExpect(jsonPath("$.data.foods[1].foodName").value("鸡胸饭"));
    }

    @Test
    void returnsDietRecordsByDateForCurrentUser() throws Exception {
        createSimpleDietRecord(2002, "2026-07-21", "breakfast", "鸡蛋", 160);
        createSimpleDietRecord(2003, "2026-07-21", "breakfast", "牛奶", 120);
        createSimpleDietRecord(2002, "2026-07-22", "lunch", "米饭", 230);

        mockMvc.perform(get("/api/v1/diet-records")
                        .header("X-User-Id", 2002)
                        .param("date", "2026-07-21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].userId").value(2002))
                .andExpect(jsonPath("$.data[0].dietDate").value("2026-07-21"))
                .andExpect(jsonPath("$.data[0].totalCalories").value(160))
                .andExpect(jsonPath("$.data[0].foods[0].foodName").value("鸡蛋"));
    }

    @Test
    void returnsDietSummaryByDate() throws Exception {
        createSimpleDietRecord(2004, "2026-08-01", "breakfast", "燕麦", 300);
        createSimpleDietRecord(2004, "2026-08-01", "lunch", "鸡胸饭", 470);
        createSimpleDietRecord(2004, "2026-08-02", "dinner", "牛肉", 500);

        mockMvc.perform(get("/api/v1/diet-records/summary")
                        .header("X-User-Id", 2004)
                        .param("date", "2026-08-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.date").value("2026-08-01"))
                .andExpect(jsonPath("$.data.totalCalories").value(770))
                .andExpect(jsonPath("$.data.proteinG").value(20.0))
                .andExpect(jsonPath("$.data.fatG").value(10.0))
                .andExpect(jsonPath("$.data.carbG").value(60.0))
                .andExpect(jsonPath("$.data.foodCount").value(2))
                .andExpect(jsonPath("$.data.recordCount").value(2));
    }

    @Test
    void returnsZeroSummaryWhenDateHasNoDietRecords() throws Exception {
        mockMvc.perform(get("/api/v1/diet-records/summary")
                        .header("X-User-Id", 2005)
                        .param("date", "2026-08-03"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.date").value("2026-08-03"))
                .andExpect(jsonPath("$.data.totalCalories").value(0))
                .andExpect(jsonPath("$.data.proteinG").value(0))
                .andExpect(jsonPath("$.data.fatG").value(0))
                .andExpect(jsonPath("$.data.carbG").value(0))
                .andExpect(jsonPath("$.data.foodCount").value(0))
                .andExpect(jsonPath("$.data.recordCount").value(0));
    }

    @Test
    void rejectsDietRecordWithoutFoods() throws Exception {
        mockMvc.perform(post("/api/v1/diet-records")
                        .header("X-User-Id", 2006)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dietDate": "2026-07-23",
                                  "foods": []
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    private void createSimpleDietRecord(long userId, String date, String mealType, String foodName, int calories)
            throws Exception {
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
