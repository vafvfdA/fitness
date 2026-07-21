# Research: 计划与个人资料页面联调

## Decision: 体重更新采用事务双写（方案 B）

**Rationale**: 用户明确选方案 B。更新 `current_weight_kg` 时，既更新 `user_profile` 快照（便于快速读取当前体重和差值），又在同一事务插入 `body_metric_record`（保留历史曲线数据）。快照与历史分离：读路径只查 profile，简单高效；写路径在 application 层用 `@Transactional` 保证一致。

**Alternatives considered**:

- 只存 profile（方案 A）：闭环快但丢失历史，用户已否决。
- 只存 body_metric_record、profile 实时取最新：读 profile 需子查询或 join，复杂且慢。

## Decision: 训练周期按 start_date 绝对计算（纯函数）

**Rationale**: 以 `start_date` 为锚点，`(today - start_date)` 天数对 `train_days + rest_days` 取余，判断落在训练段还是休息段；已过训练日数对 `muscle_rotation.length` 取余算部位。整个计算是无状态纯函数，可直接单元测试，无需每日更新任何"当前进度"字段。

**Alternatives considered**:

- 记录 `last_cycle_day` 字段每日递增：需定时任务或每次访问更新，状态化、难测试、易漂移。
- 按星期几固定：不够灵活，不支持"练三休一"这类非整周周期。

## Decision: 首次访问返回 data=null 而非 404

**Rationale**: 新用户没有 profile/plan 是正常初始状态，不是错误。返回 `data=null` 配合 HTTP 200，前端展示引导设置界面。404 语义上表示"资源不存在且不该访问"，会误导前端走错误处理路径。

**Alternatives considered**:

- 返回 404：前端要区分"无数据"和"真错误"，分支冗余。
- 自动创建默认 profile：隐式副作用，不符合"先理解再实现"，且默认值难定。

## Decision: 今日计划独立接口

**Rationale**: today 计算依赖 plan + 当前日期，独立接口 `GET /plans/current/today` 让前端 plan 页一次拿到今日状态，避免前端重复实现周期算法导致前后端不一致。后端是计算的唯一真相源。

**Alternatives considered**:

- 前端拿 plan 自己算：周期逻辑前后端各一份，易不一致。
- 合并到 `/plans/current` 一起返回 today：每次取计划都算 today，多数场景用不到，浪费。

## Decision: muscle_rotation 用 JSON 数组

**Rationale**: 表里已是 `muscle_rotation_json`（MySQL json 类型），直接存 `["胸","肩","背"]`。部位轮换低频更新、长度不规则，JSON 比再建 `training_plan_rotation` 关联表更轻。MyBatis-Plus 可配 JSON handler 自动序列化。

**Alternatives considered**:

- 拆 training_plan_rotation 关联表：过度设计，查询要多一次 join。

## Decision: 同日多次更新体重保留多条历史

**Rationale**: 每次更新 `current_weight_kg` 都插入一条 `body_metric_record`，不覆盖同日记录。理由：用户可能一天称多次（早晚），保留全部数据用于分析；查询历史按 `id` 倒序，最新在前。

**Alternatives considered**:

- 同日覆盖只留最新：丢失中间数据，不符合"历史"语义。
