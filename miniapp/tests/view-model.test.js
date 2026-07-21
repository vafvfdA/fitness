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

testFormatDate();
testMapTodaySummary();
testMapEmptyTodaySummary();
testSummaries();
testToNumber();
testCalendarMonthHelpers();
testBuildCalendarGrid();
testSummarizeCalendarMonth();

console.log("view-model tests passed");
