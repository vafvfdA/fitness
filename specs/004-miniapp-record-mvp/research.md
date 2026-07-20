# Research: 小程序记录 MVP 联调

## Decision: 不引入 npm 依赖

**Rationale**: 当前小程序是原生结构，MVP 联调不需要状态管理、UI 库或构建工具。保持轻量能减少微信开发者工具配置问题。

**Alternatives considered**:

- 引入 Vant Weapp：能更快获得组件，但会增加 npm 构建和样式依赖。
- 使用跨端框架：与项目宪章的微信小程序前端边界不一致。

## Decision: 请求工具统一携带开发期用户 ID

**Rationale**: 后端当前用 `X-User-Id` 做临时用户隔离。前端统一封装可以避免每个页面重复写请求头。

**Alternatives considered**:

- 每个页面手写请求头：容易遗漏，后续替换登录也更麻烦。

## Decision: 使用 Node 测试覆盖页面数据映射

**Rationale**: 微信小程序页面运行依赖 `wx` 环境，当前没有小程序自动化测试框架。把可测试逻辑提取到纯函数，用 Node 内置 `assert` 覆盖，是当前成本最低的测试先行方案。

**Alternatives considered**:

- 只做手动测试：不符合测试先行要求。
- 引入小程序自动化测试框架：配置成本高于本轮 MVP 收益。

## Decision: 保留页面级表单，不做复杂组件抽象

**Rationale**: 训练页和饮食页当前交互不同，MVP 阶段先让页面闭环稳定，再根据重复情况抽组件。

**Alternatives considered**:

- 预先抽通用表单组件：当前字段变化可能较大，过早抽象会增加修改成本。
