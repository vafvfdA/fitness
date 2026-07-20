# Contract: 饮食记录后端接口

## 新增饮食记录

`POST /api/v1/diet-records`

请求头：

```text
X-User-Id: 1
Content-Type: application/json
```

请求体：

```json
{
  "dietDate": "2026-07-20",
  "note": "训练日饮食",
  "foods": [
    {
      "mealType": "breakfast",
      "foodName": "燕麦",
      "amount": 80,
      "unit": "g",
      "calories": 300,
      "proteinG": 10,
      "fatG": 6,
      "carbG": 50
    }
  ]
}
```

响应：

```json
{
  "code": "OK",
  "message": "success",
  "data": {
    "id": 1,
    "userId": 1,
    "dietDate": "2026-07-20",
    "totalCalories": 300,
    "note": "训练日饮食",
    "foods": []
  }
}
```

## 查询某天饮食记录

`GET /api/v1/diet-records?date=2026-07-20`

请求头：

```text
X-User-Id: 1
```

响应 `data` 为饮食记录列表。

## 查询某天饮食汇总

`GET /api/v1/diet-records/summary?date=2026-07-20`

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
    "date": "2026-07-20",
    "totalCalories": 770,
    "proteinG": 40,
    "fatG": 20,
    "carbG": 100,
    "foodCount": 2,
    "recordCount": 1
  }
}
```
