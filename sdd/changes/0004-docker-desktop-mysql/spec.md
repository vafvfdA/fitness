# 0004 Docker Desktop MySQL 规格

## 背景

用户希望使用 Docker Desktop 管理本地 MySQL，避免手动安装、启动 Windows MySQL 服务和手工创建数据库用户。

当前后端默认连接 `localhost:3306/fitness`，用户名和密码都是 `fitness`。如果 Docker Compose 创建同名数据库和用户，后端可以直接启动并由 Flyway 自动建表。

## 目标

提供一个可通过 Docker Desktop 和 `docker compose` 启动的本地 MySQL 8 开发环境，使后端、Navicat 和后续前后端联调使用同一套数据库。

## 范围内

- 新增根目录 `docker-compose.yml`。
- 使用 MySQL 8 容器。
- 自动创建 `fitness` 数据库。
- 自动创建 `fitness` 用户和密码。
- 暴露本机 `3306` 端口。
- 使用 Docker volume 持久化 MySQL 数据。
- 增加健康检查。
- 更新 README，说明 Docker Desktop 使用流程。

## 范围外

- 不启动 Docker 容器。
- 不拉取镜像。
- 不修改 Flyway 建表脚本。
- 不实现业务 API。
- 不执行 git commit 或 push。

## 配置约定

Docker MySQL 默认配置：

| 配置 | 值 |
| --- | --- |
| 容器服务名 | `fitness-mysql` |
| 数据库 | `fitness` |
| 普通用户 | `fitness` |
| 普通用户密码 | `fitness` |
| root 密码 | `root` |
| 本机端口 | `3306` |

后端默认配置已经与普通用户保持一致。

## 验收标准

| 编号 | 标准 |
| --- | --- |
| AC-1 | 根目录存在 `docker-compose.yml`。 |
| AC-2 | Compose 配置可以通过 `docker compose config` 校验。 |
| AC-3 | README 说明 Docker Desktop 启动 MySQL、Navicat 连接方式和后端启动方式。 |
| AC-4 | 后端测试仍通过。 |

## 风险

- 如果本机已有 MySQL 占用 3306，容器启动会端口冲突。
- 首次启动需要 Docker 拉取 MySQL 镜像，取决于网络。
- Docker CLI 可能因为用户目录下 Docker 配置权限产生警告。

