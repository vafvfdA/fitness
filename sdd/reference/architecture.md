# 健身饮食应用架构文档

## 1. 架构目标

本项目既要作为个人工具可用，也要作为 Java 架构练习项目可持续演进。

架构优先级：

1. 领域边界清晰。
2. 业务逻辑可测试。
3. 先简单可用，再逐步扩展。
4. API 设计适合移动端。
5. 对个人健身和饮食数据保持隐私意识。
6. 为后续知识库和流式问答保留扩展点，但不把复杂度提前塞进 MVP。

## 2. 推荐技术栈

### 前端

- 微信小程序。
- 如果工程方案支持，优先使用 TypeScript。
- 页面按训练、饮食、日历、计划、知识库拆分。
- 本地页面状态配合后端 API 持久化。

可选 UI 库：

- 如果需要更快交付移动端表单和日历组件，可以使用 Vant Weapp。
- 如果更重视学习微信小程序基础能力，可以使用原生组件。

建议：

- 先使用原生微信小程序和少量内部组件。
- 只有当表单、日历等 UI 开发成本明显影响进度时，再引入 Vant Weapp。

### 后端

- Java 21 或 Java 17。
- Spring Boot 3.x。
- Spring Web MVC。
- Spring Validation。
- Spring Security 负责认证边界。
- MyBatis-Plus 或 Spring Data JPA。

建议：

- 使用 Spring Boot 3.x + MyBatis-Plus，兼顾 CRUD 效率和 SQL 可控性。
- 领域服务尽量不要直接依赖持久化细节。

### 数据库

- MySQL 8。
- Flyway 管理数据库迁移。
- Redis 可选，用于缓存、限流、验证码或临时会话。

### 知识库和 AI

第一阶段：

- 知识条目存 MySQL。
- 使用关键词搜索或简单全文检索。

第二阶段：

- 增加 embedding 和向量检索。
- 可选方案包括 PostgreSQL + pgvector、Milvus、Elasticsearch 向量检索或托管向量库。

建议：

- 知识写入和检索通过接口隔离。
- 先用 MySQL 关键词搜索。
- 后续增加向量检索时，不改变问答 API 契约。

### 流式输出

后端选择：

- 优先 Server-Sent Events。
- 只有需要双向实时交互时才考虑 WebSocket。

小程序约束：

- 微信小程序对流式能力的支持会受到请求 API 和网关行为影响。
- 如果原生流式不稳定，需要提供轮询或分片消息降级方案。

建议：

- 后端提供一个 SSE 接口和一个非流式兜底接口。
- 小程序端根据平台验证结果选择可靠方案。

## 3. 后端分层

推荐包结构：

```text
com.example.fitness
  common
    error
    response
    security
    validation
  user
  profile
  workout
  diet
  calendar
  plan
  knowledge
  ai
```

每个业务模块内部建议分层：

```text
controller
application
domain
infrastructure
```

职责：

- `controller`：HTTP 请求响应、参数校验、认证用户提取。
- `application`：用例编排和事务边界。
- `domain`：领域模型、规则和计算。
- `infrastructure`：mapper、repository、外部客户端、持久化转换。

## 4. 核心领域模块

### 用户和个人资料

职责：

- 微信 OpenID 绑定。
- 用户基础资料。
- 单位偏好。

### 身体指标

职责：

- 记录当前体重和历史体重。
- 保存目标体重。
- 计算距离目标还差多少。

### 训练

职责：

- 每日训练记录。
- 训练动作条目。
- 组记录。
- 训练模板。
- 预估消耗热量。

### 饮食

职责：

- 每日饮食记录。
- 餐次食物条目。
- 食物模板。
- 预估摄入热量。

### 日历

职责：

- 按日期聚合训练和饮食摘要。
- 提供紧凑的月份视图。

### 计划

职责：

- 训练周期，例如练三休一。
- 部位轮换。
- 饮食热量目标。
- 提醒配置。

### 知识库和 AI

职责：

- 知识条目 CRUD。
- 检索。
- 问答编排。
- 流式响应。
- 健身和饮食建议的安全约束。

## 5. 建议数据表

命名约定：

- 表名和字段名使用 `snake_case`。
- 常规表包含 `id`、`user_id`、`created_at`、`updated_at`。
- 逻辑删除只在确有必要的表使用，不默认铺满所有表。

### user_account

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | bigint | 主键 |
| openid | varchar(128) | 微信 OpenID，唯一 |
| nickname | varchar(64) | 可选 |
| avatar_url | varchar(512) | 可选 |
| status | tinyint | 正常或禁用 |
| created_at | datetime | 创建时间 |
| updated_at | datetime | 更新时间 |

### user_profile

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | bigint | 主键 |
| user_id | bigint | 用户 ID，唯一 |
| gender | varchar(16) | 可选 |
| height_cm | decimal(5,2) | 可选 |
| birthday | date | 可选 |
| current_weight_kg | decimal(5,2) | 当前体重 |
| target_weight_kg | decimal(5,2) | 目标体重 |
| daily_calorie_target | int | 每日热量目标 |
| created_at | datetime | 创建时间 |
| updated_at | datetime | 更新时间 |

### body_metric_record

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | bigint | 主键 |
| user_id | bigint | 所属用户 |
| record_date | date | 记录日期 |
| weight_kg | decimal(5,2) | 体重 |
| body_fat_percent | decimal(5,2) | 体脂率，可选 |
| waist_cm | decimal(5,2) | 腰围，可选 |
| note | varchar(512) | 备注 |
| created_at | datetime | 创建时间 |
| updated_at | datetime | 更新时间 |

### workout_record

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | bigint | 主键 |
| user_id | bigint | 所属用户 |
| workout_date | date | 训练日期 |
| title | varchar(128) | 标题，可选 |
| body_part | varchar(32) | 胸、肩、背、腿、臂、核心、有氧、其他 |
| total_sets | int | 汇总组数 |
| estimated_calories | int | 预估消耗热量 |
| note | varchar(512) | 备注 |
| created_at | datetime | 创建时间 |
| updated_at | datetime | 更新时间 |

### workout_exercise_entry

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | bigint | 主键 |
| workout_record_id | bigint | 所属训练记录 |
| user_id | bigint | 所属用户，用于隔离 |
| exercise_name | varchar(128) | 动作名称 |
| body_part | varchar(32) | 部位 |
| sort_order | int | 展示顺序 |
| estimated_calories | int | 预估消耗热量 |
| note | varchar(512) | 备注 |
| created_at | datetime | 创建时间 |
| updated_at | datetime | 更新时间 |

### workout_set_entry

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | bigint | 主键 |
| exercise_entry_id | bigint | 所属动作 |
| user_id | bigint | 所属用户，用于隔离 |
| set_no | int | 第几组，从 1 开始 |
| reps | int | 次数 |
| weight_kg | decimal(6,2) | 重量 |
| duration_seconds | int | 时长 |
| distance_meters | int | 距离 |
| created_at | datetime | 创建时间 |
| updated_at | datetime | 更新时间 |

### workout_template

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | bigint | 主键 |
| user_id | bigint | 所属用户，系统模板可为空 |
| name | varchar(128) | 模板名称 |
| body_part | varchar(32) | 主要部位 |
| description | varchar(512) | 描述 |
| is_system | boolean | 是否系统模板 |
| created_at | datetime | 创建时间 |
| updated_at | datetime | 更新时间 |

### workout_template_item

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | bigint | 主键 |
| template_id | bigint | 所属模板 |
| exercise_name | varchar(128) | 动作名称 |
| default_sets | int | 默认组数 |
| default_reps | int | 默认次数 |
| default_weight_kg | decimal(6,2) | 默认重量 |
| default_duration_seconds | int | 默认时长 |
| sort_order | int | 展示顺序 |

### diet_record

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | bigint | 主键 |
| user_id | bigint | 所属用户 |
| diet_date | date | 饮食日期 |
| total_calories | int | 摄入总热量 |
| note | varchar(512) | 备注 |
| created_at | datetime | 创建时间 |
| updated_at | datetime | 更新时间 |

### diet_food_entry

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | bigint | 主键 |
| diet_record_id | bigint | 所属饮食记录 |
| user_id | bigint | 所属用户 |
| meal_type | varchar(32) | 早餐、午餐、晚餐、加餐 |
| food_name | varchar(128) | 食物名称 |
| amount | decimal(8,2) | 数量 |
| unit | varchar(32) | 单位，例如克、份、碗 |
| calories | int | 预估热量 |
| protein_g | decimal(8,2) | 蛋白质，可选 |
| fat_g | decimal(8,2) | 脂肪，可选 |
| carb_g | decimal(8,2) | 碳水，可选 |
| sort_order | int | 展示顺序 |
| created_at | datetime | 创建时间 |
| updated_at | datetime | 更新时间 |

### food_template

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | bigint | 主键 |
| user_id | bigint | 所属用户，系统模板可为空 |
| food_name | varchar(128) | 食物名称 |
| default_unit | varchar(32) | 默认单位 |
| calories_per_unit | decimal(8,2) | 单位热量 |
| protein_per_unit | decimal(8,2) | 单位蛋白质 |
| fat_per_unit | decimal(8,2) | 单位脂肪 |
| carb_per_unit | decimal(8,2) | 单位碳水 |
| is_system | boolean | 是否系统模板 |
| created_at | datetime | 创建时间 |
| updated_at | datetime | 更新时间 |

### training_plan

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | bigint | 主键 |
| user_id | bigint | 所属用户 |
| name | varchar(128) | 计划名称 |
| cycle_type | varchar(32) | 训练休息轮换或自定义 |
| train_days | int | 训练天数，例如 3 |
| rest_days | int | 休息天数，例如 1 |
| start_date | date | 周期起始日期 |
| muscle_rotation_json | json | 部位轮换，例如胸、肩、背 |
| daily_calorie_target | int | 每日热量目标 |
| reminder_enabled | boolean | 是否开启提醒 |
| reminder_time | time | 提醒时间 |
| created_at | datetime | 创建时间 |
| updated_at | datetime | 更新时间 |

### knowledge_item

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | bigint | 主键 |
| user_id | bigint | 所属用户，系统知识可为空 |
| title | varchar(128) | 标题 |
| category | varchar(32) | 训练、食物、营养、计划、风险提示、其他 |
| content | text | 知识内容 |
| tags | varchar(512) | 标签 |
| source | varchar(512) | 来源 |
| is_system | boolean | 是否系统知识 |
| created_at | datetime | 创建时间 |
| updated_at | datetime | 更新时间 |

## 6. API 风格

基础路径：

```text
/api/v1
```

成功响应：

```json
{
  "code": "OK",
  "message": "success",
  "data": {}
}
```

错误响应：

```json
{
  "code": "VALIDATION_ERROR",
  "message": "workoutDate is required",
  "data": null
}
```

建议接口分组：

- `/auth/wechat-login`
- `/profile`
- `/body-metrics`
- `/workouts`
- `/workout-templates`
- `/diets`
- `/food-templates`
- `/calendar/monthly-summary`
- `/plans`
- `/knowledge-items`
- `/ai/knowledge-chat`
- `/ai/knowledge-chat/stream`

## 7. 前端导航

推荐底部导航：

1. 今日
2. 日历
3. 计划
4. 知识库
5. 我的

今日页可以承载训练和饮食的快捷卡片，并提供训练页面 A 和饮食页面 B 的快速跳转。

## 8. UI 交互要求

训练页面 A：

- 跳转到饮食页面 B 时，日期必须保持一致。
- 模板选择后应生成可编辑的当日记录，而不是不可修改的模板引用。
- 动作、组数和热量变化后，汇总数据应同步更新。

饮食页面 B：

- 餐次分区要便于扫描。
- 修改食物数量时，热量应立即重算。
- 摄入总热量应保持容易看到。

日历：

- 每日格子的内容必须紧凑。
- 使用“胸、肩、背、腿、臂”等短标签。
- 移动端点击区域要足够舒适。

计划：

- 当前体重、目标体重和差值必须位于上方显眼区域。
- 今日计划训练内容应直接可见。

知识库：

- 支持时应展示逐步输出的流式回答。
- 必须有非流式兜底。
- 高风险健康问题要自然出现安全提示。

## 9. 安全和隐私要求

- 所有用户数据表查询必须按认证用户过滤。
- 不信任请求体中的 `user_id`。
- 校验日期范围和数值范围。
- AI 接口需要限流。
- 除非确有必要，不存储包含敏感个人信息的原始 prompt。

## 10. 测试策略

后端：

- 热量计算、训练周期计算、日历聚合使用单元测试。
- 参数校验和认证边界使用 controller 测试。
- 核心 CRUD 流程使用集成测试。

前端：

- 小程序页面流程先以人工验收为主。
- 如果选定的小程序框架支持组件测试，再补充组件测试。

AI：

- 检索命中测试。
- 流式协议测试。
- 医疗或伤病相关问题的安全行为测试。

## 11. 演进计划

第一阶段：

- 训练和饮食记录。
- 日历汇总。
- 基础个人资料和计划。
- 模板。

第二阶段：

- 知识 CRUD。
- 基于关键词的问答。
- 流式回答。

第三阶段：

- 向量检索。
- 更多统计分析。
- 更可靠的提醒。
- 可选后台管理页面。

