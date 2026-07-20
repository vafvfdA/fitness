const app = getApp();

function request(options) {
  const header = {
    "content-type": "application/json",
    "X-User-Id": app.globalData.devUserId,
    ...(options.header || {})
  };

  return new Promise((resolve, reject) => {
    wx.request({
      url: `${app.globalData.apiBaseUrl}${options.url}`,
      method: options.method || "GET",
      data: options.data || {},
      header,
      success: (response) => {
        const body = response.data;
        if (response.statusCode >= 200 && response.statusCode < 300 && body && body.code === "OK") {
          resolve(body.data);
          return;
        }
        reject(body || { message: "请求失败", response });
      },
      fail: reject
    });
  });
}

module.exports = {
  request
};
