# Quickstart: 饮食记录后端接口

## 前置条件

```powershell
cd D:\develop\javafile\fitness
docker compose up -d mysql
```

启动后端：

```powershell
cd D:\develop\javafile\fitness\backend
mvn spring-boot:run
```

## 自动化验证

```powershell
cd D:\develop\javafile\fitness\backend
mvn test
```

## 手工接口验收

新增饮食记录：

```powershell
$body = '{
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
    },
    {
      "mealType": "lunch",
      "foodName": "鸡胸饭",
      "amount": 1,
      "unit": "份",
      "calories": 470,
      "proteinG": 30,
      "fatG": 14,
      "carbG": 50
    }
  ]
}'

Invoke-RestMethod `
  -Method Post `
  -Uri http://localhost:8080/api/v1/diet-records `
  -Headers @{ "X-User-Id" = "1" } `
  -ContentType "application/json" `
  -Body $body
```

查询某天饮食记录：

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/diet-records?date=2026-07-20" `
  -Headers @{ "X-User-Id" = "1" }
```

查询某天饮食汇总：

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/diet-records/summary?date=2026-07-20" `
  -Headers @{ "X-User-Id" = "1" }
```

## 预期结果

- 新增接口返回饮食记录 ID、食物明细 ID 和总热量 770。
- 日期查询只返回当前用户指定日期的数据。
- 汇总接口返回总热量 770、蛋白质 40、脂肪 20、碳水 100、食物数量 2。
