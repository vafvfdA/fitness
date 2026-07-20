# Implementation Plan: 小程序记录 MVP 联调

**Branch**: `004-miniapp-record-mvp` | **Date**: 2026-07-21 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `specs/004-miniapp-record-mvp/spec.md`

## Summary

在现有原生微信小程序骨架上实现记录 MVP：今日页调用今日汇总接口，训练页调用训练记录和训练模板接口，饮食页调用饮食记录接口。新增前端请求封装默认用户 ID、页面数据映射工具和 Node 级映射测试。后端不新增接口，复用当前已完成接口。

## Technical Context

**Language/Version**: JavaScript、微信小程序原生 WXML/WXSS

**Primary Dependencies**: 微信小程序运行时、现有 Spring Boot 后端接口

**Storage**: 小程序端不做本地持久化；数据来自后端 MySQL

**Testing**: Node.js 内置 `assert` 覆盖页面数据映射；后端继续用 Maven 测试保证接口可用

**Target Platform**: 微信开发者工具和微信小程序移动端

**Project Type**: 移动端前端 + 后端 API，本轮主要改 `miniapp`

**Performance Goals**: 页面加载时发起少量接口请求，常规个人数据下即时渲染

**Constraints**: 不引入 npm 依赖；不引入跨端框架；开发期固定 `X-User-Id: 1001`；Git 提交推送需用户确认

**Scale/Scope**: MVP 联调范围，只覆盖今日、训练、饮食三个核心页面

## Constitution Check

- **中文优先**: 文档、页面文案和最终说明使用中文，接口字段保留英文。通过。
- **Git 提交和推送由用户确认**: 本轮不主动提交推送。通过。
- **测试先行**: 先写页面数据映射测试并确认失败，再实现工具和页面。通过。
- **答疑排错不创建功能规格**: 本轮是明确功能开发，创建 `specs/004-miniapp-record-mvp` 合规。通过。
- **技术栈边界**: 使用微信小程序前端和现有 Java 后端，不引入新框架。通过。

## Project Structure

### Documentation

```text
specs/004-miniapp-record-mvp/
├── spec.md
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── miniapp-record-flow.md
├── checklists/
│   └── requirements.md
└── tasks.md
```

### Source Code

```text
miniapp/
├── app.js
├── app.wxss
├── utils/
│   ├── request.js
│   └── view-model.js
├── tests/
│   └── view-model.test.js
└── pages/
    ├── today/
    ├── workout/
    └── diet/
```

**Structure Decision**: 保持原生小程序结构，在 `utils` 中沉淀请求和数据映射，页面只负责调用接口、维护表单状态和渲染。

## Complexity Tracking

无宪章违规项。
