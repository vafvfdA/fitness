# Data Model: 今日训练饮食聚合接口

## 今日聚合

接口响应模型，不单独建表。

| 字段 | 含义 |
| --- | --- |
| date | 聚合日期 |
| workout | 训练汇总 |
| diet | 饮食汇总 |
| netCalories | 净热量，等于饮食总热量减训练估算消耗 |

## 训练汇总

来源表：`workout_record`

| 字段 | 含义 |
| --- | --- |
| workoutCount | 指定日期训练记录数 |
| bodyParts | 去重后的训练部位列表 |
| totalSets | 总组数 |
| estimatedCalories | 估算训练消耗 |

## 饮食汇总

来源表：`diet_record`、`diet_food_entry`

| 字段 | 含义 |
| --- | --- |
| recordCount | 指定日期饮食记录数 |
| foodCount | 食物条目数量 |
| totalCalories | 饮食总热量 |
| proteinG | 蛋白质合计 |
| fatG | 脂肪合计 |
| carbG | 碳水合计 |
