# Contract: 小程序记录 MVP 联调

## 今日页

加载时请求：

```http
GET /api/v1/today/summary?date=yyyy-MM-dd
X-User-Id: 1001
```

页面展示：

- 训练次数
- 训练部位
- 训练组数
- 训练消耗
- 饮食摄入
- 净热量

## 训练页

加载时请求：

```http
GET /api/v1/workout-records?date=yyyy-MM-dd
GET /api/v1/workout-templates
```

无模板时可以创建示例模板：

```http
POST /api/v1/workout-templates
```

选择模板生成训练记录：

```http
POST /api/v1/workout-templates/{id}/workout-records
```

请求体：

```json
{
  "workoutDate": "2026-07-21",
  "note": "小程序模板生成"
}
```

## 饮食页

加载时请求：

```http
GET /api/v1/diet-records?date=yyyy-MM-dd
```

新增饮食：

```http
POST /api/v1/diet-records
```

请求体：

```json
{
  "dietDate": "2026-07-21",
  "note": "小程序录入",
  "foods": [
    {
      "mealType": "早餐",
      "foodName": "鸡蛋",
      "amount": 2,
      "unit": "个",
      "calories": 140,
      "proteinG": 12,
      "fatG": 10,
      "carbG": 1
    }
  ]
}
```

## 错误处理

- 请求失败时页面使用 toast 提示。
- 页面保留当前输入，不清空用户未提交数据。
