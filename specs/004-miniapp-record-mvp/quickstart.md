# Quickstart: 小程序记录 MVP 联调

## 自动化验证

```powershell
cd D:\develop\javafile\fitness
node miniapp\tests\view-model.test.js
cd D:\develop\javafile\fitness\backend
mvn test
```

预期：

- 页面数据映射测试通过。
- 后端完整测试通过。

## 手动联调

1. 启动 MySQL Docker 容器。
2. 启动后端服务。
3. 用微信开发者工具打开 `miniapp` 目录。
4. 在开发者工具中关闭合法域名校验，或配置本地代理。
5. 打开今日页，确认汇总能加载。
6. 进入训练页，如果没有模板，点击创建示例模板。
7. 点击模板生成训练记录，确认当天训练记录刷新。
8. 进入饮食页，录入食物并提交。
9. 回到今日页，确认摄入、消耗和净热量变化。

## 开发期配置

- 后端地址在 `miniapp/app.js` 的 `apiBaseUrl`。
- 开发期用户 ID 在 `miniapp/app.js` 的 `devUserId`。
