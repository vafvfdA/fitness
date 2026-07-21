const { request } = require("../../utils/request");
const { formatDate, summarizeDietRecords, toNumber, scaleFoodTemplate } = require("../../utils/view-model");

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

const defaultAddTemplateForm = {
  foodName: "",
  defaultUnit: "100g",
  caloriesPerUnit: "",
  proteinPerUnit: "",
  fatPerUnit: "",
  carbPerUnit: ""
};

Page({
  data: {
    date: "",
    loading: false,
    saving: false,
    error: "",
    records: [],
    summary: summarizeDietRecords([]),
    form: { ...defaultForm },
    selectedTemplate: null,
    picker: { visible: false, keyword: "", templates: [], loading: false },
    addForm: { visible: false, saving: false, foodName: "", defaultUnit: "100g", caloriesPerUnit: "", proteinPerUnit: "", fatPerUnit: "", carbPerUnit: "" }
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
    const value = event.detail.value;
    // 改份量时，若已选模板，按份量重新缩放热量/宏量
    if (field === "amount" && this.data.selectedTemplate) {
      const scaled = scaleFoodTemplate(this.data.selectedTemplate, value);
      this.setData({
        "form.amount": value,
        "form.calories": scaled.calories,
        "form.proteinG": scaled.proteinG,
        "form.fatG": scaled.fatG,
        "form.carbG": scaled.carbG
      });
      return;
    }
    const patch = { [`form.${field}`]: value };
    // 手动改热量/宏量后取消模板关联，避免后续改份量覆盖手动值
    if (field === "calories" || field === "proteinG" || field === "fatG" || field === "carbG") {
      patch.selectedTemplate = null;
    }
    this.setData(patch);
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
        form: { ...defaultForm },
        selectedTemplate: null
      });
      this.loadRecords();
    }).catch((error) => {
      this.setData({ saving: false });
      wx.showToast({ title: error.message || "提交失败", icon: "none" });
    });
  },

  // ===== 食物模板选择器 =====
  openTemplatePicker() {
    this.setData({ "picker.visible": true, "picker.keyword": "", "picker.templates": [] });
    this.loadTemplates("");
  },

  closeTemplatePicker() {
    this.setData({ "picker.visible": false });
  },

  onPickerKeywordInput(event) {
    const keyword = event.detail.value;
    this.setData({ "picker.keyword": keyword });
    this.loadTemplates(keyword);
  },

  loadTemplates(keyword) {
    this.setData({ "picker.loading": true });
    const url = keyword ? `/food-templates?keyword=${encodeURIComponent(keyword)}` : "/food-templates";
    request({ url }).then((data) => {
      this.setData({ "picker.templates": data || [], "picker.loading": false });
    }).catch(() => {
      this.setData({ "picker.templates": [], "picker.loading": false });
      wx.showToast({ title: "模板加载失败", icon: "none" });
    });
  },

  pickTemplate(event) {
    const template = event.currentTarget.dataset.template;
    const scaled = scaleFoodTemplate(template, 1);
    this.setData({
      "form.foodName": scaled.foodName,
      "form.unit": scaled.unit,
      "form.amount": scaled.amount,
      "form.calories": scaled.calories,
      "form.proteinG": scaled.proteinG,
      "form.fatG": scaled.fatG,
      "form.carbG": scaled.carbG,
      selectedTemplate: template,
      "picker.visible": false
    });
    wx.showToast({ title: "已填入，可改份量", icon: "none" });
  },

  // ===== 新增模板 =====
  openAddTemplate() {
    this.setData({
      "addForm.visible": true,
      "addForm.foodName": "",
      "addForm.defaultUnit": "100g",
      "addForm.caloriesPerUnit": "",
      "addForm.proteinPerUnit": "",
      "addForm.fatPerUnit": "",
      "addForm.carbPerUnit": ""
    });
  },

  closeAddTemplate() {
    this.setData({ "addForm.visible": false });
  },

  onAddTemplateInput(event) {
    const field = event.currentTarget.dataset.field;
    this.setData({ [`addForm.${field}`]: event.detail.value });
  },

  saveTemplate() {
    const f = this.data.addForm;
    if (!f.foodName.trim()) {
      wx.showToast({ title: "请填食物名称", icon: "none" });
      return;
    }
    if (!toNumber(f.caloriesPerUnit) || toNumber(f.caloriesPerUnit) <= 0) {
      wx.showToast({ title: "热量需大于0", icon: "none" });
      return;
    }
    if (this.data.addForm.saving) {
      return;
    }
    this.setData({ "addForm.saving": true });
    request({
      url: "/food-templates",
      method: "POST",
      data: {
        foodName: f.foodName.trim(),
        defaultUnit: f.defaultUnit || "份",
        caloriesPerUnit: toNumber(f.caloriesPerUnit),
        proteinPerUnit: toNumber(f.proteinPerUnit),
        fatPerUnit: toNumber(f.fatPerUnit),
        carbPerUnit: toNumber(f.carbPerUnit)
      }
    }).then(() => {
      this.setData({ "addForm.saving": false, "addForm.visible": false });
      wx.showToast({ title: "模板已新增", icon: "success" });
      this.loadTemplates(this.data.picker.keyword);
    }).catch((error) => {
      this.setData({ "addForm.saving": false });
      wx.showToast({ title: error.message || "新增失败", icon: "none" });
    });
  },

  goWorkout() {
    wx.navigateTo({ url: `/pages/workout/index?date=${this.data.date}` });
  }
});
