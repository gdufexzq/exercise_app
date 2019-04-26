// pages/exercisedetaillistone/exercisedetaillistone.js
const app = getApp();
const urlPath = app.globalData.urlPath;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    courseInfo: {},
    activityList: [],
    autoplay: false,
    videoUrl: '',
    curIndex: 0,
    totalLen: 0,
    isStart: 0, //判断当前是否在训练状态
  },

  bindplay: function(event) {
    this.setData({isStart: 1})
  },

  bindpause: function (event) {
    this.setData({ isStart: 0 })
  },
  //开始训练
  start: function(event) {
    var isStart = 1;
    
    this.setData({
      isStart: isStart,
      videoUrl: this.data.activityList[0].videoUrl, //有缓存设置无效
      autoplay: true,
    })
  },

  //结束训练
  end: function (event) {
    var curIndex = this.data.curIndex;
    var len = this.data.activityList.length;
    console.log("curIndex:" + curIndex);
    console.log("len:" + len);
    if(curIndex == len-1) {
      //如果是最后一个动作，则默认已完成动作,用200运动币作为奖励
      wx.showToast({
        title: '获得200运动币',
        icon: 'success',
        duration: 2000,
      })
      setTimeout(function () {
        //重定向到首页
        wx.switchTab({
          url: '../exercise/exercise',
        })
      }, 2000)
      var that = this;
      //更新运动币数量
      wx.request({
        url: urlPath + 'coinTotal/updateCoinExercise',
        data: {
          token: app.globalData.token,
          beizhu: that.data.courseInfo.courseName,

        },
        success: function(res) {
            console.log("更新运动币成功");
        }
      })

      

      this.setData({autoplay: false, });
      console.log("训练完成，获得200运动币");
    }else {
      //训练没结束的时候退出
      wx.showModal({
        title: '退出',
        content: '当前训练未完成，是否退出',
        success(res) {
          if (res.confirm) {
            console.log('用户点击确定')
            //重定向到首页
            wx.switchTab({
              url: '../exercise/exercise',
            })
          } else if (res.cancel) {
            console.log('用户点击取消')
          }
        }
      })
    }
  },

  //播放用户点击的动作
  bindcurUrl: function(event) {
      var index = event.currentTarget.dataset.index;
      var curIndex = index;
    this.setData({
      videoUrl: this.data.activityList[curIndex].videoUrl,
      autoplay: true,
      curIndex: curIndex,
    })
  },
//判断当前播放的视频结束后，继续播放下一个
  bindended: function(event) {
    
    var curIndex = this.data.curIndex + 1;
    // console.log("curIndex" + curIndex);
    var totalLen = this.data.totalLen;
    if(curIndex < totalLen){
      this.setData({
        videoUrl: this.data.activityList[curIndex].videoUrl,
        autoplay: true,
        curIndex: curIndex,
      })
    }
    
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var courseInfo = wx.getStorageSync('courseInfo');
    var activityList = wx.getStorageSync('activityList');
    var len = activityList.length;
    this.setData({courseInfo: courseInfo, activityList: activityList,
    videoUrl: activityList[0].videoUrl,
    totalLen: len})
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