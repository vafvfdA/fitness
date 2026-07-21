# Research: 训练日历页面联调

## Decision: 使用现有训练日历后端接口

**Rationale**: 后端已有 `GET /workout-records/calendar?month=yyyy-MM`，能返回指定月份有训练日期的摘要，足够支撑月历视图。

**Alternatives considered**:

- 新增前端聚合接口：当前没有必要，会重复后端已有能力。
- 用训练记录列表前端聚合：需要多次按日期请求，效率和复杂度都更差。

## Decision: 前端生成完整月历格子

**Rationale**: 后端只返回有训练的日期，前端需要补齐空白日期和无训练日期，才能形成稳定月视图。

**Alternatives considered**:

- 后端返回完整月历：会把 UI 布局细节推给后端，不利于前后端边界。

## Decision: 日期点击默认进入训练页

**Rationale**: 日历展示的是训练摘要，点击日期进入训练页最符合用户预期；饮食入口作为辅助按钮提供。

**Alternatives considered**:

- 点击日期弹出选择菜单：交互更完整，但本轮 MVP 没必要增加弹层复杂度。
