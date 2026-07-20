# Quickstart: 训练模板接口

## 前置条件

- MySQL Docker 容器已启动，或使用测试环境 H2。
- 后端应用可启动。
- 请求头携带 `X-User-Id`。

## 自动化测试

```powershell
cd D:\develop\javafile\fitness\backend
mvn -Dtest=WorkoutTemplateControllerTest test
mvn test
```

预期结果：

- 训练模板接口测试通过。
- 完整后端测试通过。

## 手动验收场景

### 创建模板

请求：

```http
POST /api/v1/workout-templates
X-User-Id: 1001
Content-Type: application/json
```

请求体参考 [接口契约](./contracts/workout-template-api.md)。

预期：

- 返回 `code = OK`。
- 返回模板 ID。
- 动作列表按创建顺序返回。

### 查询模板

请求：

```http
GET /api/v1/workout-templates?bodyPart=胸
X-User-Id: 1001
```

预期：

- 只返回当前用户的胸部模板。

### 从模板生成训练记录

请求：

```http
POST /api/v1/workout-templates/{id}/workout-records
X-User-Id: 1001
Content-Type: application/json
```

预期：

- 返回训练记录详情。
- `totalSets` 等于模板动作默认组数之和。
- 生成后的记录可被 `GET /api/v1/workout-records?date=yyyy-MM-dd` 查询到。
