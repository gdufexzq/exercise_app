// pages/coursedetail/coursedetail.js
const app = getApp();
const urlPath = app.globalData.urlPath;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    courseInfo: {},
    activityList: [],
    recordCount: 0,
  },
  bindexerciseVideo: function(event) {
    var courseId = event.currentTarget.dataset.index;

   //说明还没训练过
    if(this.data.recordCount == 0) {
      
      //保存用户的课程记录
      wx.request({
        url: urlPath + 'courseRecord/addcourseRecord',
        data: {
          token: app.globalData.token,
          courseId: courseId,
        },
        success: function (res) {
          console.log("加入训练成功");
          wx.showToast({
            title: '加入训练成功',
            icon: 'none',
          })
        }
      })
      
    } else {
      //更新训练次数
      var recordCount = this.data.recordCount + 1;
      wx.request({
        url: urlPath + 'courseRecord/updatecourseRecord',
        data: {
          token: app.globalData.token,
          courseId: courseId,
          recordCount: recordCount,
        },
        success: function (res) {
          console.log("更新训练次数成功");
         
        }
      })
      this.setData({ recordCount: recordCount});
    }

    //课程训练页面
    wx.setStorageSync('courseInfo', this.data.courseInfo);
    wx.setStorageSync('activityList', this.data.activityList);
    wx.navigateTo({
      url: '../exercisedetaillistone/exercisedetaillistone',
    })
   
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
     var courseId = options.courseId;

     wx.request({
       url: urlPath + 'courseDetail/courseDetailInfo',
       data: {
         token: app.globalData.token,
         courseId: courseId,
       },
       success: function(res) {
         console.log("courseDetail: " + JSON.stringify(res.data));
          var courseInfo = res.data[0];
          var activityList = res.data[1];
          that.setData({courseInfo: courseInfo, 
           activityList: activityList,
          })
       }
     })

     //获取你的训练次数
     wx.request({
       url: urlPath + 'courseRecord/getRecordCount',
       data: {
         token: app.globalData.token,
         courseId: courseId,
       },
       success: function(res) {
         var recordCount = res.data + 1;
         that.setData({recordCount: recordCount});
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