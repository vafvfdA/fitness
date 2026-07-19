# 0006 训练记录后端接口任务

## 开发原则

- 遵守红绿开发：先写失败测试，再实现生产代码。
- 不修改无关模块。
- 不执行 git commit 或 git push。
- 所有文档描述使用中文。

## 任务列表

| 状态 | 任务 |
| --- | --- |
| 已完成 | 编写训练记录接口集成测试，覆盖新增、按日期查询、月历摘要和参数校验 |
| 已完成 | 新增训练记录请求和响应 DTO |
| 已完成 | 新增训练记录 Controller |
| 已完成 | 新增训练记录 Service |
| 已完成 | 新增基于 JDBC 的 Repository |
| 已完成 | 运行测试并修复失败项 |
| 已完成 | 编写 test.md 和 review.md |

## 接口清单

| 方法 | 路径 | 用途 |
| --- | --- | --- |
| POST | `/api/v1/workout-records` | 新增训练记录 |
| GET | `/api/v1/workout-records?date=yyyy-MM-dd` | 查询某天训练记录 |
| GET | `/api/v1/workout-records/calendar?month=yyyy-MM` | 查询月历摘要 |

## 实现备注

- 用户 ID 通过 `X-User-Id` 请求头传入。
- `totalSets` 由后端按动作组明细数量计算。
- 查询聚合在 Service 层完成，Repository 保持 SQL 访问职责。
