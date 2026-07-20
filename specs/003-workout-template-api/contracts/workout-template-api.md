# Contract: 训练模板接口

## 创建训练模板

`POST /api/v1/workout-templates`

请求头：

- `X-User-Id`: 当前用户 ID

请求体：

```json
{
  "name": "胸日模板",
  "bodyPart": "胸",
  "description": "卧推为主的胸部训练",
  "items": [
    {
      "exerciseName": "卧推",
      "defaultSets": 3,
      "defaultReps": 10,
      "defaultWeightKg": 60.0,
      "defaultDurationSeconds": null,
      "estimatedCalories": 180
    }
  ]
}
```

成功响应：

```json
{
  "code": "OK",
  "message": "success",
  "data": {
    "id": 1,
    "userId": 1001,
    "name": "胸日模板",
    "bodyPart": "胸",
    "description": "卧推为主的胸部训练",
    "system": false,
    "items": [
      {
        "id": 1,
        "exerciseName": "卧推",
        "defaultSets": 3,
        "defaultReps": 10,
        "defaultWeightKg": 60.0,
        "defaultDurationSeconds": null,
        "sortOrder": 1,
        "estimatedCalories": 180
      }
    ]
  }
}
```

## 查询训练模板列表

`GET /api/v1/workout-templates`

查询参数：

- `bodyPart`: 可选，按训练部位筛选

成功响应：

```json
{
  "code": "OK",
  "message": "success",
  "data": [
    {
      "id": 1,
      "userId": 1001,
      "name": "胸日模板",
      "bodyPart": "胸",
      "description": "卧推为主的胸部训练",
      "system": false,
      "items": []
    }
  ]
}
```

## 查询训练模板详情

`GET /api/v1/workout-templates/{id}`

成功响应同创建训练模板。

## 从模板生成训练记录

`POST /api/v1/workout-templates/{id}/workout-records`

请求体：

```json
{
  "workoutDate": "2026-07-21",
  "note": "从模板生成"
}
```

成功响应：

- 响应结构与 `POST /api/v1/workout-records` 一致。
- 训练记录的动作、组数和默认参数来自模板。

## 错误约定

- 缺少必填字段或默认组数非法时返回 `400` 和 `VALIDATION_ERROR`。
- 访问不存在或无权限的模板时返回 `404` 和通用错误响应。
