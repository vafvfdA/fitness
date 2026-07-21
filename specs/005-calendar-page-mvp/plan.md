# Implementation Plan: 训练日历页面联调

**Branch**: `005-calendar-page-mvp` | **Date**: 2026-07-21 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `specs/005-calendar-page-mvp/spec.md`

## Summary

实现小程序训练日历页联调。前端复用现有 `request` 请求封装，调用后端 `GET /workout-records/calendar?month=yyyy-MM`，将返回的训练日摘要映射为稳定月历格子。新增纯函数测试覆盖月份格式化、跨月切换和日历格子构造。

## Technical Context

**Language/Version**: JavaScript、微信小程序原生 WXML/WXSS

**Primary Dependencies**: 微信小程序运行时、现有 Spring Boot 后端接口

**Storage**: 小程序端不做本地持久化

**Testing**: Node.js 内置 `assert` 覆盖日历映射；后端继续用 Maven 测试保证接口可用

**Target Platform**: 微信开发者工具和微信小程序移动端

**Project Type**: 移动端前端 + 后端 API，本轮主要改 `miniapp/pages/calendar` 和 `miniapp/utils/view-model.js`

**Performance Goals**: 单月日历一次接口请求完成渲染

**Constraints**: 不引入 npm 依赖；不新增后端接口；Git 提交推送需用户确认

**Scale/Scope**: 单用户月视图，展示训练摘要和日期导航

## Constitution Check

- **中文优先**: 文档、页面文案和最终说明使用中文。通过。
- **Git 提交和推送由用户确认**: 本轮开发不主动提交推送。通过。
- **测试先行**: 先写日历映射失败测试，再实现映射和页面。通过。
- **答疑排错不创建功能规格**: 本轮是明确功能开发，创建 `specs/005-calendar-page-mvp` 合规。通过。
- **技术栈边界**: 使用微信小程序前端和现有 Java 后端，不引入新框架。通过。

## Project Structure

### Documentation

```text
specs/005-calendar-page-mvp/
├── spec.md
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── calendar-page-flow.md
├── checklists/
│   └── requirements.md
└── tasks.md
```

### Source Code

```text
miniapp/
├── utils/
│   └── view-model.js
├── tests/
│   └── view-model.test.js
└── pages/
    └── calendar/
        ├── index.js
        ├── index.wxml
        └── index.wxss
```

**Structure Decision**: 日历计算放在 `view-model.js`，页面只负责请求、状态和跳转。

## Complexity Tracking

无宪章违规项。
