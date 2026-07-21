# Data Model: 计划与个人资料页面联调

## 个人资料响应 (ProfileResponse)

**字段**:

- `userId`: 用户 ID
- `gender`: 性别，可选
- `heightCm`: 身高（厘米），可选
- `birthday`: 生日（yyyy-MM-dd），可选
- `currentWeightKg`: 当前体重（千克）
- `targetWeightKg`: 目标体重（千克）
- `dailyCalorieTarget`: 每日热量目标
- `weightDiffKg`: 体重差值（计算字段）= currentWeightKg - targetWeightKg，正数表示需减重，负数表示需增重

## 体重历史记录 (BodyMetricResponse)

**字段**:

- `id`: 记录 ID
- `recordDate`: 记录日期（yyyy-MM-dd）
- `weightKg`: 体重
- `bodyFatPercent`: 体脂率，可选
- `waistCm`: 腰围，可选
- `note`: 备注，可选

## 训练计划响应 (TrainingPlanResponse)

**字段**:

- `id`: 计划 ID
- `name`: 计划名称
- `cycleType`: 周期类型，如 `TRAIN_REST`
- `trainDays`: 训练天数
- `restDays`: 休息天数
- `startDate`: 周期起始日期（yyyy-MM-dd）
- `muscleRotation`: 部位轮换数组，如 `["胸","肩","背"]`
- `dailyCalorieTarget`: 每日热量目标，可选
- `reminderEnabled`: 是否开启提醒
- `reminderTime`: 提醒时间（HH:mm），可选

## 今日计划响应 (TodayPlanResponse)

**字段**:

- `date`: 今天日期（yyyy-MM-dd）
- `isTrainingDay`: 今天是否训练日
- `cycleDayIndex`: 今天在当前周期内的第几天（从 1 开始；start_date 在未来时为 0）
- `todayBodyPart`: 今日训练部位，休息日或无轮换时为空字符串
- `daysSinceStart`: 自 start_date 经过的天数（今天早于 start_date 时为负数）

## 前端页面模型

### profile 页

- `profile`: ProfileResponse 或 null
- `history`: BodyMetricResponse 数组
- `loading`: 加载状态
- `error`: 错误文案
- `editing`: 是否处于编辑态

### plan 页

- `plan`: TrainingPlanResponse 或 null
- `today`: TodayPlanResponse 或 null
- `loading`: 加载状态
- `error`: 错误文案

## 计算规则

### 体重差值

```
weightDiffKg = currentWeightKg - targetWeightKg
```

### 训练周期今日状态

```
daysSinceStart = today - startDate（按自然日）
cycleLength = trainDays + restDays
cycleDayIndex = (daysSinceStart % cycleLength) + 1   // 当 daysSinceStart >= 0
isTrainingDay = cycleDayIndex <= trainDays
```

### 今日部位（仅训练日）

```
elapsedTrainingDays = daysSinceStart - (daysSinceStart / cycleLength) * restDays
// 即已经过去的训练日数（非整周期去掉休息日）
todayBodyPart = muscleRotation[elapsedTrainingDays % muscleRotation.length]
```

边界：

- `daysSinceStart < 0`（start_date 在未来）：`isTrainingDay=false`，`cycleDayIndex=0`，`todayBodyPart=""`。
- `muscleRotation` 为空：`todayBodyPart=""`，不影响 `isTrainingDay`。
