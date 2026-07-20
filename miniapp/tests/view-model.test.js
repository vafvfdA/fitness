const assert = require("assert");
const {
  formatDate,
  mapTodaySummary,
  summarizeWorkoutRecords,
  summarizeDietRecords,
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

testFormatDate();
testMapTodaySummary();
testMapEmptyTodaySummary();
testSummaries();
testToNumber();

console.log("view-model tests passed");
