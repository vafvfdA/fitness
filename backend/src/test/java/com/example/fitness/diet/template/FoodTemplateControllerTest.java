package com.example.fitness.diet.template;

import com.example.fitness.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FoodTemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getReturnsSystemTemplatesSeededByV4() throws Exception {
        // V4 种子预置了系统食物，任何用户都应看到，且至少包含鸡胸肉
        mockMvc.perform(get("/api/v1/food-templates")
                        .header("X-User-Id", 5001))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.data[*].foodName").value(org.hamcrest.Matchers.hasItem("鸡胸肉")))
                .andExpect(jsonPath("$.data[?(@.foodName == '鸡胸肉')].isSystem").value(org.hamcrest.Matchers.hasItem(true)));
    }

    @Test
    void searchByKeywordFiltersByName() throws Exception {
        // keyword=鸡 应只返回鸡蛋、鸡胸肉，不含米饭
        mockMvc.perform(get("/api/v1/food-templates")
                        .param("keyword", "鸡")
                        .header("X-User-Id", 5001))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].foodName").value(org.hamcrest.Matchers.hasItems("鸡蛋", "鸡胸肉")))
                .andExpect(jsonPath("$.data[*].foodName").value(org.hamcrest.Matchers.not(org.hamcrest.Matchers.hasItem("米饭"))));
    }

    @Test
    void createUserTemplateIsVisibleToSelfAndMarkedMine() throws Exception {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("foodName", "5001自制蛋白棒");
        body.put("defaultUnit", "个");
        body.put("caloriesPerUnit", 220.0);
        body.put("proteinPerUnit", 18.0);
        body.put("fatPerUnit", 8.0);
        body.put("carbPerUnit", 22.0);

        MvcResult result = mockMvc.perform(post("/api/v1/food-templates")
                        .header("X-User-Id", 5001)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isSystem").value(false))
                .andExpect(jsonPath("$.data.mine").value(true))
                .andExpect(jsonPath("$.data.foodName").value("5001自制蛋白棒"))
                .andReturn();

        // 自建后本人查询能看到
        mockMvc.perform(get("/api/v1/food-templates")
                        .param("keyword", "5001自制")
                        .header("X-User-Id", 5001))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].foodName").value(org.hamcrest.Matchers.hasItem("5001自制蛋白棒")))
                .andExpect(jsonPath("$.data[?(@.foodName == '5001自制蛋白棒')].mine").value(org.hamcrest.Matchers.hasItem(true)));
    }

    @Test
    void userIsolationOtherUserCannotSeeSelfCreatedTemplate() throws Exception {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("foodName", "5001专属食物");
        body.put("defaultUnit", "份");
        body.put("caloriesPerUnit", 100.0);
        mockMvc.perform(post("/api/v1/food-templates")
                        .header("X-User-Id", 5001)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        // 5002 搜索 5001 专属食物，应查不到
        mockMvc.perform(get("/api/v1/food-templates")
                        .param("keyword", "5001专属")
                        .header("X-User-Id", 5002))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void createRejectsNonPositiveCalories() throws Exception {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("foodName", "坏数据");
        body.put("defaultUnit", "份");
        body.put("caloriesPerUnit", 0.0);
        mockMvc.perform(post("/api/v1/food-templates")
                        .header("X-User-Id", 5001)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRejectsBlankFoodName() throws Exception {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("foodName", "");
        body.put("defaultUnit", "份");
        body.put("caloriesPerUnit", 100.0);
        mockMvc.perform(post("/api/v1/food-templates")
                        .header("X-User-Id", 5001)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }
}
