# Tasks: 训练模板接口

**Input**: Design documents from `specs/003-workout-template-api/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/

**Tests**: 本项目宪章要求测试先行，本任务清单包含测试任务。

## Phase 1: Setup

- [x] T001 确认 `workout_template` 和 `workout_template_item` 表已存在于 Flyway 初始迁移
- [x] T002 确认已有训练记录 Repository 可作为生成记录的数据结构参考

---

## Phase 2: Foundational

- [x] T003 创建训练模板包结构 `backend/src/main/java/com/example/fitness/workout/template/`
- [x] T004 创建 Flyway 迁移 `backend/src/main/resources/db/migration/V2__add_workout_template_item_calories.sql`
- [x] T005 确认 `.specify/feature.json` 指向 `specs/003-workout-template-api`

---

## Phase 3: User Story 1 - 创建训练模板 (Priority: P1) MVP

**Goal**: 用户可以创建包含多个默认动作的训练模板。

**Independent Test**: 创建胸日模板后，响应包含模板基本信息和动作默认参数。

### Tests for User Story 1

- [x] T006 [US1] 编写创建训练模板失败测试 `backend/src/test/java/com/example/fitness/workout/template/WorkoutTemplateControllerTest.java`
- [x] T007 [US1] 运行 `mvn -Dtest=WorkoutTemplateControllerTest test` 并确认因接口不存在返回 404

### Implementation for User Story 1

- [x] T008 [P] [US1] 创建请求 DTO `backend/src/main/java/com/example/fitness/workout/template/api/CreateWorkoutTemplateRequest.java`
- [x] T009 [P] [US1] 创建响应 DTO `backend/src/main/java/com/example/fitness/workout/template/api/WorkoutTemplateResponse.java`
- [x] T010 [US1] 创建 Repository `backend/src/main/java/com/example/fitness/workout/template/infrastructure/WorkoutTemplateRepository.java`
- [x] T011 [US1] 创建 Service `backend/src/main/java/com/example/fitness/workout/template/application/WorkoutTemplateService.java`
- [x] T012 [US1] 创建 Controller `backend/src/main/java/com/example/fitness/workout/template/api/WorkoutTemplateController.java`

---

## Phase 4: User Story 2 - 查询训练模板 (Priority: P2)

**Goal**: 用户可以查询模板列表、按部位筛选，并查看模板详情。

**Independent Test**: 创建多个模板后，列表筛选和详情查询返回当前用户数据。

### Tests for User Story 2

- [x] T013 [US2] 在 `WorkoutTemplateControllerTest` 中增加列表筛选和详情查询测试
- [x] T014 [US2] 在 `WorkoutTemplateControllerTest` 中增加用户隔离测试

### Implementation for User Story 2

- [x] T015 [US2] 实现模板列表查询和部位筛选
- [x] T016 [US2] 实现模板详情查询和用户隔离

---

## Phase 5: User Story 3 - 从模板生成训练记录 (Priority: P3)

**Goal**: 用户可以指定日期，从训练模板生成训练记录。

**Independent Test**: 从模板生成训练记录后，响应结构与训练记录接口一致，并可按日期查询到。

### Tests for User Story 3

- [x] T017 [US3] 在 `WorkoutTemplateControllerTest` 中增加从模板生成训练记录测试
- [x] T018 [US3] 在 `WorkoutTemplateControllerTest` 中增加禁止使用其他用户私有模板测试

### Implementation for User Story 3

- [x] T019 [P] [US3] 创建生成训练记录请求 DTO `backend/src/main/java/com/example/fitness/workout/template/api/CreateWorkoutFromTemplateRequest.java`
- [x] T020 [US3] 实现从模板生成训练记录的数据写入逻辑
- [x] T021 [US3] 确认生成后的训练记录可被既有训练记录查询接口读取

---

## Phase 6: Polish & Cross-Cutting Concerns

- [x] T022 运行 `mvn -Dtest=WorkoutTemplateControllerTest test` 验证模板接口测试
- [x] T023 运行 `mvn test` 验证完整后端测试
- [x] T024 更新 `specs/003-workout-template-api/tasks.md` 任务状态
- [x] T025 记录最终验收结果到回复中

## Dependencies & Execution Order

- Phase 1 和 Phase 2 先完成。
- US1 是 MVP，先完成创建模板。
- US2 在 US1 后完成。
- US3 在 US1 后完成，并依赖已有训练记录表结构。
- 所有实现任务必须在对应测试任务之后执行。

## Parallel Opportunities

- T008 和 T009 可并行。
- T019 可与生成逻辑设计并行，但必须在实现生成接口前完成。
