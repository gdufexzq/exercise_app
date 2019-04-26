const app = getApp();
Page({
  data: {
    //判断小程序的API，回调，参数，组件等是否在当前版本可用。
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    
  },
  onLoad: function () {
    
    //判断是否授权
    wx.getSetting({
      success: function (res) {
        if (res.authSetting['scope.userInfo']) {
          wx.getUserInfo({
            withCredentials: true,
            lang: 'zh_CN',
            success: function (res) {
              console.log("userinfo:" + JSON.stringify(res.userInfo));
              setTimeout(function(){
                //用户已经授权
                app.globalData.userInfo = res.userInfo;
                console.log("index token:" + app.globalData.token);
                app.queryCoin();
                wx.switchTab({
                  url: '/pages/exercise/exercise'
                })
              },2000);
             
            }
          });
        }
      }
    })
  },
  bindGetUserInfo: function (e) {
    if (e.detail.userInfo) {
      console.log(e);
      //用户按了允许授权按钮
      var that = this;
      //设置头像显示为true
      app.globalData.hasUserInfo = true;
      //一次性的数据
      // app.globalData.encryptedData = e.detail.encryptedData;
      // app.globalData.iv = e.detail.iv;
      //插入登录的用户的相关信息到数据库
      wx.request({
        url: app.globalData.urlPath + 'user/addUserInfo',
        data: {
          encryptedData: e.detail.encryptedData,
          iv: e.detail.iv,
          token: app.globalData.token,
        },
        header: {
          'content-type': 'application/json'
        },
        success: function (res) {

          wx.getUserInfo({
            withCredentials: true,
            lang: 'zh_CN',
            success: function (res) {
              console.log("get ..userinfo:" + JSON.stringify(res.userInfo));
              app.globalData.userInfo = res.userInfo;
            }
          });
          console.log("index bind token:" + app.globalData.token);
          app.queryCoin();
          // console.log("token:" + res.data);
          app.globalData.token = res.data;
          // console.log(app.globalData);
          //授权成功后，跳转进入小程序首页
          wx.switchTab({
            url: '/pages/exercise/exercise'
          })
          console.log("插入小程序登录用户信息成功！");
        }
      });
    } else {
      //用户按了拒绝按钮
      wx.showModal({
        title: '警告',
        content: '您点击了拒绝授权，将无法进入小程序，请授权之后再进入!!!',
        showCancel: false,
        confirmText: '返回授权',
        success: function (res) {
          if (res.confirm) {
            console.log('用户点击了“返回授权”')
          }
        }
      })
    }
  },

})
