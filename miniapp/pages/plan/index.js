const { request } = require("../../utils/request");
const { mapTodayPlan, formatDate } = require("../../utils/view-model");

Page({
  data: {
    plan: null,
    today: mapTodayPlan(null),
    loading: false,
    error: "",
    editing: false,
    form: {
      name: "练三休一",
      trainDays: "3",
      restDays: "1",
      startDate: "",
      muscleRotation: "胸,肩,背",
      dailyCalorieTarget: ""
    }
  },

  onShow() {
    if (!this.data.form.startDate) {
      this.setData({ "form.startDate": formatDate() });
    }
    this.loadPlan();
    this.loadToday();
  },

  loadPlan() {
    this.setData({ loading: true, error: "" });
    request({ url: "/plans/current" }).then((data) => {
      if (data) {
        this.setData({
          plan: data,
          "form.name": data.name || "",
          "form.trainDays": String(data.trainDays || ""),
          "form.restDays": String(data.restDays || ""),
          "form.startDate": data.startDate || formatDate(),
          "form.muscleRotation": (data.muscleRotation || []).join(","),
          "form.dailyCalorieTarget": data.dailyCalorieTarget != null ? String(data.dailyCalorieTarget) : ""
        });
      } else {
        this.setData({ plan: null });
      }
      this.setData({ loading: false });
    }).catch((error) => {
      this.setData({ error: error.message || "计划加载失败", loading: false });
    });
  },

  loadToday() {
    request({ url: "/plans/current/today" }).then((data) => {
      this.setData({ today: mapTodayPlan(data) });
    }).catch(() => {
      this.setData({ today: mapTodayPlan(null) });
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

  onDateChange(event) {
    this.setData({ "form.startDate": event.detail.value });
  },

  savePlan() {
    const { name, trainDays, restDays, startDate, muscleRotation, dailyCalorieTarget } = this.data.form;
    const body = {
      name: name || "我的计划",
      cycleType: "TRAIN_REST",
      trainDays: Number(trainDays),
      restDays: Number(restDays),
      startDate: startDate,
      muscleRotation: muscleRotation ? muscleRotation.split(",").map((s) => s.trim()).filter(Boolean) : []
    };
    if (dailyCalorieTarget) body.dailyCalorieTarget = Number(dailyCalorieTarget);

    request({ url: "/plans/current", method: "PUT", data: body }).then(() => {
      this.setData({ editing: false });
      this.loadPlan();
      this.loadToday();
      wx.showToast({ title: "保存成功", icon: "success" });
    }).catch((error) => {
      wx.showToast({ title: error.message || "保存失败", icon: "none" });
    });
  }
});
