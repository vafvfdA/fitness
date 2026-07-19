---
name: project-sdd
description: 在本项目中处理产品澄清、需求拆解、实现计划、测试验收、代码评审、提交或推送准备时使用。
---

# 项目 SDD

本 skill 适用于本项目的产品功能开发、系统行为变更、测试验收、评审和提交推送准备。项目事实来源如下：

- `sdd/constitution.md`：不可违反的项目红线。
- `sdd/reference/prd.md`：产品级需求来源。
- `sdd/reference/architecture.md`：架构级约束来源。
- `sdd/workflow.md`：SDD 各阶段定义。
- `sdd/changes/<change-id>/`：每个功能或变更的独立工作目录。

## 语言规则

本项目中，AI 的回答和所有 Markdown 文档的描述内容默认使用中文。只有代码标识、接口路径、表名、字段名、命令、依赖名、错误码等技术契约可以保留英文。

## 核心规则

只有在用户明确要求开发功能、修改业务行为、调整数据模型、调整 API 契约或修复影响代码行为的缺陷时，才创建 `sdd/changes/<change-id>/` 并进入完整 SDD。

不要在当前功能变更至少具备以下文件前开始实现：

1. `spec.md`
2. `task.md`
3. `test.md`
4. `review.md`

如果只是答疑、排错、命令解释、环境连接说明、面试问题讨论，或者极小的文档和流程规则修正，不创建 change 目录；直接在回复中说明即可。

## SDD 方法

| 方法 | 产物 | 必做动作 |
| --- | --- | --- |
| `sdd-understand` | `spec.md` | 将 PRD、架构文档和用户请求转成明确行为、边界和验收标准。 |
| `sdd-develop` | 代码变更和任务状态 | 遵守红绿重构。除非用户明确批准例外，否则生产代码前必须先写失败测试。 |
| `sdd-test` | `test.md` 结果 | 执行自动化测试和人工验收，记录命令、证据和失败项。 |
| `sdd-review` | `review.md` 结论 | 在声明完成前评审正确性、架构适配、代码质量、安全性和测试缺口。 |
| `sdd-commit-push` | 提交和推送方案 | 只准备变更范围和提交信息；没有用户明确批准，不得提交或推送。 |

## Change 目录契约

功能开发时，在 `sdd/changes/` 下创建变更目录，命名格式为：

```text
NNNN-short-kebab-name
```

示例：

```text
sdd/changes/0002-add-workout-entry/
```

每个变更目录必须包含：

- `spec.md`：说明要做什么、不做什么。
- `task.md`：按顺序拆解可执行任务，并记录状态。
- `test.md`：记录验收测试、回归测试和实际结果。
- `review.md`：记录评审发现和最终结论。

以下场景不得为了“留痕”而单独创建 change：

- Navicat 连接失败、Docker 端口冲突、数据库账号密码填写等本地环境答疑。
- 对已有表设计、代码结构、提交命令、验收步骤的解释。
- 不改变代码行为的极小文档修正。

## 必走流程

1. 阅读 `constitution.md`。
2. 阅读 `reference/prd.md` 和 `reference/architecture.md` 中相关部分。
3. 判断当前请求是否需要 change；仅在功能开发或行为变更时创建或更新当前 change 目录。
4. 执行 `sdd-understand`。
5. 只有当 `spec.md` 和 `task.md` 明确后，才执行 `sdd-develop`。
6. 执行 `sdd-test`。
7. 执行 `sdd-review`。
8. 只有当用户要求准备提交或推送时，才执行 `sdd-commit-push`。

## 红线信号

出现以下情况时必须停止并修正文档、实现或向用户确认：

- 没有 change 目录就实现功能。
- 因为“看起来简单”而跳过测试。
- 代码变更和 `prd.md` 或 `architecture.md` 冲突。
- 未经用户批准就准备执行 commit 或 push。
- AI 为了推进任务修改无关文件。
- 将热量、训练或饮食建议包装成医疗保证。
