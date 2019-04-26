// pages/exerciseList/exerciseList.js
const app = getApp();
const urlPath = app.globalData.urlPath;

Page({

  /**
   * 页面的初始数据
   */
  data: {
    courseAddUrl: '../../resources/images/Ic_Course_Add.png',
    imgUrls: [{
      src: 'http://static1.keepcdn.com/2017/08/25/14/1503643687750_750x700.jpg',
      text: '20个俯卧撑',
    }, {
      src: 'http://static1.keepcdn.com/2017/08/25/14/1503643687750_750x700.jpg',
      text: '2222',
    }, {
      src: 'http://static1.keepcdn.com/2017/08/25/14/1503643687750_750x700.jpg',
      text: '3333',
    }, {
      src: 'https://img.mukewang.com/5a72827d0001cb8006000338-240-135.jpg',
      text: '4444',
    }, {
      src: 'http://static1.keepcdn.com/2017/08/25/14/1503643687750_750x700.jpg',
      text: '5555',
    },],
    indicatorDots: true,
    autoplay: true,
    interval: 5000,
    duration: 1000,
    swiperIndex: 0,
    courseFourList: [],
    total_exercise: 0,
  },
  swiperChange(e) {
    const that = this;
    that.setData({
      swiperIndex: e.detail.current,
    })
  },

//获取健身数据
  bindexercisedata: function(event) {
    wx.navigateTo({
      url: '../exerciserecord/exerciserecord',
      success: function(res) {
        console.log("exerciserecord 跳转成功")
      }
    })
  },

  bindCourse: function(event) {
  var index = event.currentTarget.dataset.index;
  var courseId = this.data.courseFourList[index].id;
    wx.navigateTo({
      url: '../exercisedetaillistmore/exercisedetaillistmore?cid=' + courseId,
      success: function (res) { console.log("跳转成功") },
    })
  },

  bindCourseTotal: function(event) {
    wx.navigateTo({
      url: '../exercisedetaillist/exercisedetaillist',
      success: function (res) { console.log("跳转成功") },

    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    wx.request({
      url: urlPath + 'courseCategory/courseFourList',
      data: {
        token: app.globalData.token,
      },
      success: function(res) {
        that.setData({courseFourList: res.data})
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
    var that = this;
    wx.request({
      url: urlPath + 'exerciseTotal/totalExercise',
      data: {
        token: app.globalData.token,
      },
      success: function (res) {
        var total_exercise = res.data;
        that.setData({ total_exercise: total_exercise });
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