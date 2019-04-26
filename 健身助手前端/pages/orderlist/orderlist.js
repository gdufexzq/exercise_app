// pages/orderlist/orderlist.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    oid: 0,
    orderHead: [{ "id": 0, name: "全部" }, { "id": 1, name: "待支付" }, { "id": 2, name: "待发货" },
    { "id": 3, name: "待签收" }, { "id": 4, name: "已签收" }],
    orderList: [],
  },
  selectType(event) {
    var oid = event.target.dataset.oid;
    this.setData({
      oid: oid
    })

    var that = this;
    //获取订单数据
    wx.request({
      url: app.globalData.urlPath + 'order/allOrderInfo',
      data: {
        token: app.globalData.token,
        status: oid,
      },
      success: function (res) {
        var orders = res.data;
        console.log("order:" + JSON.stringify(res));
        that.setData({ orderList: orders });
      }
    })


  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    //获取订单数据
    wx.request({
      url: app.globalData.urlPath + 'order/allOrderInfo',
      data: {
        token: app.globalData.token,
        status: 0,
      },
      success: function(res) {
        var orders = res.data;
        console.log("order:" + JSON.stringify(res));
        that.setData({orderList: orders});
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