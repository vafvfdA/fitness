create table user_account (
    id bigint not null auto_increment,
    openid varchar(128) not null,
    nickname varchar(64),
    avatar_url varchar(512),
    status tinyint not null default 1,
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp,
    primary key (id),
    constraint uk_user_account_openid unique (openid)
);

create table user_profile (
    id bigint not null auto_increment,
    user_id bigint not null,
    gender varchar(16),
    height_cm decimal(5, 2),
    birthday date,
    current_weight_kg decimal(5, 2),
    target_weight_kg decimal(5, 2),
    daily_calorie_target int,
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp,
    primary key (id),
    constraint uk_user_profile_user_id unique (user_id)
);

create table body_metric_record (
    id bigint not null auto_increment,
    user_id bigint not null,
    record_date date not null,
    weight_kg decimal(5, 2),
    body_fat_percent decimal(5, 2),
    waist_cm decimal(5, 2),
    note varchar(512),
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp,
    primary key (id)
);

create index idx_body_metric_user_date on body_metric_record (user_id, record_date);

create table workout_record (
    id bigint not null auto_increment,
    user_id bigint not null,
    workout_date date not null,
    title varchar(128),
    body_part varchar(32) not null,
    total_sets int not null default 0,
    estimated_calories int not null default 0,
    note varchar(512),
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp,
    primary key (id)
);

create index idx_workout_record_user_date on workout_record (user_id, workout_date);
create index idx_workout_record_user_body_part on workout_record (user_id, body_part);

create table workout_exercise_entry (
    id bigint not null auto_increment,
    workout_record_id bigint not null,
    user_id bigint not null,
    exercise_name varchar(128) not null,
    body_part varchar(32),
    sort_order int not null default 0,
    estimated_calories int not null default 0,
    note varchar(512),
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp,
    primary key (id)
);

create index idx_workout_exercise_record on workout_exercise_entry (workout_record_id, sort_order);
create index idx_workout_exercise_user on workout_exercise_entry (user_id);

create table workout_set_entry (
    id bigint not null auto_increment,
    exercise_entry_id bigint not null,
    user_id bigint not null,
    set_no int not null,
    reps int,
    weight_kg decimal(6, 2),
    duration_seconds int,
    distance_meters int,
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp,
    primary key (id)
);

create index idx_workout_set_exercise on workout_set_entry (exercise_entry_id, set_no);
create index idx_workout_set_user on workout_set_entry (user_id);

create table workout_template (
    id bigint not null auto_increment,
    user_id bigint,
    name varchar(128) not null,
    body_part varchar(32) not null,
    description varchar(512),
    is_system boolean not null default false,
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp,
    primary key (id)
);

create index idx_workout_template_user_body_part on workout_template (user_id, body_part);

create table workout_template_item (
    id bigint not null auto_increment,
    template_id bigint not null,
    exercise_name varchar(128) not null,
    default_sets int not null default 0,
    default_reps int,
    default_weight_kg decimal(6, 2),
    default_duration_seconds int,
    sort_order int not null default 0,
    primary key (id)
);

create index idx_workout_template_item_template on workout_template_item (template_id, sort_order);

create table diet_record (
    id bigint not null auto_increment,
    user_id bigint not null,
    diet_date date not null,
    total_calories int not null default 0,
    note varchar(512),
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp,
    primary key (id)
);

create index idx_diet_record_user_date on diet_record (user_id, diet_date);

create table diet_food_entry (
    id bigint not null auto_increment,
    diet_record_id bigint not null,
    user_id bigint not null,
    meal_type varchar(32) not null,
    food_name varchar(128) not null,
    amount decimal(8, 2) not null,
    unit varchar(32) not null,
    calories int not null default 0,
    protein_g decimal(8, 2),
    fat_g decimal(8, 2),
    carb_g decimal(8, 2),
    sort_order int not null default 0,
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp,
    primary key (id)
);

create index idx_diet_food_record_meal on diet_food_entry (diet_record_id, meal_type, sort_order);
create index idx_diet_food_user on diet_food_entry (user_id);

create table food_template (
    id bigint not null auto_increment,
    user_id bigint,
    food_name varchar(128) not null,
    default_unit varchar(32) not null,
    calories_per_unit decimal(8, 2) not null,
    protein_per_unit decimal(8, 2),
    fat_per_unit decimal(8, 2),
    carb_per_unit decimal(8, 2),
    is_system boolean not null default false,
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp,
    primary key (id)
);

create index idx_food_template_user_name on food_template (user_id, food_name);

create table training_plan (
    id bigint not null auto_increment,
    user_id bigint not null,
    name varchar(128) not null,
    cycle_type varchar(32) not null,
    train_days int not null default 0,
    rest_days int not null default 0,
    start_date date not null,
    muscle_rotation_json json,
    daily_calorie_target int,
    reminder_enabled boolean not null default false,
    reminder_time time,
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp,
    primary key (id)
);

create index idx_training_plan_user on training_plan (user_id);

create table knowledge_item (
    id bigint not null auto_increment,
    user_id bigint,
    title varchar(128) not null,
    category varchar(32) not null,
    content text not null,
    tags varchar(512),
    source varchar(512),
    is_system boolean not null default false,
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp,
    primary key (id)
);

create index idx_knowledge_item_user_category on knowledge_item (user_id, category);
create index idx_knowledge_item_title on knowledge_item (title);
