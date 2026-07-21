# Tasks: 计划与个人资料页面联调

**Input**: Design documents from `specs/006-plan-profile-page-mvp/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/

**Tests**: 本项目宪章要求测试先行，本任务清单包含后端领域计算/API 测试和前端映射测试。

## Phase 1: Setup

- [x] T001 确认 `user_profile`、`body_metric_record`、`training_plan` 三张表已由 V1 迁移创建
- [x] T002 确认小程序 `miniapp/pages/profile` 和 `miniapp/pages/plan` 页面文件已存在
- [x] T003 更新 `.specify/feature.json` 指向 `specs/006-plan-profile-page-mvp`

---

## Phase 2: Foundational - 领域计算（红绿）

- [x] T004 先写 `TrainingCycleCalculator` 失败测试（训练日、休息日、跨多周期、未来日期、空轮换）
- [x] T005 运行测试确认因类不存在失败
- [x] T006 实现 `TrainingCycleCalculator` 纯函数
- [x] T007 先写体重差值计算失败测试
- [x] T008 实现差值计算

---

## Phase 3: User Story 1 - 体重目标 (Priority: P1)

**Goal**: 用户能查看和更新体重目标，更新体重时事务双写历史。

- [x] T009 [US1] 先写 `ProfileController` GET/PUT 失败 API 测试（含体重历史双写断言）
- [x] T010 [US1] 实现 `ProfileRepository`、`ProfileService`、`ProfileController`
- [x] T011 [US1] 确认体重更新事务内插入 `body_metric_record`

---

## Phase 4: User Story 2 - 体重历史 (Priority: P2)

**Goal**: 用户能查看体重历史列表。

- [x] T012 [US2] 先写 `BodyMetricController` GET 失败 API 测试
- [x] T013 [US2] 实现 `BodyMetricRepository`、`BodyMetricService`、`BodyMetricController`

---

## Phase 5: User Story 3 - 训练周期配置 (Priority: P2)

**Goal**: 用户能配置训练周期和部位轮换。

- [x] T014 [US3] 先写 `TrainingPlanController` GET/PUT 失败 API 测试
- [x] T015 [US3] 实现 `TrainingPlanRepository`、`TrainingPlanService`、`TrainingPlanController`

---

## Phase 6: User Story 4 - 今日计划 (Priority: P1)

**Goal**: 用户能看到今日训练/休息状态和部位。

- [x] T016 [US4] 先写 `GET /plans/current/today` 失败 API 测试
- [x] T017 [US4] 实现今日计划接口（复用 `TrainingCycleCalculator`）

---

## Phase 7: 前端联调

- [x] T018 先写 view-model `mapProfile` 和 `computeTodayPlan` 失败测试
- [x] T019 实现 view-model 新增函数
- [x] T020 [US1/2] 联调 profile 页（展示差值、更新体重、历史列表）
- [x] T021 [US3/4] 联调 plan 页（配置周期、展示今日计划）

---

## Phase 8: Polish & Cross-Cutting Concerns

- [x] T022 运行 `node miniapp/tests/view-model.test.js`
- [x] T023 运行 `node --check` profile/plan 页 JS
- [x] T024 运行 `mvn test` 验证后端全量
- [x] T025 更新 `specs/006-plan-profile-page-mvp/tasks.md` 任务状态
- [x] T026 记录微信开发者工具手动验收项

## Dependencies & Execution Order

- Phase 1 和 Phase 2 先完成（领域计算是后续接口基础）。
- Phase 3-6 后端接口按 US 顺序，每个接口先红后绿。
- Phase 7 前端在后端接口可用后联调。
- 所有实现任务必须在对应测试任务之后执行。
