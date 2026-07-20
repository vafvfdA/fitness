const { request } = require("../../utils/request");
const { formatDate, summarizeWorkoutRecords } = require("../../utils/view-model");

Page({
  data: {
    date: "",
    loading: false,
    saving: false,
    error: "",
    records: [],
    templates: [],
    summary: summarizeWorkoutRecords([])
  },

  onLoad(options) {
    this.setData({ date: options.date || formatDate() });
  },

  onShow() {
    if (!this.data.date) {
      this.setData({ date: formatDate() });
    }
    this.loadData();
  },

  loadData() {
    this.setData({ loading: true, error: "" });
    Promise.all([
      request({ url: `/workout-records?date=${this.data.date}` }),
      request({ url: "/workout-templates" })
    ]).then(([records, templates]) => {
      this.setData({
        records: records || [],
        templates: templates || [],
        summary: summarizeWorkoutRecords(records),
        loading: false
      });
    }).catch((error) => {
      this.setData({ error: error.message || "训练数据加载失败", loading: false });
      wx.showToast({ title: "训练加载失败", icon: "none" });
    });
  },

  createSampleTemplate() {
    if (this.data.saving) {
      return;
    }
    this.setData({ saving: true });
    request({
      url: "/workout-templates",
      method: "POST",
      data: {
        name: "胸日模板",
        bodyPart: "胸",
        description: "小程序示例模板",
        items: [
          {
            exerciseName: "卧推",
            defaultSets: 3,
            defaultReps: 10,
            defaultWeightKg: 60,
            estimatedCalories: 180
          },
          {
            exerciseName: "上斜哑铃推举",
            defaultSets: 2,
            defaultReps: 12,
            defaultWeightKg: 22.5,
            estimatedCalories: 120
          }
        ]
      }
    }).then(() => {
      wx.showToast({ title: "模板已创建", icon: "success" });
      this.setData({ saving: false });
      this.loadData();
    }).catch((error) => {
      this.setData({ saving: false });
      wx.showToast({ title: error.message || "创建失败", icon: "none" });
    });
  },

  createWorkoutFromTemplate(event) {
    const templateId = event.currentTarget.dataset.id;
    if (!templateId || this.data.saving) {
      return;
    }
    this.setData({ saving: true });
    request({
      url: `/workout-templates/${templateId}/workout-records`,
      method: "POST",
      data: {
        workoutDate: this.data.date,
        note: "小程序模板生成"
      }
    }).then(() => {
      wx.showToast({ title: "训练已生成", icon: "success" });
      this.setData({ saving: false });
      this.loadData();
    }).catch((error) => {
      this.setData({ saving: false });
      wx.showToast({ title: error.message || "生成失败", icon: "none" });
    });
  },

  goDiet() {
    wx.navigateTo({ url: `/pages/diet/index?date=${this.data.date}` });
  }
});
