# Implementation Plan: 饮食记录后端接口

**Branch**: `001-diet-record-api` | **Date**: 2026-07-20 | **Spec**: specs/001-diet-record-api/spec.md

**Input**: Feature specification from `specs/001-diet-record-api/spec.md`

## Summary

实现饮食记录后端最小闭环：新增饮食记录、按日期查询饮食记录、按日期查询饮食汇总。技术上复用既有
Spring Boot 后端、MySQL/Flyway 表结构和统一响应模型，新增 diet 模块的 DTO、Controller、Service、
Repository 和集成测试。

## Technical Context

**Language/Version**: Java 17

**Primary Dependencies**: Spring Boot Web、Spring Validation、Spring JDBC

**Storage**: MySQL，测试环境使用 H2 MySQL 模式；表结构由 Flyway 管理

**Testing**: JUnit 5、Spring Boot Test、MockMvc

**Target Platform**: 本地 Java 后端服务，供微信小程序调用

**Project Type**: 移动端小程序 + 后端 API

**Performance Goals**: 单日饮食记录查询和汇总在本地开发数据量下应快速返回，满足小程序页面交互。

**Constraints**: 不引入 ORM；不修改既有表结构；开发期继续使用 `X-User-Id`；必须测试先行。

**Scale/Scope**: MVP 阶段面向单用户日常记录，接口设计保留用户级数据隔离。

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- 中文优先：通过，规格、计划和任务均使用中文。
- Git 提交和推送由用户确认：通过，本变更不自动 commit/push。
- 测试先行：通过，任务中先写失败测试，再实现生产代码。
- 答疑排错不创建功能规格：通过，本次是明确功能开发，创建 spec 合理。
- 技术栈边界：通过，继续使用 Java、Spring Boot、MySQL、Flyway 和微信小程序架构。

## Project Structure

### Documentation (this feature)

```text
specs/001-diet-record-api/
├── spec.md
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── diet-record-api.md
└── tasks.md
```

### Source Code (repository root)

```text
backend/
├── src/main/java/com/example/fitness/diet/
│   ├── api/
│   ├── application/
│   └── infrastructure/
└── src/test/java/com/example/fitness/diet/
```

**Structure Decision**: 本功能只新增后端 API，不修改小程序页面。后端沿用训练记录接口已采用的
Controller、Service、Repository 分层。

## Complexity Tracking

无宪章违规项。
