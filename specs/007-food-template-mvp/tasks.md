# Tasks — 007 食物模板

## Phase 1 — Setup
- [x] 建 specs/007-food-template-mvp/ 文档套件
- [x] 更新 .specify/feature.json → 007

## Phase 2 — V4 种子迁移
- [x] 写 V4__seed_system_food_templates.sql（~30 系统食物）
- [x] 测试：迁移后 is_system=true count >= 30
- [x] 测试：抽验鸡胸肉 calories_per_unit=133

## Phase 3 — 后端查询
- [x] FoodTemplateResponse（record）
- [x] FoodTemplateRepository.findVisible(userId, keyword)
- [x] FoodTemplateService.findVisible
- [x] FoodTemplateController GET /food-templates
- [x] 测试：系统可见、自建可见、keyword 搜索、用户隔离

## Phase 4 — 后端自建
- [x] CreateFoodTemplateRequest（@Valid）
- [x] FoodTemplateRepository.insert
- [x] FoodTemplateService.create（强制 is_system=false, user_id=当前）
- [x] FoodTemplateController POST /food-templates
- [x] 测试：自建成功、calories_per_unit 正数校验、隔离

## Phase 5 — 前端缩放纯函数
- [x] view-model.js scaleFoodTemplate(template, amount)
- [x] 测试：amount=1/2/0.5、四舍五入

## Phase 6 — 前端饮食页选择器
- [x] 饮食页「从模板选」按钮 + 模板列表面板（可搜索）
- [x] GET /food-templates 加载
- [x] 选中 → scaleFoodTemplate 填表单
- [x] amount 改动 → 重新缩放
- [x] 「新增模板」入口 → POST /food-templates
- [x] 保留手动录入兜底
- [x] node --check 通过

## Phase 7 — 全量测试 + 提交
- [x] mvn test 全绿
- [x] view-model 测试通过
- [x] node --check 通过
- [x] 更新 tasks.md / checklists
- [x] 汇报 + 拟提交信息，待批准 commit + push
