
const app = getApp();
const urlPath = app.globalData.urlPath;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    totalCount: 0,
    exerciseRecordList: [],
  },
  queryExercise: function(options) {
    var that = this;
    wx.request({
      url: urlPath + 'exerciseTotal/totalExercise',
      data: {
        token: app.globalData.token,
      },
      success: function (res) {
        var totalCount = res.data;
        that.setData({ totalCount: totalCount });
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

    // console.log("test...");
    // console.log(new Date().getTime());
    // console.log(new Date(new Date().toLocaleDateString()).getTime());
    var that = this;
    //查询健身次数
   that.queryExercise();
    wx.request({
      url: urlPath + 'courseRecord/exerciseRecordList',
      data: {
        token: app.globalData.token,
      },
      success: function (res) {
        console.log("courseRecord:" + JSON.stringify(res.data));
        that.setData({
          exerciseRecordList: res.data,
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