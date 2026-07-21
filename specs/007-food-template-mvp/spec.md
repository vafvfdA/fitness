# 007 — 食物模板 MVP

## 概述

为饮食录入提供「食物模板」能力：用户在饮食页可以**从模板列表选择食物**，系统自动带出名称、单位、热量和宏量营养素，并按录入份量自动缩放，免去每次手敲热量。补齐 MVP 闭环最后一块（FR-10，应该）。

## 背景

- 004 已交付饮食页**纯手动录入**（食物名/份量/单位/热量/蛋白/脂肪/碳水全手填）。
- `food_template` 表在 V1 迁移已建好（`is_system` + `user_id` 可空），但**一直是空的、无 Controller**。
- 本变更填充该表（系统种子）并暴露查询/自建接口，前端饮食页接入选择器。

## 范围

### 做
- **后端**
  - `GET /api/v1/food-templates?keyword=`：返回系统模板（`is_system=true`）+ 当前用户自建模板（`user_id=?`），支持 food_name 关键字模糊搜索，按 food_name 排序。
  - `POST /api/v1/food-templates`：当前用户自建模板（`is_system=false`, `user_id=?`）。
  - V4 迁移：预置约 30 个常见系统食物（主食/蛋白/蔬菜/水果/坚果/油脂），带 `calories_per_unit` + 蛋白/脂肪/碳水。
- **前端**
  - `view-model.js` 新增 `scaleFoodTemplate(template, amount)` 纯函数：按份量缩放 calories/蛋白/脂肪/碳水。
  - 饮食页加「从模板选」入口：弹出模板列表（可搜索）→ 选中按份量缩放后自动填充表单 → 用户可改份量后提交。
  - 保留原手动录入作为兜底（模板没覆盖时仍可手填）。
- **测试**（红绿）：模板查询（系统可见、用户自建、搜索、用户隔离）、自建校验、前端缩放映射。

### 不做
- 不改 `diet_food_entry` 结构（选择器只是填表单，记录仍存 food_name/calories 等）。
- 不做模板编辑/删除（自建一次即用；后续变更再补）。
- 不做模板分类/标签/图片。
- 不做后端按份量缩放（缩放在前端纯函数完成，后端只存 per_unit 标准值）。

## 用户故事

- **US-1 查模板**：作为用户，我打开饮食页点「从模板选」，看到系统预置食物和我自己加的食物，能搜关键字，点一个就填好表单。
- **US-2 自建模板**：作为用户，模板里没有我常吃的食物时，我能自己加一条（名称/单位/每单位热量/宏量），下次直接选。
- **US-3 按份量缩放**：作为用户，选了「鸡胸肉 100g 133kcal」后我把份量改成 200，热量自动变 266、蛋白等同步缩放。

## 验收标准

- [ ] `GET /food-templates` 不带 keyword 返回系统模板 + 本人自建；带 keyword 按 food_name 模糊匹配。
- [ ] 用户看不到他人自建模板（用户隔离）。
- [ ] `POST /food-templates` 创建的模板 `is_system=false`、`user_id=当前`；`calories_per_unit` 必须为正。
- [ ] V4 迁移执行后系统食物 ≥ 30 条，`is_system=true`、`user_id=null`。
- [ ] 前端选模板后，表单的 foodName/unit/calories/proteinG/fatG/carbG 按 amount 缩放正确填入。
- [ ] 后端 `mvn test` 全绿（原 55 + 新增 FoodTemplate 测试）。
- [ ] 前端 view-model 测试通过，`node --check` 通过。

## 开放问题

- 无（范围已定：系统预置 + 用户自建 + 选择器）。
