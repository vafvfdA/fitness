Page({
  data: {
    date: ""
  },

  onLoad(options) {
    this.setData({ date: options.date || new Date().toISOString().slice(0, 10) });
  },

  goDiet() {
    wx.navigateTo({ url: `/pages/diet/index?date=${this.data.date}` });
  }
});
