// pages/me/me.js
const app = getApp();
const urlPath = app.globalData.urlPath;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    info: {}
  },
  bindData: function(event) {
      wx.navigateTo({
        url: '../mydata/mydata',
      })
  },
  bindOrder: function(event) {

    wx.navigateTo({
      url: '../orderlist/orderlist',
    })
  },

  bindGoodcar: function() {
    wx.navigateTo({
      url: '/pages/goodcar/goodcar',
      success() {
        console.log('跳转成功');
      }
    })
  },

  bindCourse: function(event) {
    wx.navigateTo({
      url: '../mycourse/mycourse',
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var info = {
      'headImg': app.globalData.userInfo.avatarUrl,
      'username': app.globalData.userInfo.nickName,
    };
    this.setData({
      info: info,
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
    console.log("mine show...");
    var that = this;
    wx.request({
      url: urlPath + 'exerciseTotal/totalExercise',
      data: {
        token: app.globalData.token,
      },
      success: function (res) {
        var total_exercise = res.data;
        that.setData({
          exerciseCoin: app.globalData.total_coin,
          exerciseCount: total_exercise,
        })
      }
    })
   
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