# Plan — 007 食物模板

## 实现策略

沿用 006 的红绿重构 + 三层分层（api → application → infrastructure）+ JdbcTemplate + H2/MySQL Flyway 测试。

## Phase 划分

### Phase 1 — Setup
- 建 specs/007-food-template-mvp/ 文档套件（本批）
- 更新 .specify/feature.json → 007

### Phase 2 — V4 种子迁移（RED→GREEN）
- 写 `V4__seed_system_food_templates.sql`，预置 ~30 系统食物
- 测试：迁移后 `food_template` where is_system=true count >= 30
- 测试：随机抽验（鸡胸肉 calories_per_unit=133）

### Phase 3 — 后端 FoodTemplate 查询（RED→GREEN）
- FoodTemplateResponse（record）
- FoodTemplateRepository.findVisible(userId, keyword) → SELECT ... WHERE is_system=true OR user_id=? [AND food_name LIKE ?] ORDER BY food_name
- FoodTemplateService.findVisible
- FoodTemplateController GET /food-templates
- 测试：系统模板可见、用户自建可见、keyword 搜索、用户隔离（看不到他人自建）

### Phase 4 — 后端 FoodTemplate 自建（RED→GREEN）
- CreateFoodTemplateRequest（@Valid）
- FoodTemplateRepository.insert(userId, request)
- FoodTemplateService.create（强制 is_system=false, user_id=当前）
- FoodTemplateController POST /food-templates
- 测试：自建成功 is_system=false mine=true、calories_per_unit 必须正、用户隔离

### Phase 5 — 前端缩放纯函数（RED→GREEN）
- view-model.js 新增 scaleFoodTemplate(template, amount)
- 测试：amount=1 原值、amount=2 翻倍、amount=0.5 减半、四舍五入

### Phase 6 — 前端饮食页模板选择器
- 饮食页加「从模板选」按钮 + 模板列表面板（可搜索）
- GET /food-templates 加载列表
- 选中 → scaleFoodTemplate 填表单
- amount 改动 → 重新缩放
- 「新增模板」入口 → POST /food-templates
- 保留手动录入兜底
- node --check 通过

### Phase 7 — 全量测试 + review + 提交
- mvn test（原 55 + 新增 FoodTemplate 测试）
- node view-model.test.js
- node --check 饮食页
- 更新 tasks.md / checklists
- 汇报 + 拟提交信息，待批准 commit + push

## 技术约束
- 后端：JdbcTemplate 直写 SQL（与现有模块一致），不用 MyBatis
- 测试：@SpringBootTest + MockMvc + H2 MySQL 模式 + Flyway
- 用户隔离：所有查询带 user_id 条件，X-User-Id 头
- 迁移：V4 只 INSERT，不改 V1 表结构
