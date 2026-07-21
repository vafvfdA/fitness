# Data Model: 训练日历页面联调

## 月历页面模型

**字段**:

- `month`: 当前月份，格式 `yyyy-MM`
- `monthTitle`: 页面展示月份，格式 `yyyy年MM月`
- `loading`: 加载状态
- `error`: 错误文案
- `days`: 月历格子列表
- `summary`: 月度摘要

## 月历格子

**字段**:

- `date`: 日期，格式 `yyyy-MM-dd`
- `day`: 日期数字
- `inMonth`: 是否属于当前月
- `bodyPartsText`: 训练部位摘要
- `totalSets`: 当天总组数
- `estimatedCalories`: 当天消耗热量
- `workoutCount`: 当天训练记录数
- `hasWorkout`: 是否有训练

## 月度摘要

**字段**:

- `trainingDays`: 有训练的天数
- `estimatedCalories`: 月度总消耗
- `totalSets`: 月度总组数
