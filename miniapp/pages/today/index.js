Page({
  goWorkout() {
    wx.navigateTo({ url: "/pages/workout/index" });
  },

  goDiet() {
    wx.navigateTo({ url: "/pages/diet/index" });
  }
});
