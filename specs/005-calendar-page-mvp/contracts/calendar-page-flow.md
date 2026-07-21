# Contract: 训练日历页面联调

## 月历加载

请求：

```http
GET /api/v1/workout-records/calendar?month=2026-07
X-User-Id: 1001
```

成功响应：

```json
{
  "code": "OK",
  "message": "success",
  "data": {
    "month": "2026-07",
    "days": [
      {
        "date": "2026-07-21",
        "bodyParts": ["胸", "肩"],
        "totalSets": 5,
        "estimatedCalories": 300,
        "workoutCount": 1
      }
    ]
  }
}
```

## 页面跳转

点击日期进入训练页：

```text
/pages/workout/index?date=yyyy-MM-dd
```

点击饮食入口进入饮食页：

```text
/pages/diet/index?date=yyyy-MM-dd
```

## 错误处理

- 请求失败时展示错误提示。
- 无数据时仍展示完整月历。
