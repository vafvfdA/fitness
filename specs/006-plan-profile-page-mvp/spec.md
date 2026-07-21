# Feature Specification: 计划与个人资料页面联调

**Feature Branch**: `006-plan-profile-page-mvp`

**Created**: 2026-07-21

**Status**: Draft

**Input**: 用户确认 005 已验收并推送，下一步开发计划与个人资料页面联调。体重方案选 B（同时记历史），FR-14 训练周期不拆出，006 一次性完成。

## User Scenarios & Testing

### User Story 1 - 查看并设置体重目标 (Priority: P1)

用户在 profile 页可以看到当前体重、目标体重和差值，并能更新当前体重和目标体重。

**Why this priority**: 体重目标是 MVP 必须 项 FR-12/13，是计划页面的核心数据。

**Independent Test**: 更新当前体重后，profile 快照同步更新，body_metric_record 多一条历史记录，差值刷新。

**Acceptance Scenarios**:

1. **Given** 用户已有 profile，**When** 打开 profile 页，**Then** 展示当前体重、目标体重、差值（当前 - 目标）。
2. **Given** 用户更新当前体重为 74.5，**When** 保存，**Then** profile.current_weight_kg 变为 74.5，body_metric_record 插入一条今日记录，差值刷新。
3. **Given** 用户更新目标体重，**When** 保存，**Then** 差值同步刷新。

---

### User Story 2 - 查看体重历史 (Priority: P2)

用户可以查看最近的体重历史记录列表。

**Why this priority**: 方案 B 的核心价值是保留历史，但曲线图是增强功能，MVP 先做列表。

**Independent Test**: 多次更新体重后，历史接口按日期倒序返回记录。

**Acceptance Scenarios**:

1. **Given** 用户更新过多次体重，**When** 打开 profile 页，**Then** 展示最近 N 条体重历史，按日期倒序。
2. **Given** 用户从未更新过体重，**When** 打开 profile 页，**Then** 历史区域展示空状态。

---

### User Story 3 - 配置训练周期 (Priority: P2)

用户在 plan 页可以配置训练/休息天数和部位轮换。

**Why this priority**: 对应 FR-14（应该），让今日计划可计算。

**Independent Test**: 保存训练周期后，GET /plans/current 返回保存的配置。

**Acceptance Scenarios**:

1. **Given** 用户设置 train_days=3、rest_days=1、muscle_rotation=[胸,肩,背]、start_date=2026-07-01，**When** 保存，**Then** GET /plans/current 返回该配置。
2. **Given** 用户首次打开 plan 页无计划，**When** 页面加载，**Then** 展示空状态并引导设置。

---

### User Story 4 - 查看今日计划 (Priority: P1)

用户在 plan 页能看到今天是训练日还是休息日，以及今日训练部位。

**Why this priority**: 计划页的核心价值是回答"今天练什么"。

**Independent Test**: 给定 start_date、cycle、muscle_rotation 和今天日期，今日计划接口返回训练/休息状态和部位。

**Acceptance Scenarios**:

1. **Given** 今天落在训练段，**When** 查询今日计划，**Then** 返回 isTrainingDay=true 和今日部位。
2. **Given** 今天落在休息段，**When** 查询今日计划，**Then** 返回 isTrainingDay=false，部位为空。
3. **Given** start_date 在未来，**When** 查询今日计划，**Then** 返回 isTrainingDay=false，cycleDayIndex=0，表示未开始。

### Edge Cases

- 首次访问无 profile/plan：返回 data=null，前端展示引导设置，不报错。
- 体重非法（负数、零、>500kg）：返回 VALIDATION_ERROR。
- start_date 在未来：今日算"未开始"，isTrainingDay=false。
- muscle_rotation 为空：训练日只返回 isTrainingDay=true，todayBodyPart 为空。
- 差值为负（当前 < 目标，增重场景）：正常展示负数，前端文案区分"需增重"。
- 同一天多次更新体重：每次更新都插入一条 body_metric_record（保留多次记录）。

## Requirements

### Functional Requirements

- **FR-001**: `GET /api/v1/profile` 返回当前体重、目标体重、差值、身高、生日、性别、每日热量目标。
- **FR-002**: `PUT /api/v1/profile` 更新个人资料；当 `currentWeightKg` 变化时，在同一事务内插入 `body_metric_record`（record_date=今天，weight_kg=新值）。
- **FR-003**: `GET /api/v1/body-metrics?limit=N` 返回最近 N 条体重历史，按日期倒序。
- **FR-004**: `GET /api/v1/plans/current` 返回当前训练计划。
- **FR-005**: `PUT /api/v1/plans/current` 更新训练计划（upsert，每用户至多一条当前计划）。
- **FR-006**: `GET /api/v1/plans/current/today` 返回今日计划（训练/休息 + 部位 + 周期内天数）。
- **FR-007**: 差值 = `current_weight_kg - target_weight_kg`。
- **FR-008**: 训练周期计算：从 `start_date` 起，按 `train_days + rest_days` 循环，算今天落在训练段还是休息段。
- **FR-009**: 部位轮换：已过训练日数 % `muscle_rotation.length` 算今日部位。
- **FR-010**: 前端 profile 页展示体重目标差值，支持更新当前体重和目标体重。
- **FR-011**: 前端 plan 页展示今日计划标签和训练周期配置。
- **FR-012**: 所有 API 按认证用户（`X-User-Id`）隔离，不信任请求体中的 user_id。
- **FR-013**: 体重历史双写与 profile 更新在同一事务，保证一致性。

### Key Entities

- **个人资料**：current_weight_kg、target_weight_kg、daily_calorie_target、height_cm、birthday、gender、weight_diff_kg（计算字段）。
- **体重历史记录**：record_date、weight_kg、body_fat_percent、waist_cm、note。
- **训练计划**：name、cycle_type、train_days、rest_days、start_date、muscle_rotation、daily_calorie_target、reminder_enabled、reminder_time。
- **今日计划**：date、is_training_day、cycle_day_index、today_body_part、days_since_start。

## Success Criteria

### Measurable Outcomes

- **SC-001**: 用户能在 profile 页 1 屏内看到当前体重、目标体重、差值。
- **SC-002**: 更新体重后，body_metric_record 多一条记录，profile 快照同步，差值刷新。
- **SC-003**: 用户能在 plan 页配置训练周期并看到今日训练/休息状态和部位。
- **SC-004**: 差值计算和周期计算有聚焦的单元测试覆盖。
- **SC-005**: 后端回归测试保持通过（21+ 新增测试不破坏现有 21 个）。

## Assumptions

- 开发模式用户 ID `1001`，通过 `X-User-Id` 头传入。
- 首次 `GET /profile` 或 `GET /plans/current` 无记录时返回 `data=null`，前端引导设置；`PUT` 时 upsert。
- `muscle_rotation_json` 格式为字符串数组，如 `["胸","肩","背"]`。
- 本轮只做体重历史列表，不做曲线图。
- 本轮只存储 `reminder_enabled`/`reminder_time`，不实现推送触发。
- 周期计算基于日历日（自然日），不考虑跨时区。
