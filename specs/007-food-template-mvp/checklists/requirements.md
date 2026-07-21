# Requirements Checklist — 007 食物模板

## PRD 对齐
- [x] FR-10 食物模板（应该）—— 本变更实现
- [ ] FR-10.1 系统预置食物 —— V4 迁移
- [ ] FR-10.2 用户自建食物模板 —— POST /food-templates
- [ ] FR-10.3 饮食录入从模板选择 —— 前端选择器

## 功能验收
- [ ] GET /food-templates 返回系统模板 + 本人自建
- [ ] GET /food-templates?keyword= 按 food_name 模糊搜索
- [ ] 用户隔离：看不到他人自建
- [ ] POST /food-templates 自建成功，is_system=false，user_id=当前
- [ ] calories_per_unit 必须为正（校验）
- [ ] V4 迁移后系统食物 ≥ 30 条
- [ ] 前端选模板后表单按 amount 缩放正确填入
- [ ] 前端改 amount 后热量/宏量同步缩放
- [ ] 前端可自建模板并立即在选择器可见
- [ ] 手动录入流程保留可用

## 非功能
- [ ] 后端 mvn test 全绿（原 55 + 新增）
- [ ] 前端 view-model 测试通过
- [ ] node --check 饮食页/view-model 通过
- [ ] 用户隔离测试覆盖
- [ ] 无回归（today/workout/calendar/profile/plan 仍正常）

## 架构对齐
- [ ] JdbcTemplate 三层分层
- [ ] X-User-Id 隔离
- [ ] Flyway V4 迁移可重放
- [ ] food_template 表结构不改（用现有）
