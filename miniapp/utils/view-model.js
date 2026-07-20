function pad(value) {
  return String(value).padStart(2, "0");
}

function formatDate(date) {
  const value = date || new Date();
  return `${value.getFullYear()}-${pad(value.getMonth() + 1)}-${pad(value.getDate())}`;
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

module.exports = {
  formatDate,
  mapTodaySummary,
  summarizeWorkoutRecords,
  summarizeDietRecords,
  toNumber
};
