// pages/daka/daka.
const app = getApp();
const urlPath = app.globalData.urlPath;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    speciallist: [],
    count_txt_user: [
      // {
      //   'count_txt': '红色：',
      //   'count_ber': '未打卡'
      // },
      {
        'count_txt': '橙色：',
        'count_ber': '已打卡'
      }
    ],
    curYear: new Date().getFullYear(), // 年份
    curMonth: new Date().getMonth() + 1, // 月份 1-12
    day: new Date().getDate(), // 日期 1-31 若日期超过该月天数，月份自动增加
    isDaka: false,
    isDetail: false,
    //每周最后一天（的后一天，星期一），检查上周的打卡情况，如果满足7天打卡，则给它额外的100运动币，
    //每月的也是下个月的第一天检查情况。
    detail: [{id: 1, content:"签到可获得运动币10个"},{id: 2, content: "本周打卡连续7天可获得额外100运动币"}, {id: 3, content: "本月全勤可得额外200运动币"}],
  },

  //打开详情提示
  showDetail: function () {
    this.setData({
      isDetail: true
    })
  },
  //关闭详情
  hideDetail: function () {
    this.setData({
      // isDetail: false
    })
  },

  /**
   * 打卡监听方法
   */
  daka: function(options) {
    console.log(options);
    console.log(this.data.day);
    console.log(new Date());
    var that = this;
  wx.request({
    url: urlPath + 'daka/isDaka',
    data: {
      token: app.globalData.token,
    },
    success: function(res){
      var isDaka = res.data;
      that.setData({isDaka: isDaka})
    }

  })
    if(this.data.isDaka == 0) {
      
      wx.request({
        url: app.globalData.urlPath + 'daka/dakaAddInfo',
        data: {
          token: app.globalData.token,
        },
        success: function (res) {
          console.log("sdffdfdsfsddata: " +　JSON.stringify(res));
          that.queryMonth();
          console.log("数据插入数据库成功.");
          wx.setStorageSync('isDaka', true);
          that.setData({isDaka: true})
          wx.showToast({
            title: '获得10运动币',
            icon: 'success',
            duration: 2000
          })
        if(res.week != null) {
          wx.showToast({
            title: '获得110运动币',
            icon: 'success',
            duration: 3000
          })
        }
        if(res.month != null) {
          if(res.week !=null) {
            wx.showToast({
              title: '获得310运动币',
              icon: 'success',
              duration: 3000
            })
          }else {
            wx.showToast({
              title: '获得210运动币',
              icon: 'success',
              duration: 3000
            })
          }
          
        }
          
        }
      })
    } else {
      wx.showToast({
        title: '今日已签到',
        icon: 'none',
        duration: 2000
      })
    }
    app.queryCoin();
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    // this.setData({
    //   speciallist: [
    //     { date: '2019-03-02', background: 'yellow', text: '', color: '' },
    //     { date: '2019-03-05', background: 'red', text: '', },
    //     { date: '2019-03-06', background: 'blue', text: '', },
    //   ], count_txt_user: [
    //     { 'count_txt': '红色：', 'count_ber': '未打卡' },
    //     { 'count_txt': '橙色：', 'count_ber': '已打卡' },
    //   ],
    // })
    console.log(options);
    this.queryMonth();

  },

  queryMonth: function() {
    var that = this;
    //请求数据库显示当前月的打卡记录
    wx.request({
      url: app.globalData.urlPath + 'daka/dakaInfo',
      data: {
        token: app.globalData.token,
        curYear: that.data.curYear,
        curMonth: that.data.curMonth,
        day: that.data.day,
      },

      success: function(res) {
        that.setData({speciallist: res.data});
        console.log(JSON.stringify(res));
         
      }
    });
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
    console.log("daka show..");
    var that = this;
    wx.request({
      url: urlPath + 'daka/isDaka',
      data: {
        token: app.globalData.token,
      },
      success: function (res) {
        var isDaka = res.data;
        that.setData({ isDaka: isDaka })
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