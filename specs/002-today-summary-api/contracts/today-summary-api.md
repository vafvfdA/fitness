# Contract: 今日训练饮食聚合接口

## 查询今日聚合

`GET /api/v1/today/summary?date=2026-07-21`

请求头：

```text
X-User-Id: 1
```

响应：

```json
{
  "code": "OK",
  "message": "success",
  "data": {
    "date": "2026-07-21",
    "workout": {
      "workoutCount": 1,
      "bodyParts": ["胸", "肩"],
      "totalSets": 3,
      "estimatedCalories": 320
    },
    "diet": {
      "recordCount": 1,
      "foodCount": 2,
      "totalCalories": 770,
      "proteinG": 40,
      "fatG": 20,
      "carbG": 100
    },
    "netCalories": 450
  }
}
```

无数据日期响应：

```json
{
  "code": "OK",
  "message": "success",
  "data": {
    "date": "2026-07-21",
    "workout": {
      "workoutCount": 0,
      "bodyParts": [],
      "totalSets": 0,
      "estimatedCalories": 0
    },
    "diet": {
      "recordCount": 0,
      "foodCount": 0,
      "totalCalories": 0,
      "proteinG": 0,
      "fatG": 0,
      "carbG": 0
    },
    "netCalories": 0
  }
}
