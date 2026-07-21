const { request } = require("../../utils/request");
const {
  formatMonth,
  buildCalendarGrid,
  summarizeCalendarMonth,
  shiftMonth
} = require("../../utils/view-model");

Page({
  data: {
    month: "",
    monthTitle: "",
    weekdays: ["日", "一", "二", "三", "四", "五", "六"],
    days: [],
    summary: summarizeCalendarMonth([]),
    loading: false,
    error: ""
  },

  onLoad(options) {
    this.setData({ month: options.month || formatMonth() });
  },

  onShow() {
    if (!this.data.month) {
      this.setData({ month: formatMonth() });
    }
    this.loadCalendar();
  },

  loadCalendar() {
    this.setData({ loading: true, error: "" });
    request({
      url: `/workout-records/calendar?month=${this.data.month}`
    }).then((calendar) => {
      const grid = buildCalendarGrid(calendar);
      this.setData({
        month: grid.month,
        monthTitle: grid.monthTitle,
        days: grid.days,
        summary: summarizeCalendarMonth(grid.days),
        loading: false
      });
    }).catch((error) => {
      const grid = buildCalendarGrid({ month: this.data.month, days: [] });
      this.setData({
        monthTitle: grid.monthTitle,
        days: grid.days,
        summary: summarizeCalendarMonth([]),
        error: error.message || "日历加载失败",
        loading: false
      });
      wx.showToast({ title: "日历加载失败", icon: "none" });
    });
  },

  previousMonth() {
    this.setData({ month: shiftMonth(this.data.month, -1) });
    this.loadCalendar();
  },

  nextMonth() {
    this.setData({ month: shiftMonth(this.data.month, 1) });
    this.loadCalendar();
  },

  openWorkout(event) {
    const date = event.currentTarget.dataset.date;
    if (!date) {
      return;
    }
    wx.navigateTo({ url: `/pages/workout/index?date=${date}` });
  },

  openDiet(event) {
    const date = event.currentTarget.dataset.date;
    if (!date) {
      return;
    }
    wx.navigateTo({ url: `/pages/diet/index?date=${date}` });
  }
});
