# Research: 今日训练饮食聚合接口

## Decision: 新增 today 聚合模块

**Rationale**: 今日页是跨训练和饮食的视图，单独建 today 模块可以保持 workout 和 diet 模块边界清晰。

**Alternatives considered**:

- 放在 workout 或 diet 模块：不采用，会让单一业务模块承担跨模块聚合职责。

## Decision: 实时查询聚合

**Rationale**: MVP 阶段数据量小，实时聚合足够简单直接，避免维护冗余汇总表。

**Alternatives considered**:

- 新增 daily_summary 表：暂不采用，会带来同步和一致性成本。

## Decision: 净热量计算为摄入减消耗

**Rationale**: 今日页需要直观看到饮食摄入扣除训练消耗后的结果。

**Alternatives considered**:

- 只返回摄入和消耗，不返回净热量：不采用，会把计算负担交给前端。
