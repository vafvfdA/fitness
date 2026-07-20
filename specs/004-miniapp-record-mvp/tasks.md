# Tasks: 小程序记录 MVP 联调

**Input**: Design documents from `specs/004-miniapp-record-mvp/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/

**Tests**: 本项目宪章要求测试先行，本任务清单包含前端映射测试和后端回归测试。

## Phase 1: Setup

- [x] T001 确认 `miniapp/pages/today`、`miniapp/pages/workout`、`miniapp/pages/diet` 已存在
- [x] T002 确认后端训练、饮食、今日汇总、训练模板接口已存在

---

## Phase 2: Foundational

- [x] T003 创建失败测试 `miniapp/tests/view-model.test.js`
- [x] T004 运行 `node miniapp/tests/view-model.test.js` 并确认因映射工具不存在失败
- [x] T005 实现页面数据映射工具 `miniapp/utils/view-model.js`
- [x] T006 更新请求工具 `miniapp/utils/request.js`，统一携带 `X-User-Id`
- [x] T007 更新开发期配置 `miniapp/app.js`
- [x] T008 确认 `.specify/feature.json` 指向 `specs/004-miniapp-record-mvp`

---

## Phase 3: User Story 1 - 查看今日汇总 (Priority: P1) MVP

**Goal**: 首页展示今日训练、饮食和净热量。

**Independent Test**: 后端有无数据时，今日页都能正常展示。

- [x] T009 [US1] 实现今日页逻辑 `miniapp/pages/today/index.js`
- [x] T010 [US1] 实现今日页结构 `miniapp/pages/today/index.wxml`
- [x] T011 [US1] 实现今日页样式 `miniapp/pages/today/index.wxss`

---

## Phase 4: User Story 2 - 使用训练模板生成训练记录 (Priority: P2)

**Goal**: 训练页可以展示模板和训练记录，并从模板生成当天训练。

**Independent Test**: 选择模板后，当天训练记录刷新。

- [x] T012 [US2] 实现训练页逻辑 `miniapp/pages/workout/index.js`
- [x] T013 [US2] 实现训练页结构 `miniapp/pages/workout/index.wxml`
- [x] T014 [US2] 实现训练页样式 `miniapp/pages/workout/index.wxss`

---

## Phase 5: User Story 3 - 录入饮食记录 (Priority: P3)

**Goal**: 饮食页可以展示当天饮食记录，并提交一条食物记录。

**Independent Test**: 提交食物后，当天饮食列表和汇总刷新。

- [x] T015 [US3] 实现饮食页逻辑 `miniapp/pages/diet/index.js`
- [x] T016 [US3] 实现饮食页结构 `miniapp/pages/diet/index.wxml`
- [x] T017 [US3] 实现饮食页样式 `miniapp/pages/diet/index.wxss`

---

## Phase 6: Polish & Cross-Cutting Concerns

- [x] T018 运行 `node miniapp/tests/view-model.test.js`
- [x] T019 运行 `mvn test` 验证后端回归
- [x] T020 更新 `specs/004-miniapp-record-mvp/tasks.md` 任务状态
- [x] T021 记录微信开发者工具未自动打开的手动验收风险

## Dependencies & Execution Order

- Phase 1 和 Phase 2 先完成。
- US1 是 MVP，先完成今日页。
- US2 和 US3 可在 US1 后顺序完成。
- 所有实现任务必须在对应测试任务之后执行。
