# Data Model: 训练模板接口

## 训练模板

**表**: `workout_template`

**用途**: 保存用户可复用的训练模板。

**字段**:

- `id`: 模板 ID
- `user_id`: 用户 ID，个人模板必填；系统模板可为空
- `name`: 模板名称
- `body_part`: 训练部位，例如胸、肩、背、腿、臂
- `description`: 模板描述
- `is_system`: 是否系统模板，本轮创建的模板均为 `false`
- `created_at`: 创建时间
- `updated_at`: 更新时间

**校验规则**:

- `name` 不能为空。
- `body_part` 不能为空。
- 用户只能读取自己的模板；系统模板读取留作未来扩展。

## 训练模板动作

**表**: `workout_template_item`

**用途**: 保存模板内的默认动作和默认训练参数。

**字段**:

- `id`: 模板动作 ID
- `template_id`: 所属模板 ID
- `exercise_name`: 动作名称
- `default_sets`: 默认组数
- `default_reps`: 默认次数
- `default_weight_kg`: 默认重量
- `default_duration_seconds`: 默认时长
- `sort_order`: 排序
- `estimated_calories`: 预估消耗热量

**校验规则**:

- `exercise_name` 不能为空。
- `default_sets` 必须大于 0。
- `sort_order` 未传时由服务端按动作顺序生成。

## 从模板生成的训练记录

**表**: `workout_record`、`workout_exercise_entry`、`workout_set_entry`

**转换规则**:

- 训练记录标题默认等于模板名称。
- 训练记录训练部位等于模板训练部位。
- 训练记录总组数等于所有模板动作默认组数之和。
- 训练记录预估消耗热量等于所有模板动作预估消耗热量之和。
- 每个模板动作转换为一条训练动作。
- 每个模板动作按 `default_sets` 生成多条训练组，组号从 1 开始。
- 默认次数、默认重量、默认时长复制到每一组。
