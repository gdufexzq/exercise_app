
// pages/exercise/exercise.js
const app = getApp();
const urlPath = app.globalData.urlPath;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    
    courseAddUrl: '../../resources/images/Ic_Course_Add.png',
    hasUserInfo: true,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    cid: 2,
    articleCategoryList: [{ "id": 1, name: "计划" }, { "id": 2, name: "首页" }, { "id": 3, name: "健身" }, { "id": 4, name: "跑步" }, { "id": 5, name: "瑜伽" }, { "id": 6, name: "行走" }, { "id": 7, name: "骑行" }],
    total_coin: app.globalData.total_coin,
     today_coin: app.globalData.today_coin,
    myCourseInfoList: [],
  },
  selectType(event) {
    var cid = event.target.dataset.cid;
    this.setData({
      cid: cid
    })
  },
  bindcoin: function(event) {
    wx.navigateTo({
      url: '../coinrecord/coinrecord',
      success: function (res) {
        console.log("coinrecord 跳转成功")
      },

    })
  },
  bindCategory: function(event) {
    var cid = event.target.dataset.cid;
    console.log("cid:" + cid);
    if(cid == 1){
      wx.navigateTo({
        url: '../plan/plan',
        success: function(res) {
          console.log("plan 跳转成功")
        },
       
      })
    }
    if (cid == 2) {
      wx.navigateTo({
        url: '../exercise/exercise',
        success: function (res) { console.log(" exercise跳转成功"); },
      })
    }
    if (cid == 3) {
      wx.navigateTo({
        url: '../exerciseList/exerciseList',
        success: function (res) { console.log(" exerciseList跳转成功") },
        
      })
    }
    if (cid == 4) {
      wx.navigateTo({
        url: '../exercise/exercise',
        success: function (res) { console.log("exercise跳转成功") },

      })
    }
  },
  bindViewTap: function () {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  daka: function() {
    wx.navigateTo({
      url: '../daka/daka',
    })
  },
  getUserInfo: function (e) {
    console.log(e)
    app.globalData.userInfo = e.detail.userInfo
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  },
  /**
   * 由于token有时拿不到，需要等待
   */
  waitToken: function() {
    var res = new Promise(function(resolve, reject) {
      var interval = setInterval(function() {
        console.log("exercise interval token:" + app.globalData.token);
          if(app.globalData.token != undefined && app.globalData.token != ""){
            //清除定时器
            clearInterval(interval);
            //resolve相当于回调
            resolve();
          }
      }, 500);
    });
    return res;
  },
  bindAllCourse: function(event) {
    wx.navigateTo({
      url: '../exercisedetaillist/exercisedetaillist',
    })
  },
  bindCourse: function (event) {
    wx.navigateTo({
      url: '../mycourse/mycourse',
    })
  },
  bindOneCourse: function (event) {
    console.log(JSON.stringify(event))
    var index = event.currentTarget.dataset.index;
    var cid = this.data.myCourseInfoList[index].id;
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

    this.waitToken().then(function(){
      wx.getSetting({
        success: function(res) {
          if(res.authSetting['scope.userInfo']){
            wx.getUserInfo({
              withCredentials: true,
              lang: 'zh_CN',
              success: function (res) {
                console.log("exercise onload token:" + app.globalData.token);
                app.queryCoin();
              }

            })
          }
        }
      })
      wx.showLoading({
        title: '加载中',
        mask: true,
      })

      setTimeout(function () {
        wx.hideLoading()
        if (app.globalData.userInfo) {
          that.setData({
            userInfo: app.globalData.userInfo,
            hasUserInfo: true
          })
        } else if (that.data.canIUse) {
          // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
          // 所以此处加入 callback 以防止这种情况
          app.userInfoReadyCallback = res => {
            that.setData({
              userInfo: res.userInfo,
              hasUserInfo: true
            })
          }
        } else {
          // 在没有 open-type=getUserInfo 版本的兼容处理
          wx.getUserInfo({
            success: res => {
              app.globalData.userInfo = res.userInfo
              that.setData({
                userInfo: res.userInfo,
                hasUserInfo: true
              })
            }
          })
        }
      }, 2000)
     
      
    }).then(function(){

      wx.request({
        url: urlPath + 'courseRecord/myThreeCourseInfo',
        data: {
          token: app.globalData.token,
        },
        success: function (res) {
          var myCourseInfoList = res.data;
          that.setData({ myCourseInfoList: myCourseInfoList })
        }
      })
      that.setData({
        total_coin: app.globalData.total_coin,
        today_coin: app.globalData.today_coin,
      })
      console.log("log....")
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
    this.setData({
      total_coin: app.globalData.total_coin,
      today_coin: app.globalData.today_coin,
    })
    console.log("exercise show...")
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