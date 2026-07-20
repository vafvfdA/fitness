# Implementation Plan: 训练模板接口

**Branch**: `003-workout-template-api` | **Date**: 2026-07-21 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `specs/003-workout-template-api/spec.md`

## Summary

实现训练模板后端接口，让用户可以创建训练模板、查询模板列表/详情，并从模板生成训练记录。技术上沿用现有 Spring Boot 分层：Controller 接收请求，Service 编排业务规则，Repository 使用 Spring JDBC 访问 MySQL/Flyway 表；生成训练记录时复用已有 `workout_record`、`workout_exercise_entry`、`workout_set_entry` 表结构。现有 `workout_template_item` 缺少动作预估热量字段，本轮通过 Flyway V2 增加 `estimated_calories`。

## Technical Context

**Language/Version**: Java 17

**Primary Dependencies**: Spring Boot 3.3.7、Spring Web、Spring JDBC、Jakarta Validation、Flyway

**Storage**: MySQL，测试环境使用 H2 MySQL mode

**Testing**: JUnit 5、Spring Boot Test、MockMvc

**Target Platform**: 后端 Web 服务，供微信小程序调用

**Project Type**: 移动端前端 + 后端 API，本轮只改后端

**Performance Goals**: 单用户模板列表和详情查询在常规数据量下即时返回；本轮以接口正确性和可联调性优先

**Constraints**: 不引入 ORM；不新增后端框架；数据库结构变更必须走 Flyway；不修改已有训练记录响应契约；提交和推送需用户确认

**Scale/Scope**: 支撑个人使用和开发联调；模板数量按单用户几十到几百条设计

## Constitution Check

- **中文优先**: 规格、计划、任务和最终说明使用中文，接口字段保留英文契约。通过。
- **Git 提交和推送由用户确认**: 本轮开发不主动 commit/push。通过。
- **测试先行**: 先写 `WorkoutTemplateControllerTest` 并确认 404/失败，再实现生产代码。通过。
- **答疑排错不创建功能规格**: 本轮是明确功能开发，创建 `specs/003-workout-template-api` 合规。通过。
- **技术栈边界**: 使用 Spring Boot + MySQL + Flyway，不引入新框架。通过。

## Project Structure

### Documentation

```text
specs/003-workout-template-api/
├── spec.md
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── workout-template-api.md
├── checklists/
│   └── requirements.md
└── tasks.md
```

### Source Code

```text
backend/src/main/java/com/example/fitness/workout/template/
├── api/
├── application/
└── infrastructure/

backend/src/main/resources/db/migration/
└── V2__add_workout_template_item_calories.sql

backend/src/test/java/com/example/fitness/workout/template/
└── WorkoutTemplateControllerTest.java
```

**Structure Decision**: 使用 `workout/template` 子包承载模板能力，避免与已有训练记录 API 混在同一包内，同时保持 Controller、Service、Repository 三层结构。

## Complexity Tracking

无宪章违规项。
