// pages/coinrecord/coinrecord.js
const app = getApp();
const urlPath = app.globalData.urlPath;
Page({

  /**
   * 页面的初始数据
   */
  data: {
      totalCoin: 0,
      todayCoin: 0,
      coinRecordList: [],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

    // console.log("test...");
    // console.log(new Date().getTime());
    // console.log(new Date(new Date().toLocaleDateString()).getTime());
    var that = this;
    app.queryCoin();
    wx.request({
      url: urlPath + 'coinDetail/coinDetailList',
      data: {
        token: app.globalData.token,
      },
      success: function(res) {
       
        that.setData({
          totalCoin: app.globalData.total_coin,
        todayCoin: app.globalData.today_coin,
          coinRecordList: res.data,
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})