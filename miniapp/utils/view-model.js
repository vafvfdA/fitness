function pad(value) {
  return String(value).padStart(2, "0");
}

function formatDate(date) {
  const value = date || new Date();
  return `${value.getFullYear()}-${pad(value.getMonth() + 1)}-${pad(value.getDate())}`;
}

function formatMonth(date) {
  const value = date || new Date();
  return `${value.getFullYear()}-${pad(value.getMonth() + 1)}`;
}

function shiftMonth(month, offset) {
  const parts = month.split("-").map(Number);
  const date = new Date(parts[0], parts[1] - 1 + offset, 1);
  return formatMonth(date);
}

function toNumber(value) {
  if (value === null || value === undefined || value === "") {
    return 0;
  }
  const number = Number(value);
  return Number.isFinite(number) ? number : 0;
}

function mapTodaySummary(summary) {
  const workout = summary && summary.workout ? summary.workout : {};
  const diet = summary && summary.diet ? summary.diet : {};
  const bodyParts = Array.isArray(workout.bodyParts) ? workout.bodyParts : [];

  return {
    date: summary && summary.date ? summary.date : formatDate(),
    workoutCount: workout.workoutCount || 0,
    bodyPartsText: bodyParts.length > 0 ? bodyParts.join("、") : "未训练",
    totalSets: workout.totalSets || 0,
    workoutCalories: workout.estimatedCalories || 0,
    dietCalories: diet.totalCalories || 0,
    netCalories: summary && summary.netCalories ? summary.netCalories : 0
  };
}

function summarizeWorkoutRecords(records) {
  const list = Array.isArray(records) ? records : [];
  return list.reduce((summary, record) => {
    const exercises = Array.isArray(record.exercises) ? record.exercises : [];
    return {
      recordCount: summary.recordCount + 1,
      exerciseCount: summary.exerciseCount + exercises.length,
      totalSets: summary.totalSets + (record.totalSets || 0),
      estimatedCalories: summary.estimatedCalories + (record.estimatedCalories || 0)
    };
  }, { recordCount: 0, exerciseCount: 0, totalSets: 0, estimatedCalories: 0 });
}

function summarizeDietRecords(records) {
  const list = Array.isArray(records) ? records : [];
  return list.reduce((summary, record) => {
    const foods = Array.isArray(record.foods) ? record.foods : [];
    return {
      recordCount: summary.recordCount + 1,
      foodCount: summary.foodCount + foods.length,
      totalCalories: summary.totalCalories + (record.totalCalories || 0)
    };
  }, { recordCount: 0, foodCount: 0, totalCalories: 0 });
}

function buildCalendarGrid(calendar) {
  const month = calendar && calendar.month ? calendar.month : formatMonth();
  const monthParts = month.split("-").map(Number);
  const firstDate = new Date(monthParts[0], monthParts[1] - 1, 1);
  const lastDate = new Date(monthParts[0], monthParts[1], 0);
  const startDate = new Date(firstDate);
  startDate.setDate(firstDate.getDate() - firstDate.getDay());

  const totalCells = Math.ceil((firstDate.getDay() + lastDate.getDate()) / 7) * 7;
  const dayMap = new Map();
  const sourceDays = calendar && Array.isArray(calendar.days) ? calendar.days : [];
  sourceDays.forEach((day) => {
    dayMap.set(day.date, day);
  });

  const days = [];
  for (let index = 0; index < totalCells; index++) {
    const date = new Date(startDate);
    date.setDate(startDate.getDate() + index);
    const dateText = formatDate(date);
    const source = dayMap.get(dateText);
    const bodyParts = source && Array.isArray(source.bodyParts) ? source.bodyParts : [];

    days.push({
      date: dateText,
      day: date.getDate(),
      inMonth: formatMonth(date) === month,
      bodyPartsText: bodyParts.join("、"),
      totalSets: source ? source.totalSets || 0 : 0,
      estimatedCalories: source ? source.estimatedCalories || 0 : 0,
      workoutCount: source ? source.workoutCount || 0 : 0,
      hasWorkout: !!source && (source.workoutCount || 0) > 0
    });
  }

  return {
    month,
    monthTitle: `${monthParts[0]}年${pad(monthParts[1])}月`,
    days
  };
}

function summarizeCalendarMonth(days) {
  const list = Array.isArray(days) ? days : [];
  return list.reduce((summary, day) => {
    if (!day.hasWorkout) {
      return summary;
    }
    return {
      trainingDays: summary.trainingDays + 1,
      totalSets: summary.totalSets + (day.totalSets || 0),
      estimatedCalories: summary.estimatedCalories + (day.estimatedCalories || 0)
    };
  }, { trainingDays: 0, totalSets: 0, estimatedCalories: 0 });
}

module.exports = {
  formatDate,
  formatMonth,
  shiftMonth,
  mapTodaySummary,
  summarizeWorkoutRecords,
  summarizeDietRecords,
  buildCalendarGrid,
  summarizeCalendarMonth,
  toNumber
};
