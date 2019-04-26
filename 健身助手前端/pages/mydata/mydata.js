// pages/mydata/mydata.js
const app = getApp();
const urlPath = app.globalData.urlPath;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    totalCoin: 0,
    totalCount: 0,
  },
  bindTotalCoin: function(event) {
    wx.navigateTo({
      url: '../coinrecord/coinrecord',
      success:function(res) {
        console.log("coinrecord 跳转成功");
      }
    })
  },
  bindTotalCount: function(event) {
    wx.navigateTo({
      url: '../exerciserecord/exerciserecord',
      success: function (res) {
        console.log("exerciserecord 跳转成功");
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

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
    var that = this;
    wx.request({
      url: urlPath + 'exerciseTotal/totalExercise',
      data: {
        token: app.globalData.token,
      },
      success: function (res) {
        var totalCount = res.data;
        that.setData({ totalCount: totalCount,
         totalCoin: app.globalData.total_coin,
         });
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