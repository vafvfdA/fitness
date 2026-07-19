# 0002 项目骨架规格

## 背景

本变更基于 `sdd/reference/prd.md` 和 `sdd/reference/architecture.md`，为后续 MVP 功能开发创建可维护的项目骨架。

当前目标不是实现训练、饮食、日历、计划的完整业务，而是先建立目录结构、基础依赖、运行入口、模块边界和最小健康检查。

## 目标

创建一个包含 Java 后端和微信小程序前端的双端项目骨架，使后续功能可以按 SDD change 逐步开发。

## 范围内

- 创建后端 `backend/` Maven 工程。
- 使用 Java 17 和 Spring Boot 3.x。
- 建立后端基础包结构：`common`、`user`、`profile`、`workout`、`diet`、`calendar`、`plan`、`knowledge`、`ai`。
- 添加统一响应模型、基础健康检查接口和应用启动类。
- 创建基础后端测试，验证应用上下文和健康检查。
- 创建微信小程序 `miniapp/` 骨架。
- 建立小程序页面：今日、训练、饮食、日历、计划、知识库、我的。
- 添加小程序基础配置、页面样式和接口客户端占位。
- 添加根目录说明文档。

## 范围外

- 不实现真实微信登录。
- 不实现数据库表迁移。
- 不实现训练、饮食、日历、计划 CRUD。
- 不接入 MySQL、Redis 或 AI 服务。
- 不实现知识库流式问答。
- 不做 git commit 或 push。

## 设计约束

- Markdown 描述使用中文。
- 代码中的包名、类名、接口路径、字段名使用英文技术标识。
- 后端骨架应能独立通过 Maven 测试。
- 小程序骨架应能被微信开发者工具识别为普通小程序项目。
- 不引入非必要复杂依赖。

## 功能需求

| 编号 | 需求 | 优先级 |
| --- | --- | --- |
| FR-1 | 根目录包含后端和小程序两个明确子项目。 | 必须 |
| FR-2 | 后端提供 Spring Boot 启动类。 | 必须 |
| FR-3 | 后端提供统一响应模型。 | 必须 |
| FR-4 | 后端提供 `/api/v1/health` 健康检查接口。 | 必须 |
| FR-5 | 后端创建与架构文档一致的模块包结构。 | 必须 |
| FR-6 | 后端包含可运行的基础测试。 | 必须 |
| FR-7 | 小程序包含核心页面和 tabBar。 | 必须 |
| FR-8 | 小程序包含 API 客户端占位，方便后续接后端。 | 应该 |
| FR-9 | 根目录说明如何运行后端和打开小程序。 | 必须 |

## 数据影响

本变更不创建数据库表，不修改数据模型。

## API 影响

新增健康检查接口：

- `GET /api/v1/health`

预期响应：

```json
{
  "code": "OK",
  "message": "success",
  "data": {
    "status": "UP",
    "service": "fitness-backend"
  }
}
```

## UI 影响

小程序创建以下页面骨架：

- `pages/today/index`
- `pages/workout/index`
- `pages/diet/index`
- `pages/calendar/index`
- `pages/plan/index`
- `pages/knowledge/index`
- `pages/profile/index`

页面只展示最小结构和入口文案，不实现复杂交互。

## 验收标准

| 编号 | 标准 |
| --- | --- |
| AC-1 | `backend` 目录包含可识别的 Maven/Spring Boot 工程。 |
| AC-2 | `mvn test` 在后端目录可以执行并通过。 |
| AC-3 | 后端测试覆盖应用上下文和健康检查接口。 |
| AC-4 | 后端包结构和架构文档一致。 |
| AC-5 | `miniapp` 目录包含 `app.json`、`app.js`、`app.wxss` 和核心页面。 |
| AC-6 | 小程序 `tabBar` 至少包含今日、日历、计划、我的。 |
| AC-7 | 根目录 `README.md` 说明项目结构和启动方式。 |

## 风险

- 首次运行 Maven 可能需要下载依赖，受本机网络或 Maven 仓库影响。
- 小程序需要通过微信开发者工具进一步验证。
- 当前只是骨架，业务功能仍需后续 change 实现。

## 开放问题

- 后续是否使用真实微信登录作为第一个业务功能。
- 后续是否先做训练记录，还是先做数据库迁移。

