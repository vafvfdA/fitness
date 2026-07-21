# Contract: 计划与个人资料页面联调

## 获取个人资料

请求：

```http
GET /api/v1/profile
X-User-Id: 1001
```

成功响应（有数据）：

```json
{
  "code": "OK",
  "message": "success",
  "data": {
    "userId": 1001,
    "gender": "male",
    "heightCm": 175.00,
    "birthday": "1995-01-01",
    "currentWeightKg": 75.00,
    "targetWeightKg": 70.00,
    "dailyCalorieTarget": 2000,
    "weightDiffKg": 5.00
  }
}
```

成功响应（首次无数据）：

```json
{ "code": "OK", "message": "success", "data": null }
```

## 更新个人资料

请求：

```http
PUT /api/v1/profile
X-User-Id: 1001
Content-Type: application/json

{
  "gender": "male",
  "heightCm": 175.00,
  "birthday": "1995-01-01",
  "currentWeightKg": 74.50,
  "targetWeightKg": 70.00,
  "dailyCalorieTarget": 2000
}
```

成功响应：同 GET 的有数据响应，`weightDiffKg` 为 4.50。

说明：当请求中的 `currentWeightKg` 与现有 profile 不同时，在同一事务内插入 `body_metric_record`（`record_date`=今天，`weight_kg`=新值）。upsert 语义：无则新建，有则更新。

## 查询体重历史

请求：

```http
GET /api/v1/body-metrics?limit=30
X-User-Id: 1001
```

成功响应：

```json
{
  "code": "OK",
  "message": "success",
  "data": [
    { "id": 10, "recordDate": "2026-07-21", "weightKg": 74.50, "bodyFatPercent": null, "waistCm": null, "note": null },
    { "id": 9, "recordDate": "2026-07-20", "weightKg": 75.00, "bodyFatPercent": null, "waistCm": null, "note": null }
  ]
}
```

说明：`limit` 默认 30，最大 90；按 `record_date` 倒序，同日多条按 `id` 倒序。

## 获取当前训练计划

请求：

```http
GET /api/v1/plans/current
X-User-Id: 1001
```

成功响应（有数据）：

```json
{
  "code": "OK",
  "message": "success",
  "data": {
    "id": 1,
    "name": "练三休一",
    "cycleType": "TRAIN_REST",
    "trainDays": 3,
    "restDays": 1,
    "startDate": "2026-07-01",
    "muscleRotation": ["胸", "肩", "背"],
    "dailyCalorieTarget": 2000,
    "reminderEnabled": false,
    "reminderTime": null
  }
}
```

成功响应（首次无数据）：

```json
{ "code": "OK", "message": "success", "data": null }
```

## 更新训练计划

请求：

```http
PUT /api/v1/plans/current
X-User-Id: 1001
Content-Type: application/json

{
  "name": "练三休一",
  "trainDays": 3,
  "restDays": 1,
  "startDate": "2026-07-01",
  "muscleRotation": ["胸", "肩", "背"],
  "dailyCalorieTarget": 2000,
  "reminderEnabled": false,
  "reminderTime": null
}
```

成功响应：同 GET 的有数据响应。upsert 语义：每用户至多一条当前计划。

## 获取今日计划

请求：

```http
GET /api/v1/plans/current/today
X-User-Id: 1001
```

成功响应（训练日）：

```json
{
  "code": "OK",
  "message": "success",
  "data": {
    "date": "2026-07-21",
    "isTrainingDay": true,
    "cycleDayIndex": 2,
    "todayBodyPart": "肩",
    "daysSinceStart": 20
  }
}
```

成功响应（休息日）：

```json
{
  "code": "OK",
  "message": "success",
  "data": {
    "date": "2026-07-21",
    "isTrainingDay": false,
    "cycleDayIndex": 4,
    "todayBodyPart": "",
    "daysSinceStart": 20
  }
}
```

成功响应（无计划）：

```json
{ "code": "OK", "message": "success", "data": null }
```

## 错误处理

- 无 profile/plan：返回 `data=null`，HTTP 200，前端引导设置。
- 体重非法（负数、零、>500kg）：`VALIDATION_ERROR`。
- 训练天数非法（<1、>14）：`VALIDATION_ERROR`。
- 日期格式非法：`VALIDATION_ERROR`。
- 未认证（无 `X-User-Id`）：`UNAUTHORIZED`。
