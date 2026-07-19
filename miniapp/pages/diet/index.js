Page({
  data: {
    date: ""
  },

  onLoad(options) {
    this.setData({ date: options.date || new Date().toISOString().slice(0, 10) });
  },

  goWorkout() {
    wx.navigateTo({ url: `/pages/workout/index?date=${this.data.date}` });
  }
});
