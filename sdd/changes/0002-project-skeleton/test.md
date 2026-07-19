# 0002 项目骨架测试计划

## 测试范围

本测试计划验证项目骨架是否可以作为后续开发基础。

## 自动化测试

| 编号 | 命令或测试 | 预期结果 | 实际结果 | 状态 |
| --- | --- | --- | --- | --- |
| T-1 | `mvn test` | 后端测试全部通过。 | 通过；共 2 个测试，失败 0，错误 0，跳过 0。 | 通过 |
| T-2 | `ApplicationContext` 测试 | Spring Boot 上下文可以启动。 | 通过；`FitnessApplicationTests.contextLoads` 成功。 | 通过 |
| T-3 | 健康检查 controller 测试 | `/api/v1/health` 返回 `OK` 和 `UP`。 | 通过；`HealthControllerTest.returnsHealthStatus` 成功。 | 通过 |
| T-4 | 小程序 JSON 解析检查 | `miniapp` 下 JSON 文件均可解析。 | 通过；`app.json`、`project.config.json`、`sitemap.json` 和页面 JSON 均可解析。 | 通过 |

## 人工验收测试

| 编号 | 场景 | 步骤 | 预期结果 | 实际结果 | 状态 |
| --- | --- | --- | --- | --- | --- |
| M-1 | 检查后端结构 | 查看 `backend/src/main/java`。 | 包结构符合架构文档。 | 通过；已创建 common、user、profile、workout、diet、calendar、plan、knowledge、ai 等包。 | 通过 |
| M-2 | 检查小程序结构 | 查看 `miniapp/app.json` 和 `miniapp/pages`。 | 核心页面和 tabBar 存在。 | 通过；今日、训练、饮食、日历、计划、知识库、我的页面存在，tabBar 包含今日、日历、计划、我的。 | 通过 |
| M-3 | 检查根目录说明 | 查看 `README.md`。 | 包含项目结构和运行方式。 | 通过；已说明后端测试、启动方式和小程序打开方式。 | 通过 |

## 回归检查

- `[x]` 未修改 `sdd/reference/prd.md`。
- `[x]` 未修改 `sdd/reference/architecture.md`。
- `[x]` 未执行 git commit 或 push。

## 红绿记录

- 红灯：先创建后端测试后运行 `mvn test`，编译失败，原因是缺少 `HealthController`。
- 绿灯：补充启动类、统一响应和健康检查后，`mvn test` 通过。

## 未解决测试风险

- 小程序尚未用微信开发者工具实际打开验证。
- 普通沙箱内 Maven 写入 `target` 曾被拒绝，最终使用经批准的 Maven 测试权限完成验证。
