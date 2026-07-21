const { request } = require("../../utils/request");
const { mapProfile } = require("../../utils/view-model");

Page({
  data: {
    profile: mapProfile(null),
    history: [],
    loading: false,
    error: "",
    editing: false,
    form: {
      currentWeightKg: "",
      targetWeightKg: "",
      dailyCalorieTarget: ""
    }
  },

  onShow() {
    this.loadProfile();
    this.loadHistory();
  },

  loadProfile() {
    this.setData({ loading: true, error: "" });
    request({ url: "/profile" }).then((data) => {
      const profile = mapProfile(data);
      this.setData({
        profile,
        form: {
          currentWeightKg: profile.currentWeightKg,
          targetWeightKg: profile.targetWeightKg,
          dailyCalorieTarget: profile.dailyCalorieTarget
        },
        loading: false
      });
    }).catch((error) => {
      this.setData({
        error: error.message || "个人资料加载失败",
        loading: false
      });
    });
  },

  loadHistory() {
    request({ url: "/body-metrics?limit=30" }).then((data) => {
      this.setData({ history: data || [] });
    }).catch(() => {
      this.setData({ history: [] });
    });
  },

  startEdit() {
    this.setData({ editing: true });
  },

  cancelEdit() {
    this.setData({ editing: false });
  },

  onInput(event) {
    const field = event.currentTarget.dataset.field;
    this.setData({ [`form.${field}`]: event.detail.value });
  },

  saveProfile() {
    const { currentWeightKg, targetWeightKg, dailyCalorieTarget } = this.data.form;
    const body = {};
    if (currentWeightKg) body.currentWeightKg = Number(currentWeightKg);
    if (targetWeightKg) body.targetWeightKg = Number(targetWeightKg);
    if (dailyCalorieTarget) body.dailyCalorieTarget = Number(dailyCalorieTarget);

    request({ url: "/profile", method: "PUT", data: body }).then(() => {
      this.setData({ editing: false });
      this.loadProfile();
      this.loadHistory();
      wx.showToast({ title: "保存成功", icon: "success" });
    }).catch((error) => {
      wx.showToast({ title: error.message || "保存失败", icon: "none" });
    });
  }
});
