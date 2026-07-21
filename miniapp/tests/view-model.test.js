const assert = require("assert");
const {
  formatDate,
  mapTodaySummary,
  summarizeWorkoutRecords,
  summarizeDietRecords,
  formatMonth,
  shiftMonth,
  buildCalendarGrid,
  summarizeCalendarMonth,
  mapProfile,
  mapTodayPlan,
  scaleFoodTemplate,
  toNumber
} = require("../utils/view-model");

function testFormatDate() {
  const date = new Date(2026, 6, 21);
  assert.strictEqual(formatDate(date), "2026-07-21");
}

function testMapTodaySummary() {
  const summary = mapTodaySummary({
    date: "2026-07-21",
    workout: {
      workoutCount: 1,
      bodyParts: ["胸", "肩"],
      totalSets: 5,
      estimatedCalories: 300
    },
    diet: {
      totalCalories: 720
    },
    netCalories: 420
  });

  assert.strictEqual(summary.date, "2026-07-21");
  assert.strictEqual(summary.workoutCount, 1);
  assert.strictEqual(summary.bodyPartsText, "胸、肩");
  assert.strictEqual(summary.totalSets, 5);
  assert.strictEqual(summary.workoutCalories, 300);
  assert.strictEqual(summary.dietCalories, 720);
  assert.strictEqual(summary.netCalories, 420);
}

function testMapEmptyTodaySummary() {
  const summary = mapTodaySummary(null);

  assert.strictEqual(summary.workoutCount, 0);
  assert.strictEqual(summary.bodyPartsText, "未训练");
  assert.strictEqual(summary.totalSets, 0);
  assert.strictEqual(summary.workoutCalories, 0);
  assert.strictEqual(summary.dietCalories, 0);
  assert.strictEqual(summary.netCalories, 0);
}

function testSummaries() {
  assert.deepStrictEqual(
    summarizeWorkoutRecords([
      { totalSets: 3, estimatedCalories: 180, exercises: [{}, {}] },
      { totalSets: 2, estimatedCalories: 120, exercises: [{}] }
    ]),
    { recordCount: 2, exerciseCount: 3, totalSets: 5, estimatedCalories: 300 }
  );

  assert.deepStrictEqual(
    summarizeDietRecords([
      { totalCalories: 200, foods: [{}, {}] },
      { totalCalories: 300, foods: [{}] }
    ]),
    { recordCount: 2, foodCount: 3, totalCalories: 500 }
  );
}

function testToNumber() {
  assert.strictEqual(toNumber("12.5"), 12.5);
  assert.strictEqual(toNumber(""), 0);
  assert.strictEqual(toNumber(undefined), 0);
}

function testCalendarMonthHelpers() {
  assert.strictEqual(formatMonth(new Date(2026, 6, 1)), "2026-07");
  assert.strictEqual(shiftMonth("2026-01", -1), "2025-12");
  assert.strictEqual(shiftMonth("2026-12", 1), "2027-01");
}

function testBuildCalendarGrid() {
  const grid = buildCalendarGrid({
    month: "2026-07",
    days: [
      {
        date: "2026-07-21",
        bodyParts: ["胸", "肩"],
        totalSets: 5,
        estimatedCalories: 300,
        workoutCount: 1
      }
    ]
  });

  assert.strictEqual(grid.month, "2026-07");
  assert.strictEqual(grid.monthTitle, "2026年07月");
  assert.strictEqual(grid.days.length, 35);
  assert.strictEqual(grid.days[0].date, "2026-06-28");
  assert.strictEqual(grid.days[0].inMonth, false);

  const trainedDay = grid.days.find((day) => day.date === "2026-07-21");
  assert.strictEqual(trainedDay.inMonth, true);
  assert.strictEqual(trainedDay.hasWorkout, true);
  assert.strictEqual(trainedDay.bodyPartsText, "胸、肩");
  assert.strictEqual(trainedDay.estimatedCalories, 300);

  const emptyDay = grid.days.find((day) => day.date === "2026-07-22");
  assert.strictEqual(emptyDay.hasWorkout, false);
  assert.strictEqual(emptyDay.bodyPartsText, "");
}

function testSummarizeCalendarMonth() {
  assert.deepStrictEqual(
    summarizeCalendarMonth([
      { hasWorkout: true, totalSets: 5, estimatedCalories: 300 },
      { hasWorkout: false, totalSets: 0, estimatedCalories: 0 },
      { hasWorkout: true, totalSets: 3, estimatedCalories: 180 }
    ]),
    { trainingDays: 2, totalSets: 8, estimatedCalories: 480 }
  );
}

function testMapProfile() {
  const losing = mapProfile({
    currentWeightKg: 75,
    targetWeightKg: 70,
    weightDiffKg: 5,
    dailyCalorieTarget: 2000,
    gender: "male",
    heightCm: 175,
    birthday: "1995-01-01"
  });
  assert.strictEqual(losing.exists, true);
  assert.strictEqual(losing.currentWeightKg, "75");
  assert.strictEqual(losing.targetWeightKg, "70");
  assert.strictEqual(losing.weightDiffKg, "5");
  assert.strictEqual(losing.diffText, "需减重 5 kg");

  const gaining = mapProfile({
    currentWeightKg: 68,
    targetWeightKg: 70,
    weightDiffKg: -2,
    dailyCalorieTarget: 2000
  });
  assert.strictEqual(gaining.diffText, "需增重 2 kg");

  const met = mapProfile({
    currentWeightKg: 70,
    targetWeightKg: 70,
    weightDiffKg: 0,
    dailyCalorieTarget: 2000
  });
  assert.strictEqual(met.diffText, "已达标");

  const empty = mapProfile(null);
  assert.strictEqual(empty.exists, false);
  assert.strictEqual(empty.diffText, "未设置目标");
}

function testMapTodayPlan() {
  const training = mapTodayPlan({
    date: "2026-07-22",
    isTrainingDay: true,
    cycleDayIndex: 1,
    todayBodyPart: "胸",
    daysSinceStart: 0
  });
  assert.strictEqual(training.exists, true);
  assert.strictEqual(training.statusText, "今日训练");
  assert.strictEqual(training.bodyPartText, "胸");

  const rest = mapTodayPlan({
    date: "2026-07-22",
    isTrainingDay: false,
    cycleDayIndex: 4,
    todayBodyPart: "",
    daysSinceStart: 3
  });
  assert.strictEqual(rest.statusText, "今日休息");
  assert.strictEqual(rest.bodyPartText, "");

  const future = mapTodayPlan({
    date: "2026-07-22",
    isTrainingDay: false,
    cycleDayIndex: 0,
    todayBodyPart: "",
    daysSinceStart: -2
  });
  assert.strictEqual(future.statusText, "计划未开始");

  const empty = mapTodayPlan(null);
  assert.strictEqual(empty.exists, false);
  assert.strictEqual(empty.statusText, "未设置计划");
}

testFormatDate();
testMapTodaySummary();
testMapEmptyTodaySummary();
testSummaries();
testToNumber();
testCalendarMonthHelpers();
testBuildCalendarGrid();
testSummarizeCalendarMonth();
testMapProfile();
testMapTodayPlan();

function testScaleFoodTemplate() {
  const chicken = {
    foodName: "鸡胸肉",
    defaultUnit: "100g",
    caloriesPerUnit: 133,
    proteinPerUnit: 31,
    fatPerUnit: 1.2,
    carbPerUnit: 0
  };

  // amount=1：原值
  const one = scaleFoodTemplate(chicken, 1);
  assert.strictEqual(one.foodName, "鸡胸肉");
  assert.strictEqual(one.unit, "100g");
  assert.strictEqual(one.amount, "1");
  assert.strictEqual(one.calories, "133");
  assert.strictEqual(one.proteinG, "31");
  assert.strictEqual(one.fatG, "1.2");
  assert.strictEqual(one.carbG, "0");

  // amount=2：翻倍
  const two = scaleFoodTemplate(chicken, 2);
  assert.strictEqual(two.calories, "266");
  assert.strictEqual(two.proteinG, "62");
  assert.strictEqual(two.fatG, "2.4");
  assert.strictEqual(two.carbG, "0");

  // amount=0.5：减半，热量四舍五入
  const half = scaleFoodTemplate(chicken, 0.5);
  assert.strictEqual(half.calories, "67");
  assert.strictEqual(half.proteinG, "15.5");
  assert.strictEqual(half.fatG, "0.6");

  // amount 缺省默认 1
  const def = scaleFoodTemplate(chicken, "");
  assert.strictEqual(def.amount, "1");
  assert.strictEqual(def.calories, "133");

  // 模板为 null：返回空字段，不抛错
  const empty = scaleFoodTemplate(null, 1);
  assert.strictEqual(empty.foodName, "");
  assert.strictEqual(empty.unit, "");
  assert.strictEqual(empty.calories, "0");
}
testScaleFoodTemplate();

console.log("view-model tests passed");
