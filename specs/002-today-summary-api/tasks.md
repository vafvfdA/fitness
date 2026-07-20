# Tasks: 今日训练饮食聚合接口

**Input**: Design documents from `specs/002-today-summary-api/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/

**Tests**: 本项目宪章要求测试先行，本任务清单包含测试任务。

## Phase 1: Setup

- [x] T001 确认 `backend/src/main/java/com/example/fitness/workout/` 和 `backend/src/main/java/com/example/fitness/diet/` 已存在可用接口
- [x] T002 确认 `.gitignore` 不会提交 `backend/target` 等构建产物

---

## Phase 2: Foundational

- [x] T003 确认 `workout_record`、`diet_record`、`diet_food_entry` 表可支撑聚合查询
- [x] T004 确认统一响应 `ApiResponse` 和请求头错误处理可复用

---

## Phase 3: User Story 1 - 查看今日训练饮食总览 (Priority: P1) MVP

**Goal**: 用户通过一个接口看到指定日期训练、饮食和净热量。

**Independent Test**: 创建同日训练和饮食数据后，聚合接口返回正确汇总。

### Tests for User Story 1

- [x] T005 [US1] 编写有数据聚合失败测试 `backend/src/test/java/com/example/fitness/today/TodaySummaryControllerTest.java`
- [x] T006 [US1] 运行 `mvn -Dtest=TodaySummaryControllerTest test` 并确认因接口不存在返回 404

### Implementation for User Story 1

- [x] T007 [P] [US1] 创建响应 DTO `backend/src/main/java/com/example/fitness/today/api/TodaySummaryResponse.java`
- [x] T008 [US1] 创建 Controller `backend/src/main/java/com/example/fitness/today/api/TodaySummaryController.java`
- [x] T009 [US1] 创建 Service `backend/src/main/java/com/example/fitness/today/application/TodaySummaryService.java`
- [x] T010 [US1] 创建 Repository `backend/src/main/java/com/example/fitness/today/infrastructure/TodaySummaryRepository.java`

---

## Phase 4: User Story 2 - 没有数据时返回空汇总 (Priority: P2)

- [x] T011 [US2] 在 `TodaySummaryControllerTest` 中增加无数据日期返回 0 值测试
- [x] T012 [US2] 完善 `TodaySummaryRepository` 的空数据兜底

---

## Phase 5: User Story 3 - 用户数据隔离 (Priority: P3)

- [x] T013 [US3] 在 `TodaySummaryControllerTest` 中增加用户隔离测试
- [x] T014 [US3] 确认所有聚合 SQL 都按 `user_id` 和日期过滤

---

## Phase 6: Polish & Cross-Cutting Concerns

- [x] T015 运行 `mvn test` 验证完整后端测试
- [x] T016 更新 `specs/002-today-summary-api/tasks.md` 任务状态
- [x] T017 记录最终验收结果到回复中

## Dependencies & Execution Order

- Phase 1 和 Phase 2 先完成。
- US1 是 MVP，先完成有数据聚合。
- US2 和 US3 在 US1 后完成。
- 所有实现任务必须在对应测试任务之后执行。
