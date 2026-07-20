# Data Model: 小程序记录 MVP 联调

## 今日汇总页面模型

**来源**: `GET /api/v1/today/summary`

**字段**:

- `date`: 当前日期
- `workoutCount`: 训练记录数量
- `bodyPartsText`: 训练部位摘要
- `totalSets`: 总组数
- `workoutCalories`: 训练消耗
- `dietCalories`: 饮食摄入
- `netCalories`: 净热量

## 训练页面模型

**来源**:

- `GET /api/v1/workout-records?date=yyyy-MM-dd`
- `GET /api/v1/workout-templates`
- `POST /api/v1/workout-templates`
- `POST /api/v1/workout-templates/{id}/workout-records`

**字段**:

- `date`: 当前训练日期
- `records`: 当天训练记录列表
- `templates`: 可选训练模板列表
- `loading`: 加载状态
- `saving`: 提交状态

## 饮食页面模型

**来源**:

- `GET /api/v1/diet-records?date=yyyy-MM-dd`
- `POST /api/v1/diet-records`

**字段**:

- `date`: 当前饮食日期
- `records`: 当天饮食记录列表
- `form`: 食物录入表单
- `summary`: 当天饮食总热量和食物数量
- `loading`: 加载状态
- `saving`: 提交状态

## 请求上下文

**字段**:

- `apiBaseUrl`: 后端接口根路径
- `devUserId`: 开发期用户 ID，默认 `1001`

**规则**:

- 所有业务接口请求必须携带 `X-User-Id`。
- 接口返回 `code !== OK` 时，页面展示错误提示。
