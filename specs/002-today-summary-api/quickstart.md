# Quickstart: 今日训练饮食聚合接口

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

先通过训练记录接口和饮食记录接口写入同一天数据，然后查询：

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/today/summary?date=2026-07-21" `
  -Headers @{ "X-User-Id" = "1" }
```

## 预期结果

- `workout` 返回训练次数、训练部位、总组数和估算消耗。
- `diet` 返回饮食记录数、食物数量、热量和三大营养素。
- `netCalories` 等于 `diet.totalCalories - workout.estimatedCalories`。
