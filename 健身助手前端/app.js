

App({
  queryCoin: function () {
    var that = this;
    wx.request({
      url: that.globalData.urlPath + 'coinDetail/todayCoinDetail',
      data: {
        token: that.globalData.token,
      },

      success: function(res){
        console.log(JSON.stringify(res));
        that.globalData.today_coin = res.data;
        console.log("test:" + that.globalData.today_coin);
      }

    });
    wx.request({
      url: that.globalData.urlPath + 'coinTotal/totalCoin',
      data: {
        token: that.globalData.token,
      },

      success: function (res) {
        console.log(res.data);
        that.globalData.total_coin = res.data;
      }
    });


  },
  getToken: function() {
    var that = this;
    // 查看是否授权
    wx.login({
      success: function (res) {
        console.log(res);
        // that.setData({code: res.code})
        that.globalData.code = res.code;
        wx.request({
          url: that.globalData.urlPath + '/user/getToken',
          data: {
            code: that.globalData.code,
          },
          success: function(res) {
            that.globalData.token = res.data;
            console.log("app bind token:" + that.globalData.token);
          }
        })
      }
    });

  },
  onLaunch: function () {
    var that = this;

  this.getToken();
    //判断是否授权
    wx.getSetting({
      success: function (res) {
        if (res.authSetting['scope.userInfo']) {
          wx.getUserInfo({
            withCredentials: true,
            lang: 'zh_CN',
            success: function (res) {
              console.log("ddsfsd")
              //用户已经授权
              that.globalData.userInfo = res.userInfo;
              
              
              setTimeout(function() {
                that.queryCoin();
                console.log("timeout");
                wx.switchTab({
                  url: '/pages/exercise/exercise'
                })
              }, 2000);
             
            }
          });
        } else {

          //用户还未授权
          wx.reLaunch({
            url: '/pages/index/index'
          })
        }
      }
    })
 



  //   // 展示本地存储能力
  //   var logs = wx.getStorageSync('logs') || []
  //   logs.unshift(Date.now())
  //   wx.setStorageSync('logs', logs)

  //   // 登录
  //   wx.login({
  //     success: res => {
  //       // 发送 res.code 到后台换取 openId, sessionKey, unionId
  //     }
  //   })
  //   // 获取用户信息
  //   wx.getSetting({
  //     success: res => {
  //       if (res.authSetting['scope.userInfo']) {
  //         // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
  //         wx.getUserInfo({
  //           success: res => {
  //             // 可以将 res 发送给后台解码出 unionId
  //             this.globalData.userInfo = res.userInfo

  //             // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
  //             // 所以此处加入 callback 以防止这种情况
  //             if (this.userInfoReadyCallback) {
  //               this.userInfoReadyCallback(res)
  //             }
  //           }
  //         })
  //       }
  //     }
  //   })
  },
  globalData: {
    userInfo: null,
    urlPath: 'https://ruanjiangongcheng2.xyz:8090/exercise_app/',
    // urlPath: 'http://192.168.137.1:8080/exercise_app/',//该地址需要电脑开移动热点
    // urlPath: 'http://localhost:8090/exercise_app/',
    hasUserInfo: true,
    token: '',
    total_coin: 0,
    today_coin: 0,
    total_exercise: 0,
  }
})