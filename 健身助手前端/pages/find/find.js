// pages/find/find.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    types: [
      // { "id": 2, name: "训练" }, { "id": 1, name: "瑜伽" }, { "id": 3, name: "女子服饰" }, { "id": 4, name: "男子服饰" },
    ],
    typeList: [],
  },
  /**
   * 点击分类
   */
  tapType: function(event) {
    var that = this;
    // console.log("options" + JSON.stringify(event));
    console.log("typeId:" + event.target.dataset.typeId);
    wx.request({
      url: app.globalData.urlPath + 'category/categoryInfo',
      data: {
        token: app.globalData.token,
        typeId: event.target.dataset.typeId,
      },
      success: function(res) {
        that.setData({
          typeList: res.data[1].typeList
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {
    var that = this;
    wx.request({
      url: app.globalData.urlPath + 'category/categoryInfo',
      data: {
        token: app.globalData.token,
        typeId: 1,
      },
      success: function(res) {

        console.log(":" + that.data.types);
        console.log("type:" + JSON.stringify(res.data[0].types));
        that.setData({
          types: res.data[0].types,
          typeList: res.data[1].typeList
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }
})