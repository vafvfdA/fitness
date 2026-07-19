# 0005 Docker MySQL 端口调整测试计划

## 测试范围

验证本项目 MySQL 容器本机端口改为 `3307` 后，配置可解析且后端测试不受影响。

## 自动化测试

| 编号 | 命令或测试 | 预期结果 | 实际结果 | 状态 |
| --- | --- | --- | --- | --- |
| T-1 | `docker compose config` | Compose 配置可解析，端口为 `3307:3306`。 | 通过；展开结果显示 `published: "3307"`、`target: 3306`。 | 通过 |
| T-2 | `mvn test` | 后端测试全部通过。 | 通过；共 3 个测试，失败 0，错误 0，跳过 0。 | 通过 |

## 人工验收测试

| 编号 | 场景 | 步骤 | 预期结果 | 实际结果 | 状态 |
| --- | --- | --- | --- | --- | --- |
| M-1 | 检查 Docker 端口 | 查看 `docker-compose.yml`。 | 本机端口为 `3307`。 | 通过；配置为 `"3307:3306"`。 | 通过 |
| M-2 | 检查后端默认配置 | 查看 `application.yml`。 | 默认 MySQL URL 使用 `3307`。 | 通过；默认 URL 为 `jdbc:mysql://localhost:3307/fitness`。 | 通过 |
| M-3 | 检查 README | 查看 Docker 和 Navicat 说明。 | 文档提示使用 `127.0.0.1:3307`。 | 通过；README 已更新 Navicat、环境变量和默认连接说明。 | 通过 |

## 回归检查

- `[x]` 未停止或删除已有 MySQL 容器。
- `[x]` 未修改 Flyway 建表脚本。
- `[x]` 未执行 git commit 或 push。

## 未解决测试风险

- 本次不启动容器，因此不验证容器实际运行。
- Docker CLI 仍提示 `C:\Users\wcf26\.docker\config.json` 访问被拒绝，但 Compose 配置解析成功。
