# Tasks: 饮食记录后端接口

**Input**: Design documents from `specs/001-diet-record-api/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/

**Tests**: 本项目宪章要求测试先行，本任务清单包含测试任务。

**Organization**: 任务按用户故事组织，确保每个故事可以独立实现和验收。

## Format: `[ID] [P?] [Story] Description`

- **[P]**: 可并行执行，影响不同文件且无依赖冲突。
- **[Story]**: 对应用户故事，例如 US1、US2、US3。
- 每个任务必须包含明确文件路径。

## Phase 1: Setup

**Purpose**: 确认现有项目结构和忽略规则可支撑本功能。

- [x] T001 检查 `.gitignore` 是否覆盖 Java、微信小程序和本地工具产物

---

## Phase 2: Foundational

**Purpose**: 确认饮食接口可复用既有数据库和通用响应能力。

- [x] T002 确认 `backend/src/main/resources/db/migration/V1__create_core_tables.sql` 已包含 `diet_record` 和 `diet_food_entry`
- [x] T003 确认 `backend/src/main/java/com/example/fitness/common/response/ApiResponse.java` 可复用
- [x] T004 确认 `backend/src/main/java/com/example/fitness/common/error/GlobalExceptionHandler.java` 可处理校验错误

---

## Phase 3: User Story 1 - 录入当天饮食记录 (Priority: P1) MVP

**Goal**: 用户可以保存某天饮食记录和食物明细。

**Independent Test**: 调用新增接口后，响应包含记录 ID、食物明细 ID、用户 ID、饮食日期和后端计算总热量。

### Tests for User Story 1

- [x] T005 [US1] 编写新增饮食记录失败测试 `backend/src/test/java/com/example/fitness/diet/DietRecordControllerTest.java`
- [x] T006 [US1] 运行 `mvn -Dtest=DietRecordControllerTest test` 并确认测试因接口不存在而失败

### Implementation for User Story 1

- [x] T007 [P] [US1] 创建饮食记录请求 DTO `backend/src/main/java/com/example/fitness/diet/api/CreateDietRecordRequest.java`
- [x] T008 [P] [US1] 创建饮食记录响应 DTO `backend/src/main/java/com/example/fitness/diet/api/DietRecordResponse.java`
- [x] T009 [P] [US1] 创建食物明细响应 DTO `backend/src/main/java/com/example/fitness/diet/api/DietFoodResponse.java`
- [x] T010 [US1] 创建饮食记录 Controller `backend/src/main/java/com/example/fitness/diet/api/DietRecordController.java`
- [x] T011 [US1] 创建饮食记录 Service `backend/src/main/java/com/example/fitness/diet/application/DietRecordService.java`
- [x] T012 [US1] 创建饮食记录 Repository `backend/src/main/java/com/example/fitness/diet/infrastructure/DietRecordRepository.java`

---

## Phase 4: User Story 2 - 查询某天饮食记录 (Priority: P2)

**Goal**: 用户可以查询某天已保存的饮食记录。

**Independent Test**: 不同用户和不同日期的数据存在时，只返回当前用户指定日期的数据。

### Tests for User Story 2

- [x] T013 [US2] 在 `DietRecordControllerTest` 中增加按日期查询过滤测试

### Implementation for User Story 2

- [x] T014 [US2] 在 `DietRecordController` 中实现按日期查询接口
- [x] T015 [US2] 在 `DietRecordService` 和 `DietRecordRepository` 中实现按用户和日期查询

---

## Phase 5: User Story 3 - 查看某天饮食汇总 (Priority: P3)

**Goal**: 用户可以查看某天热量和营养素汇总。

**Independent Test**: 多条食物明细存在时，汇总接口返回总热量、蛋白质、脂肪、碳水和食物数量。

### Tests for User Story 3

- [x] T016 [US3] 在 `DietRecordControllerTest` 中增加按日期汇总测试
- [x] T017 [US3] 在 `DietRecordControllerTest` 中增加无数据日期返回 0 汇总测试
- [x] T018 [US3] 在 `DietRecordControllerTest` 中增加缺少必填字段返回 400 的校验测试

### Implementation for User Story 3

- [x] T019 [P] [US3] 创建饮食汇总响应 DTO `backend/src/main/java/com/example/fitness/diet/api/DietSummaryResponse.java`
- [x] T020 [US3] 在 `DietRecordController` 中实现汇总接口
- [x] T021 [US3] 在 `DietRecordService` 和 `DietRecordRepository` 中实现按日汇总

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: 完成回归验证和文档状态更新。

- [x] T022 运行 `mvn test` 验证完整后端测试
- [x] T023 更新 `specs/001-diet-record-api/tasks.md` 任务状态
- [x] T024 记录最终验收结果到回复中

## Dependencies & Execution Order

- Phase 1 和 Phase 2 先完成。
- US1 是 MVP，必须先完成新增能力。
- US2 依赖可创建测试数据，放在 US1 后。
- US3 依赖已能创建和查询食物明细，放在 US2 后。
- 所有实现任务必须在对应测试任务之后执行。

## Implementation Strategy

1. 先完成 US1，使饮食记录可新增。
2. 再完成 US2，使 B 界面可回显当天记录。
3. 最后完成 US3，为计划页和日历页提供热量汇总基础。
