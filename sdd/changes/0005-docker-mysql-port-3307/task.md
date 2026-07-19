# 0005 Docker MySQL 端口调整任务

## 状态说明

- `[ ]` 未开始
- `[~]` 进行中
- `[x]` 已完成
- `[!]` 阻塞
- `[-]` 延期

## SDD 准备

1. `[x]` 确认端口冲突来源。
2. `[x]` 创建本变更 `spec.md`。
3. `[x]` 创建本变更 `task.md`、`test.md`、`review.md`。

## 实现

1. `[x]` 修改 `docker-compose.yml` 端口映射为 `3307:3306`。
2. `[x]` 修改后端默认数据源 URL 为 `localhost:3307`。
3. `[x]` 更新 README 中 Docker 和 Navicat 说明。

## 验证

1. `[x]` 运行 `docker compose config`。
2. `[x]` 运行后端 `mvn test`。
3. `[x]` 更新 `test.md`。
4. `[x]` 更新 `review.md`。
