// pages/addresslist/addresslist.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    addressList: [],
  },
  toAddAddress: function(res) {
      wx.navigateTo({
        url: '../addaddress/addaddress',
      })
  },
  getAddressList: function(event) {
    console.log(JSON.stringify(event));
    var index = event.currentTarget.dataset.index;
    var addressInfo = this.data.addressList[index];
    console.log("addressInfo:" + addressInfo);
    wx.setStorageSync('addressInfo', addressInfo);
    //跳转到订单确认界面
   
    console.log("13424");
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    wx.request({
      url: app.globalData.urlPath + 'address/addressList',

      data: {
        token: app.globalData.token,
      },
      success: function(res) {
        var addressList = res.data;
       
        console.log(JSON.stringify(res.data));
        that.setData({addressList: addressList});
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