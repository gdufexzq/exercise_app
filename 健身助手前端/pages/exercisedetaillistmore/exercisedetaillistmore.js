// pages/exercisedetaillistmore/exercisedetaillistmore.js
const app = getApp();
const urlPath = app.globalData.urlPath;
Page({
  /**
   * 页面的初始数据
   */
  data: {
    courseInfoList: [],
  },
  bindCourse: function (event) {
    console.log(JSON.stringify(event))
    var index = event.currentTarget.dataset.index;
    var cid = this.data.courseInfoList[index].id;
    wx.navigateTo({
      url: '../coursedetail/coursedetail?courseId=' + cid,
      success: function (res) { console.log("跳转成功") },

    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    console.log("options:" + JSON.stringify(options))
    var categoryId = options.cid;
    wx.request({
      url: urlPath + 'courseInfo/courseInfoList',
      data: {
        token: app.globalData.token,
        categoryId: categoryId,
      },
      success: function(res) {
        var courseInfoList = res.data
        that.setData({ courseInfoList: courseInfoList});
      }
    });

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