// pages/productmessagechoose/productmessagechoose.js

Page({

  /**
   * 页面的初始数据
   */
  data: {
    showModalStatus: false,
    templateData: {
      showModalStatus: true,
    },
    color: '',
    type: '',
    count: 1,
  },
  /**
  * 
  */
  bindColor(event) {
    var color = event.detail.value;
    this.setData({
      color: color
    })
  },
  bindType(event) {
    var type = event.detail.value;
    this.setData({
      type: type
    })
  },

  bindjian(event){

  },

  bindAdd(event){

  },

  /**
  * 点击选择
  */
  choose(e) {
    var currentStatu = e.currentTarget.dataset.statu;
    this.typeColor(currentStatu);
  },

  /**
  * 确定
  */
  confirm(e) {
    console.log(" ------------------- ");
    var currentStatu = e.currentTarget.dataset.statu;
    this.util(currentStatu);
    var self = this;
    wx.showLoading({
      title: '加载中',
      mask: true
    })

    setTimeout(function () {
      wx.hideLoading()
    }, 1000)

    // 后端请求
    wx.request({
      url: domain + '/user/reward',
      data: {
        token: app.globalData.token,
        toUid: self.data.user.id,
        coin: self.data.coin
      },
      success(res) {
        if (res.data.code == 200) {
          wx.showToast({
            title: '打赏成功',
          })
        } else {
          wx.showToast({
            icon: "none",
            title: res.data.errMsg,
          })
        }
      }
    })
  },

  /**
  * 取消
  */
  cancel: function (e) {
    var currentStatu = e.currentTarget.dataset.statu;
    this.typeColor(currentStatu)
  },

  typeColor: function (currentStatu) {
    /* 动画部分 */
    // 第1步：创建动画实例 
    var animation = wx.createAnimation({
      duration: 200, //动画时长 
      timingFunction: "linear", //线性 
      delay: 0 //0则不延迟 
    });

    // 第2步：这个动画实例赋给当前的动画实例 
    this.animation = animation;

    // 第3步：执行第一组动画 
    animation.opacity(0).rotateX(-100).step();

    // 第4步：导出动画对象赋给数据对象储存 
    this.setData({
      animationData: animation.export()
    })

    // 第5步：设置定时器到指定时候后，执行第二组动画 
    setTimeout(function () {
      // 执行第二组动画 
      animation.opacity(1).rotateX(0).step();
      // 给数据对象储存的第一组动画，更替为执行完第二组动画的动画对象 
      this.setData({
        animationData: animation
      })

      //关闭 
      if (currentStatu == "close") {
        this.setData(
          {
            showModalStatus: false
          }
        );
      }
    }.bind(this), 200)

    // 显示 
    if (currentStatu == "open") {
      this.setData(
        {
          showModalStatus: true
        }
      );
    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

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