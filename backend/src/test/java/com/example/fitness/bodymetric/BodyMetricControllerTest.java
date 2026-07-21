package com.example.fitness.bodymetric;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BodyMetricControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsEmptyForNewUser() throws Exception {
        mockMvc.perform(get("/api/v1/body-metrics").header("X-User-Id", 4101))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    void returnsHistoryInDescendingOrder() throws Exception {
        updateWeight(4102, 75.00);
        updateWeight(4102, 74.50);
        updateWeight(4102, 74.00);

        mockMvc.perform(get("/api/v1/body-metrics").header("X-User-Id", 4102))
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].weightKg").value(74.00))
                .andExpect(jsonPath("$.data[1].weightKg").value(74.50))
                .andExpect(jsonPath("$.data[2].weightKg").value(75.00));
    }

    @Test
    void respectsLimit() throws Exception {
        updateWeight(4103, 75.00);
        updateWeight(4103, 74.50);
        updateWeight(4103, 74.00);

        mockMvc.perform(get("/api/v1/body-metrics").header("X-User-Id", 4103).param("limit", "1"))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].weightKg").value(74.00));
    }

    @Test
    void isolatesByUser() throws Exception {
        updateWeight(4104, 75.00);

        mockMvc.perform(get("/api/v1/body-metrics").header("X-User-Id", 4105))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    private void updateWeight(long userId, double weight) throws Exception {
        mockMvc.perform(put("/api/v1/profile")
                        .header("X-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "currentWeightKg": %.2f
                                }
                                """.formatted(weight)))
                .andExpect(status().isOk());
    }
}
