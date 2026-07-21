# Contracts — 007 食物模板流程

## 接口

### 1. GET /api/v1/food-templates
查询可见食物模板（系统 + 本人自建），支持关键字搜索。

- 请求头：`X-User-Id: <userId>`
- 查询参数：`keyword`（可选，按 food_name 模糊匹配，大小写不敏感）
- 响应：`ApiResponse<List<FoodTemplateResponse>>`

```json
{
  "code": "OK",
  "message": "success",
  "data": [
    {
      "id": 1,
      "foodName": "鸡胸肉",
      "defaultUnit": "100g",
      "caloriesPerUnit": 133.00,
      "proteinPerUnit": 31.00,
      "fatPerUnit": 1.20,
      "carbPerUnit": 0.00,
      "isSystem": true,
      "mine": false
    },
    {
      "id": 100,
      "foodName": "自制蛋白棒",
      "defaultUnit": "个",
      "caloriesPerUnit": 220.00,
      "proteinPerUnit": 18.00,
      "fatPerUnit": 8.00,
      "carbPerUnit": 22.00,
      "isSystem": false,
      "mine": true
    }
  ]
}
```

- `mine`：是否本人自建（系统模板 false，本人自建 true）。
- 排序：系统模板在前，再按 food_name 升序（或纯 food_name 升序，实现取其一，保持稳定）。

### 2. POST /api/v1/food-templates
当前用户自建一条模板。

- 请求头：`X-User-Id: <userId>`
- 请求体：
```json
{
  "foodName": "自制蛋白棒",
  "defaultUnit": "个",
  "caloriesPerUnit": 220.0,
  "proteinPerUnit": 18.0,
  "fatPerUnit": 8.0,
  "carbPerUnit": 22.0
}
```
- 校验：`foodName` @NotBlank、`defaultUnit` @NotBlank、`caloriesPerUnit` @NotNull @Positive。蛋白/脂肪/碳水可空（@PositiveOrZero）。
- 响应：`ApiResponse<FoodTemplateResponse>`（同上结构，`isSystem=false`, `mine=true`）。
- 服务端强制 `user_id=当前`、`is_system=false`（忽略请求体里的 isSystem/userId）。

## 前端流程：饮食页「从模板选」

```
饮食页
  ├─ [从模板选] 按钮
  │    ├─ GET /food-templates?keyword=（首次加载全部，搜索框输入时按 keyword 重新查）
  │    ├─ 列表展示 foodName / defaultUnit / caloriesPerUnit
  │    ├─ 选中一项 → scaleFoodTemplate(template, amount=1) → 填入表单
  │    │     foodName      = template.foodName
  │    │     unit          = template.defaultUnit
  │    │     amount        = 1
  │    │     calories      = caloriesPerUnit * 1
  │    │     proteinG/fatG/carbG = per_unit * 1
  │    ├─ 用户改 amount → onInput 时重新 scaleFoodTemplate(template, newAmount) 刷新热量/宏量
  │    └─ [提交] → 走现有 POST /diet-records
  └─ [手动录入]（原流程保留）
```

## 自建模板流程

```
饮食页 → [从模板选] → [新增模板] 
  → 表单：foodName / defaultUnit / caloriesPerUnit / protein/fat/carb
  → POST /food-templates
  → 成功后回到模板列表（新模板可见）
```
