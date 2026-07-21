# Quickstart — 007 食物模板

## 自动化验证

```powershell
cd D:\develop\javafile\fitness
node miniapp\tests\view-model.test.js
node --check miniapp\pages\diet\index.js
cd D:\develop\javafile\fitness\backend
mvn test
```

预期：
- view-model 测试通过（含新增 scaleFoodTemplate）。
- 饮食页 JS 语法检查通过。
- 后端测试通过（原 55 + 新增 FoodTemplate 测试）。

## 接口手测（后端在 8080）

```bash
B=http://127.0.0.1:8080/api/v1

# 查模板（应含系统种子，如鸡胸肉）
curl -s -H "X-User-Id:1001" $B/food-templates

# 搜索
curl -s -H "X-User-Id:1001" "$B/food-templates?keyword=鸡"

# 自建模板
curl -s -H "X-User-Id:1001" -H "Content-Type:application/json" -X POST $B/food-templates \
  --data-binary '@food.json'   # {"foodName":"自制蛋白棒","defaultUnit":"个","caloriesPerUnit":220,"proteinPerUnit":18,"fatPerUnit":8,"carbPerUnit":22}

# 跨用户隔离：1002 看不到 1001 自建的
curl -s -H "X-User-Id:1002" "$B/food-templates?keyword=蛋白棒"
```

⚠️ curl 注意：GET 不带 Content-Type；JSON 含中文用 `--data-binary @文件` 避免 shell 编码乱。

## 微信开发者工具端到端

1. 启动 MySQL + 后端（已在 3307/8080）。
2. 微信开发者工具打开 `miniapp`。
3. 进饮食页，点「从模板选」→ 看到系统食物列表（米饭/鸡蛋/鸡胸肉…）。
4. 搜索框输「鸡」→ 过滤出鸡蛋/鸡胸肉。
5. 选「鸡胸肉」→ 表单自动填：foodName=鸡胸肉, unit=100g, amount=1, calories=133, 蛋白31/脂肪1.2/碳水0。
6. 把 amount 改成 2 → calories 变 266、蛋白 62 等（缩放生效）。
7. 提交 → 当日饮食记录多一条鸡胸肉 200g 266kcal。
8. 点「新增模板」→ 填一条自定义食物 → 保存 → 列表里能看到它。
9. 切到另一个 devUserId（改 app.js devUserId 或换用户）→ 看不到上一步自建的模板（隔离）。

## 验收要点
- 模板列表首屏有数据（系统种子，非空）。
- 缩放正确（amount=2 热量翻倍）。
- 自建模板即时可见、跨用户隔离。
- 手动录入仍可用（模板没覆盖时兜底）。
