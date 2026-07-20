const { request } = require("../../utils/request");
const { formatDate, mapTodaySummary } = require("../../utils/view-model");

Page({
  data: {
    date: "",
    loading: false,
    error: "",
    summary: mapTodaySummary(null)
  },

  onLoad(options) {
    this.setData({ date: options.date || formatDate() });
  },

  onShow() {
    if (!this.data.date) {
      this.setData({ date: formatDate() });
    }
    this.loadSummary();
  },

  loadSummary() {
    this.setData({ loading: true, error: "" });
    request({
      url: `/today/summary?date=${this.data.date}`
    }).then((data) => {
      this.setData({
        summary: mapTodaySummary(data),
        loading: false
      });
    }).catch((error) => {
      this.setData({
        error: error.message || "今日汇总加载失败",
        summary: mapTodaySummary(null),
        loading: false
      });
      wx.showToast({ title: "汇总加载失败", icon: "none" });
    });
  },

  goWorkout() {
    wx.navigateTo({ url: `/pages/workout/index?date=${this.data.date}` });
  },

  goDiet() {
    wx.navigateTo({ url: `/pages/diet/index?date=${this.data.date}` });
  }
});
