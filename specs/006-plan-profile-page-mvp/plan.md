# Implementation Plan: 计划与个人资料页面联调

**Branch**: `006-plan-profile-page-mvp` | **Date**: 2026-07-21 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `specs/006-plan-profile-page-mvp/spec.md`

## Summary

实现小程序计划与个人资料页面联调。后端新增 `ProfileController`（含体重历史事务双写）、`BodyMetricController`（历史查询）、`TrainingPlanController`（含今日计划计算），前端联调 `profile` 和 `plan` 页，新增 `view-model` 纯函数覆盖差值计算和周期今日计划。遵循红绿重构，领域计算先行单测。

## Technical Context

**Language/Version**: Java 21 + Spring Boot 3.x（后端）；JavaScript、微信小程序原生 WXML/WXSS（前端）

**Primary Dependencies**: Spring Web MVC、Spring Validation、MyBatis-Plus、MySQL 8、Flyway（已有）；微信小程序运行时

**Storage**: MySQL（`user_profile`、`body_metric_record`、`training_plan` 三张已有表，本轮不加迁移）

**Testing**: JUnit 5 + AssertJ（后端单元/API 测试）；Node.js 内置 `assert`（前端映射测试）

**Target Platform**: 微信开发者工具和微信小程序移动端；Spring Boot 后端

**Project Type**: 全栈，本轮新增后端 3 个模块 + 前端 2 个页面联调

**Performance Goals**: profile/plan 页各 1 次接口请求完成首屏；今日计划接口纯计算无额外 IO

**Constraints**: 不引入新框架或依赖；不新增数据库迁移；Git 提交推送需用户确认；不实现提醒推送触发

**Scale/Scope**: 单用户，个人资料与计划管理

## Constitution Check

- **中文优先**: 文档、页面文案和最终说明使用中文。通过。
- **PRD 和架构文档是源参考**: 本轮引用 `sdd/reference/prd.md` 5.5 计划页面和 `architecture.md` 训练计划表。通过。
- **先理解再实现**: spec.md 定义范围、流程、数据、API、验收标准后再开发。通过。
- **红绿重构**: 领域计算（差值、周期）和 API 先写失败测试再实现。通过。
- **测试和评审独立关口**: test.md 记录测试，review.md 记录评审。通过。
- **Git 操作经用户审核**: 不主动提交推送。通过。
- **控制范围**: 不做提醒推送、不做曲线图、不引入新依赖。通过。
- **健康安全**: 体重差不描述为医疗诊断，仅作参考。通过。
- **隐私安全**: 所有查询按 `X-User-Id` 过滤，不信任请求体 user_id。通过。

## Project Structure

### Documentation

```text
specs/006-plan-profile-page-mvp/
├── spec.md
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── profile-plan-flow.md
├── checklists/
│   └── requirements.md
└── tasks.md
```

### Source Code

后端新增（遵循现有 `api → application → infrastructure` 分层）：

```text
backend/src/main/java/com/example/fitness/
├── profile/
│   ├── api/
│   │   ├── ProfileController.java
│   │   ├── ProfileResponse.java
│   │   └── UpdateProfileRequest.java
│   ├── application/
│   │   └── ProfileService.java
│   └── infrastructure/
│       └── ProfileRepository.java
├── bodymetric/
│   ├── api/
│   │   ├── BodyMetricController.java
│   │   └── BodyMetricResponse.java
│   ├── application/
│   │   └── BodyMetricService.java
│   └── infrastructure/
│       └── BodyMetricRepository.java
└── plan/
    ├── api/
    │   ├── TrainingPlanController.java
    │   ├── TrainingPlanResponse.java
    │   ├── UpdateTrainingPlanRequest.java
    │   └── TodayPlanResponse.java
    ├── application/
    │   ├── TrainingPlanService.java
    │   └── TrainingCycleCalculator.java   // 纯函数领域计算
    └── infrastructure/
        └── TrainingPlanRepository.java
```

后端测试新增：

```text
backend/src/test/java/com/example/fitness/
├── profile/ProfileControllerTest.java
├── bodymetric/BodyMetricControllerTest.java
└── plan/
    ├── TrainingCycleCalculatorTest.java   // 领域计算单测
    └── TrainingPlanControllerTest.java
```

前端改动：

```text
miniapp/
├── utils/view-model.js          // 新增 mapProfile、computeTodayPlan
├── tests/view-model.test.js     // 新增对应测试
└── pages/
    ├── profile/
    │   ├── index.js
    │   ├── index.wxml
    │   └── index.wxss
    └── plan/
        ├── index.js
        ├── index.wxml
        └── index.wxss
```

**Structure Decision**: 周期计算抽成 `TrainingCycleCalculator` 纯函数类，无 Spring 依赖，便于单测；profile 与 body_metric 分属不同模块但 ProfileService 在事务内调用 BodyMetricRepository 完成双写。

## Complexity Tracking

- `body_metric_record` 接入是本轮新增面，但表已存在（V1 迁移），无 schema 变更。
- 周期计算是本轮最易出错的逻辑，用纯函数 + 边界用例（未来日期、空轮换、跨多周期）覆盖。
