# 0006 训练记录后端接口测试

## 测试策略

本变更按红绿开发执行：

1. 先新增 `WorkoutRecordControllerTest`，覆盖训练记录新增、按日期查询、月历摘要和参数校验。
2. 首次运行测试时，接口未实现，测试因 `/api/v1/workout-records` 返回 404 失败，符合 RED 预期。
3. 补充 Controller、Service、Repository 和 DTO 后，再运行测试进入 GREEN。
4. 最后运行完整后端测试，确认没有影响健康检查和数据库迁移测试。

## 测试项

| 编号 | 测试项 | 覆盖内容 | 结果 |
| --- | --- | --- | --- |
| T-1 | `createsWorkoutRecordWithExercisesAndSets` | 新增训练记录，返回记录 ID、动作 ID、组 ID、用户 ID、训练日期、部位、热量和后端计算的总组数 | 通过 |
| T-2 | `returnsWorkoutRecordsByDateForCurrentUser` | 按日期查询时，只返回当前用户和指定日期的数据 | 通过 |
| T-3 | `returnsCalendarSummaryByMonth` | 按月份聚合日期、训练部位、总组数、估算热量和训练次数 | 通过 |
| T-4 | `rejectsWorkoutRecordWithoutBodyPart` | 缺少必填训练部位时返回 400 和统一错误响应 | 通过 |
| T-5 | 完整后端测试 | 健康检查、数据库迁移、训练记录接口全部通过 | 通过 |

## 执行记录

### RED

命令：

```powershell
cd D:\develop\javafile\fitness\backend
mvn -Dtest=WorkoutRecordControllerTest test
```

结果：

```text
Tests run: 4, Failures: 4, Errors: 0, Skipped: 0
Status expected:<200> but was:<404>
```

说明：训练记录接口尚不存在，测试因 404 失败，符合预期。

### GREEN

命令：

```powershell
cd D:\develop\javafile\fitness\backend
mvn -Dtest=WorkoutRecordControllerTest test
```

结果：

```text
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### 完整验证

命令：

```powershell
cd D:\develop\javafile\fitness\backend
mvn test
```

结果：

```text
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## 备注

- 普通沙箱运行 Maven 时仍会遇到 `backend/target` 写入拒绝，因此测试使用提权执行。
- 测试环境使用 H2 MySQL 模式验证接口和 Flyway 迁移，真实 MySQL 联调需要启动本地 Docker MySQL 后再启动后端。
