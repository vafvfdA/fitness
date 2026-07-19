const app = getApp();

function request(options) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: `${app.globalData.apiBaseUrl}${options.url}`,
      method: options.method || "GET",
      data: options.data || {},
      header: {
        "content-type": "application/json",
        ...(options.header || {})
      },
      success: (response) => {
        const body = response.data;
        if (response.statusCode >= 200 && response.statusCode < 300 && body && body.code === "OK") {
          resolve(body.data);
          return;
        }
        reject(body || response);
      },
      fail: reject
    });
  });
}

module.exports = {
  request
};
