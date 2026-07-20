const { request } = require("../../utils/request");
const { formatDate, summarizeDietRecords, toNumber } = require("../../utils/view-model");

const defaultForm = {
  mealType: "早餐",
  foodName: "",
  amount: "1",
  unit: "份",
  calories: "",
  proteinG: "",
  fatG: "",
  carbG: ""
};

Page({
  data: {
    date: "",
    loading: false,
    saving: false,
    error: "",
    records: [],
    summary: summarizeDietRecords([]),
    form: { ...defaultForm }
  },

  onLoad(options) {
    this.setData({ date: options.date || formatDate() });
  },

  onShow() {
    if (!this.data.date) {
      this.setData({ date: formatDate() });
    }
    this.loadRecords();
  },

  loadRecords() {
    this.setData({ loading: true, error: "" });
    request({
      url: `/diet-records?date=${this.data.date}`
    }).then((records) => {
      this.setData({
        records: records || [],
        summary: summarizeDietRecords(records),
        loading: false
      });
    }).catch((error) => {
      this.setData({ error: error.message || "饮食数据加载失败", loading: false });
      wx.showToast({ title: "饮食加载失败", icon: "none" });
    });
  },

  onInput(event) {
    const field = event.currentTarget.dataset.field;
    this.setData({
      [`form.${field}`]: event.detail.value
    });
  },

  submitFood() {
    const form = this.data.form;
    if (!form.foodName.trim()) {
      wx.showToast({ title: "请填写食物名称", icon: "none" });
      return;
    }
    if (this.data.saving) {
      return;
    }

    this.setData({ saving: true });
    request({
      url: "/diet-records",
      method: "POST",
      data: {
        dietDate: this.data.date,
        note: "小程序录入",
        foods: [
          {
            mealType: form.mealType || "加餐",
            foodName: form.foodName.trim(),
            amount: toNumber(form.amount) || 1,
            unit: form.unit || "份",
            calories: toNumber(form.calories),
            proteinG: toNumber(form.proteinG),
            fatG: toNumber(form.fatG),
            carbG: toNumber(form.carbG)
          }
        ]
      }
    }).then(() => {
      wx.showToast({ title: "已记录", icon: "success" });
      this.setData({
        saving: false,
        form: { ...defaultForm }
      });
      this.loadRecords();
    }).catch((error) => {
      this.setData({ saving: false });
      wx.showToast({ title: error.message || "提交失败", icon: "none" });
    });
  },

  goWorkout() {
    wx.navigateTo({ url: `/pages/workout/index?date=${this.data.date}` });
  }
});
