# 0005 Docker MySQL 端口调整规格

## 背景

本机已有其他项目的 MySQL 容器占用 `3306`：

```text
9846183ce630   mysql   mysql:8   0.0.0.0:3306->3306/tcp
```

如果本项目继续把 Docker MySQL 映射到本机 `3306`，会和已有容器端口冲突。

## 目标

将本项目 Docker MySQL 的本机端口调整为 `3307`，容器内部仍使用 MySQL 默认 `3306`，并让后端默认连接配置和 README 保持一致。

## 范围内

- 修改 `docker-compose.yml` 端口映射。
- 修改后端默认数据源 URL。
- 更新 README 中 Docker Desktop、Navicat 和环境变量说明。
- 校验 Compose 配置。
- 运行后端测试。

## 范围外

- 不停止或删除已有 `mysql` 容器。
- 不启动本项目 MySQL 容器。
- 不修改 Flyway 建表脚本。
- 不实现业务 API。
- 不执行 git commit 或 push。

## 配置约定

| 配置 | 值 |
| --- | --- |
| 本机连接端口 | `3307` |
| 容器内部端口 | `3306` |
| 数据库 | `fitness` |
| 普通用户 | `fitness` |
| 普通用户密码 | `fitness` |
| root 密码 | `root` |

## 验收标准

| 编号 | 标准 |
| --- | --- |
| AC-1 | `docker-compose.yml` 使用 `3307:3306`。 |
| AC-2 | 后端默认 MySQL URL 使用 `localhost:3307`。 |
| AC-3 | README 中 Navicat 和环境变量说明使用 `3307`。 |
| AC-4 | `docker compose config` 通过。 |
| AC-5 | 后端 `mvn test` 通过。 |

