# Implementation Plan: 今日训练饮食聚合接口

**Branch**: `002-today-summary-api` | **Date**: 2026-07-21 | **Spec**: specs/002-today-summary-api/spec.md

**Input**: Feature specification from `specs/002-today-summary-api/spec.md`

## Summary

新增今日页聚合接口，按用户和日期返回训练汇总、饮食汇总和净热量。实现上新增 today 模块，
通过 JDBC 聚合既有 `workout_record`、`diet_record`、`diet_food_entry` 数据，不新增数据库表。

## Technical Context

**Language/Version**: Java 17

**Primary Dependencies**: Spring Boot Web、Spring Validation、Spring JDBC

**Storage**: MySQL，测试环境使用 H2 MySQL 模式；复用 Flyway 已创建表

**Testing**: JUnit 5、Spring Boot Test、MockMvc

**Target Platform**: 本地 Java 后端服务，供微信小程序今日页调用

**Project Type**: 移动端小程序 + 后端 API

**Performance Goals**: 单日聚合在本地开发数据量下快速返回，满足今日页首屏展示。

**Constraints**: 不新增数据库表；不引入缓存；继续使用 `X-User-Id`；必须测试先行。

**Scale/Scope**: MVP 阶段按单用户单日聚合，后续可扩展为周/月统计。

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- 中文优先：通过，所有规格和任务使用中文。
- Git 提交和推送由用户确认：通过，本轮不自动 commit/push。
- 测试先行：通过，先写 `TodaySummaryControllerTest`。
- 答疑排错不创建功能规格：通过，本次是明确功能开发。
- 技术栈边界：通过，使用既有 Java、Spring Boot、MySQL/Flyway。

## Project Structure

### Documentation (this feature)

```text
specs/002-today-summary-api/
├── spec.md
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── today-summary-api.md
└── tasks.md
```

### Source Code (repository root)

```text
backend/
├── src/main/java/com/example/fitness/today/
│   ├── api/
│   ├── application/
│   └── infrastructure/
└── src/test/java/com/example/fitness/today/
```

**Structure Decision**: 新增 today 聚合模块，不把聚合逻辑塞进 workout 或 diet 模块，避免两个业务模块互相依赖。

## Complexity Tracking

无宪章违规项。
