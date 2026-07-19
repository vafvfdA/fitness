# 0004 Docker Desktop MySQL 测试计划

## 测试范围

本测试计划验证 Docker Compose 配置可解析，且后端现有测试不受影响。

## 自动化测试

| 编号 | 命令或测试 | 预期结果 | 实际结果 | 状态 |
| --- | --- | --- | --- | --- |
| T-1 | `docker compose config` | Compose 配置可以正确展开。 | 通过；配置展开成功，Docker CLI 输出用户配置文件权限警告。 | 通过 |
| T-2 | `mvn test` | 后端测试全部通过。 | 通过；共 3 个测试，失败 0，错误 0，跳过 0。 | 通过 |

## 人工验收测试

| 编号 | 场景 | 步骤 | 预期结果 | 实际结果 | 状态 |
| --- | --- | --- | --- | --- | --- |
| M-1 | 检查 Docker 配置 | 查看 `docker-compose.yml`。 | MySQL 数据库、账号、端口、volume 和健康检查配置齐全。 | 通过；配置包含 MySQL 8.4、`fitness` 数据库、`fitness/fitness` 用户、`3306` 映射、volume 和健康检查。 | 通过 |
| M-2 | 检查 README | 查看 Docker Desktop 说明。 | 能知道如何启动 MySQL、连接 Navicat、启动后端。 | 通过；README 已补充 Docker Desktop、Navicat、端口冲突和后端说明。 | 通过 |

## 回归检查

- `[x]` 未修改 Flyway 建表脚本。
- `[x]` 未实现超出本变更范围的业务代码。
- `[x]` 未执行 git commit 或 push。

## 未解决测试风险

- 本变更默认不启动容器，因此不验证实际拉取镜像和容器运行。
- Docker CLI 当前提示 `C:\Users\wcf26\.docker\config.json` 访问被拒绝；配置解析仍通过。
