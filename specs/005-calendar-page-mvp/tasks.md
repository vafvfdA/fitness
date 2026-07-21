# Tasks: 训练日历页面联调

**Input**: Design documents from `specs/005-calendar-page-mvp/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/

**Tests**: 本项目宪章要求测试先行，本任务清单包含前端映射测试和后端回归测试。

## Phase 1: Setup

- [x] T001 确认后端 `GET /workout-records/calendar?month=yyyy-MM` 已存在
- [x] T002 确认小程序 `miniapp/pages/calendar` 已存在

---

## Phase 2: Foundational

- [x] T003 在 `miniapp/tests/view-model.test.js` 中新增日历映射失败测试
- [x] T004 运行 `node miniapp/tests/view-model.test.js` 并确认因日历映射函数不存在失败
- [x] T005 在 `miniapp/utils/view-model.js` 中实现日历映射函数
- [x] T006 确认 `.specify/feature.json` 指向 `specs/005-calendar-page-mvp`

---

## Phase 3: User Story 1 - 查看月度训练日历 (Priority: P1) MVP

**Goal**: 日历页展示当前月份格子、训练部位和消耗热量。

**Independent Test**: 后端返回有训练日期后，页面对应日期展示摘要。

- [x] T007 [US1] 实现日历页加载逻辑 `miniapp/pages/calendar/index.js`
- [x] T008 [US1] 实现日历页结构 `miniapp/pages/calendar/index.wxml`
- [x] T009 [US1] 实现日历页样式 `miniapp/pages/calendar/index.wxss`

---

## Phase 4: User Story 2 - 切换月份 (Priority: P2)

**Goal**: 用户可以查看上月和下月训练摘要。

**Independent Test**: 点击切换按钮后请求对应月份。

- [x] T010 [US2] 在 `miniapp/pages/calendar/index.js` 实现上月/下月切换
- [x] T011 [US2] 在 `miniapp/pages/calendar/index.wxml` 增加月份切换控件

---

## Phase 5: User Story 3 - 从日期进入记录页 (Priority: P3)

**Goal**: 用户可以从日历日期进入训练页或饮食页。

**Independent Test**: 点击日期携带同一日期参数跳转。

- [x] T012 [US3] 在 `miniapp/pages/calendar/index.js` 实现日期跳转
- [x] T013 [US3] 在 `miniapp/pages/calendar/index.wxml` 增加日期点击和饮食入口

---

## Phase 6: Polish & Cross-Cutting Concerns

- [x] T014 运行 `node miniapp/tests/view-model.test.js`
- [x] T015 运行 `node --check miniapp/pages/calendar/index.js`
- [x] T016 运行 `mvn test` 验证后端回归
- [x] T017 更新 `specs/005-calendar-page-mvp/tasks.md` 任务状态
- [x] T018 记录微信开发者工具手动验收项

## Dependencies & Execution Order

- Phase 1 和 Phase 2 先完成。
- US1 是 MVP，先完成月历展示。
- US2 和 US3 在 US1 后完成。
- 所有实现任务必须在对应测试任务之后执行。
