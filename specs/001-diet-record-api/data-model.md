# Data Model: 饮食记录后端接口

## 饮食记录

对应表：`diet_record`

| 字段 | 含义 | 校验 |
| --- | --- | --- |
| id | 饮食记录 ID | 数据库生成 |
| user_id | 用户 ID | 必填，来自 `X-User-Id` |
| diet_date | 饮食日期 | 必填 |
| total_calories | 总热量 | 后端按食物明细合计 |
| note | 备注 | 可选 |

## 食物明细

对应表：`diet_food_entry`

| 字段 | 含义 | 校验 |
| --- | --- | --- |
| id | 食物明细 ID | 数据库生成 |
| diet_record_id | 所属饮食记录 ID | 必填 |
| user_id | 用户 ID | 必填 |
| meal_type | 餐次 | 必填，例如 breakfast、lunch、dinner、snack |
| food_name | 食物名称 | 必填 |
| amount | 数量 | 必填，必须大于 0 |
| unit | 单位 | 必填 |
| calories | 热量 | 必填，不能为负数 |
| protein_g | 蛋白质 | 可选，不能为负数 |
| fat_g | 脂肪 | 可选，不能为负数 |
| carb_g | 碳水 | 可选，不能为负数 |
| sort_order | 排序 | 后端按请求顺序生成 |

## 饮食汇总

接口响应模型，不单独建表。

| 字段 | 含义 |
| --- | --- |
| date | 汇总日期 |
| totalCalories | 总热量 |
| proteinG | 蛋白质合计 |
| fatG | 脂肪合计 |
| carbG | 碳水合计 |
| foodCount | 食物条目数量 |
| recordCount | 饮食记录数量 |
