# Research: 饮食记录后端接口

## Decision: 复用已有饮食表

**Rationale**: `diet_record` 和 `diet_food_entry` 已经覆盖本轮新增、查询和汇总所需字段，不需要新增迁移。

**Alternatives considered**:

- 新增按日汇总表：暂不采用，MVP 阶段可实时聚合，避免提前维护冗余数据。

## Decision: 后端计算总热量

**Rationale**: 总热量来自食物明细合计，后端计算可以避免前端传入总值和明细不一致。

**Alternatives considered**:

- 前端传 `totalCalories`：不采用，容易出现数据不一致。

## Decision: 继续使用 `X-User-Id`

**Rationale**: 当前微信登录和认证体系尚未完成，为了能尽快联调后端主链路，沿用训练记录接口的临时方案。

**Alternatives considered**:

- 先做完整登录认证：不采用，会扩大本轮范围。
