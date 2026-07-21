# Data Model — 007 食物模板

## 现有表（V1 已建，无需改结构）

### food_template
| 列 | 类型 | 说明 |
|---|---|---|
| id | bigint PK auto | |
| user_id | bigint null | 系统模板为 null；用户自建为当前用户 |
| food_name | varchar(128) not null | 食物名称 |
| default_unit | varchar(32) not null | 默认单位（100g / 个 / 100ml / 杯） |
| calories_per_unit | decimal(8,2) not null | 每单位热量 |
| protein_per_unit | decimal(8,2) | 每单位蛋白 g |
| fat_per_unit | decimal(8,2) | 每单位脂肪 g |
| carb_per_unit | decimal(8,2) | 每单位碳水 g |
| is_system | boolean not null default false | 系统模板标记 |
| created_at / updated_at | datetime | 自动维护 |

索引：`idx_food_template_user_name (user_id, food_name)`

## 可见性规则（查询核心）

```
WHERE is_system = true
   OR user_id = :currentUserId
```
- 系统模板对所有人可见（user_id is null, is_system=true）。
- 用户自建只对本人可见（user_id=当前, is_system=false）。
- 他人自建不可见（用户隔离）。

## V4 迁移：预置系统食物（新增）

`V4__seed_system_food_templates.sql`：约 30 条 `INSERT INTO food_template (user_id, food_name, default_unit, calories_per_unit, protein_per_unit, fat_per_unit, carb_per_unit, is_system) VALUES (NULL, ..., true)`。

涵盖：主食（米饭/馒头/面条/燕麦/红薯/全麦面包）、蛋白（鸡蛋/鸡胸肉/牛肉/猪肉/三文鱼/虾/豆腐/牛奶/酸奶/豆浆）、蔬菜（西兰花/菠菜/番茄/黄瓜/胡萝卜/土豆）、水果（苹果/香蕉/橙子）、坚果油脂（花生/核桃/杏仁/橄榄油）、补剂（蛋白粉）。

数值采用常见食物成分表（每 100g/个/100ml）。单位统一倾向 100g，少数用「个」「100ml」。

## diet_food_entry（不改）

饮食记录子表结构不变。模板选择器只是**前端填表单**，提交时仍走现有 `POST /diet-records`，存 food_name/amount/unit/calories/proteinG/fatG/carbG。**不存 food_template_id**（MVP 不做模板与记录的关联追溯）。

## 份量缩放（前端纯函数，不落库）

```
calories = round(calories_per_unit * amount)
proteinG = round(protein_per_unit * amount, 1)
fatG     = round(fat_per_unit * amount, 1)
carbG    = round(carb_per_unit * amount, 1)
```
- amount 默认 1（即 1 个单位）。用户可改。
- 缩放在前端 `scaleFoodTemplate` 完成；后端只存 per_unit 标准值，保证模板本身不被份量污染。
