# Research: 训练模板接口

## Decision: 复用现有模板表，并通过 V2 补充动作预估热量字段

**Rationale**: `V1__create_core_tables.sql` 已包含 `workout_template` 和 `workout_template_item`，但模板动作缺少 `estimated_calories`。用户录入模板时需要保留动作预估消耗，并在从模板生成训练记录时汇总到训练记录和动作明细，因此通过 Flyway V2 增加该字段。

**Alternatives considered**:

- 不保存动作预估热量：会丢失 A 界面模板中的核心字段，生成训练记录时无法计算热量。
- 使用 JSON 存储模板动作：不利于按动作排序、校验和后续统计。

## Decision: 从模板生成训练记录时直接写入已有训练记录表

**Rationale**: 已有训练记录接口的数据结构已经稳定，模板生成后的结果应与手动创建训练记录一致，便于今日汇总和日历接口复用。

**Alternatives considered**:

- 先生成草稿表：当前没有草稿编辑流程，超出本轮范围。
- 只返回生成预览不落库：不能支撑 A 界面快速创建当天记录的目标。

## Decision: 模板接口独立放在 `workout/template` 子包

**Rationale**: 模板和训练记录同属 workout 领域，但生命周期和接口职责不同；独立子包能保持代码边界清晰。

**Alternatives considered**:

- 直接放入 `workout/api`：短期少建目录，但后续编辑、删除、系统模板等功能会让 workout 包职责膨胀。

## Decision: 本轮不实现系统模板初始化、编辑和删除

**Rationale**: 当前最急需的是小程序快速录入闭环。编辑、删除和系统模板数据初始化可以在模板基础能力稳定后单独迭代。

**Alternatives considered**:

- 一次性实现完整模板管理：范围过大，影响测试先行和联调节奏。
