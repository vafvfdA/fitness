# Quickstart: 训练日历页面联调

## 自动化验证

```powershell
cd D:\develop\javafile\fitness
node miniapp\tests\view-model.test.js
node --check miniapp\pages\calendar\index.js
cd D:\develop\javafile\fitness\backend
mvn test
```

预期：

- 日历映射测试通过。
- 日历页 JS 语法检查通过。
- 后端完整测试通过。

## 手动联调

1. 启动 MySQL 和后端。
2. 用微信开发者工具打开 `miniapp`。
3. 先在训练页生成几条不同日期的训练记录。
4. 进入日历页，确认有训练的日期显示部位和热量。
5. 点击上月/下月，确认月份切换。
6. 点击某个日期，确认进入对应日期的训练页。
7. 点击饮食入口，确认进入对应日期的饮食页。
