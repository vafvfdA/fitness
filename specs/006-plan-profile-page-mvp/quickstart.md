# Quickstart: 计划与个人资料页面联调

## 自动化验证

```powershell
cd D:\develop\javafile\fitness
node miniapp\tests\view-model.test.js
node --check miniapp\pages\profile\index.js
node --check miniapp\pages\plan\index.js
cd D:\develop\javafile\fitness\backend
mvn test
```

预期：

- view-model 映射测试通过（含新增的 profile 差值和今日计划计算）。
- profile/plan 页 JS 语法检查通过。
- 后端完整测试通过（原 21 + 新增 Profile/BodyMetric/Plan 测试）。

## 手动联调

1. 启动 MySQL 和后端。
2. 用微信开发者工具打开 `miniapp`。
3. 打开 profile 页，首次访问展示空状态或引导设置。
4. 设置当前体重 75、目标体重 70，保存，确认差值显示 5。
5. 再次更新当前体重为 74.5，保存，确认差值变为 4.5，历史列表多一条今日记录。
6. 打开 plan 页，首次访问展示空状态。
7. 配置练三休一、部位轮换 [胸,肩,背]、start_date 设为今天往前若干天，保存。
8. 确认今日计划区域展示训练/休息状态和今日部位。
9. 切换 start_date 到未来某天，确认今日计划显示"未开始"。
10. 确认 profile 页和 plan 页的加载、空、失败状态都正常。
