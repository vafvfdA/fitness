# 0003 数据库迁移规格

## 背景

当前后端只有健康检查接口，没有数据库迁移和表结构，无法支撑训练、饮食、计划等业务联调。

本变更基于 `sdd/reference/architecture.md` 中的表设计，将第一批核心表落到后端 Flyway 迁移脚本中，并提供自动化测试验证迁移可执行。

## 目标

让后端具备数据库迁移基础，使后续训练记录、饮食记录、日历和计划 API 可以基于真实表结构开发。

## 范围内

- 引入 Flyway。
- 引入 MySQL JDBC 驱动。
- 引入测试用 H2 数据库。
- 新增核心表迁移脚本。
- 新增迁移契约测试，验证迁移脚本可执行且核心表存在。
- 配置后端数据源占位。

## 范围外

- 不连接真实 MySQL 实例。
- 不实现实体、Mapper、Repository。
- 不实现训练或饮食 CRUD。
- 不实现真实用户登录。
- 不执行 git commit 或 push。

## 核心表

本变更创建以下表：

- `user_account`
- `user_profile`
- `body_metric_record`
- `workout_record`
- `workout_exercise_entry`
- `workout_set_entry`
- `workout_template`
- `workout_template_item`
- `diet_record`
- `diet_food_entry`
- `food_template`
- `training_plan`
- `knowledge_item`

## 数据影响

新增 `backend/src/main/resources/db/migration/V1__create_core_tables.sql`。

表名、字段名和主要字段类型应与 `sdd/reference/architecture.md` 保持一致。第一版使用 MySQL 8 兼容 SQL。

## API 影响

本变更不新增业务 API。

健康检查接口保持不变：

- `GET /api/v1/health`

## 配置影响

后端配置文件增加数据库和 Flyway 占位配置：

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `spring.flyway.enabled`
- `spring.flyway.locations`

本地没有真实 MySQL 时，应用测试通过 H2 配置验证迁移。

## 验收标准

| 编号 | 标准 |
| --- | --- |
| AC-1 | Maven 依赖包含 Flyway、MySQL 驱动和测试用 H2。 |
| AC-2 | 存在 `V1__create_core_tables.sql`。 |
| AC-3 | 自动化测试可以执行 Flyway 迁移。 |
| AC-4 | 自动化测试验证 13 张核心表存在。 |
| AC-5 | `mvn test` 通过，且原有健康检查测试不回退。 |

## 风险

- H2 的 MySQL 兼容模式不能完全等同真实 MySQL。
- 真正连接 MySQL 仍需要用户本机提供数据库地址、账号和密码。
- 后续实体和 Mapper 仍需单独 change 实现。

## 开放问题

- 本地 MySQL 库名是否使用 `fitness`。
- 开发环境数据库账号是否使用环境变量注入。

