const app = getApp();
// pages/productmessage/productmessage.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    // showModalStatus: false,
    templateData: {
      showModalStatus: false,
      productColor: [],
      productType: [],
      productImage: '',
    },
    productImage: [

    ],
    currentSwiper: 0,
    autoplay: true,
    productInfo: {},
    //接收购物车传过来的数据
    count: 1,
    type: '',
    color: '',
    select1: -1,
    select2: -1,
    productId: '',
  },
  swiperChange(e) {
    const that = this;
    that.setData({
      currentSwiper: e.detail.current,
    });

  },

  /**
   * 弹框
   * /
   * */

  bindColor(event) {
    
    var index = event.currentTarget.dataset.index;
    var color = this.data.templateData.productColor[index].productColor;
    console.log(JSON.stringify(this.data.templateData.productColor[index]));
    var selected = event.target.dataset.index;
    this.setData({
      color: color,
      select1: selected,
    })
    console.log("color:" + color);
  },
  bindType(event) {
    var index = event.currentTarget.dataset.index;
    var type = this.data.templateData.productType[index].productType;
    
    var selected = event.target.dataset.index;
    this.setData({
      type: type,
      select2: selected,
    })
    console.log("type:" + type);
  },

  bindjian(event) {
    if (this.data.count == 1) {
      this.setData({
        count: 1
      })
    } else {
      this.setData({
        count: count - 1
      })
    }
    console.log("count:" + this.data.count);
  },

  bindAdd(event) {
    this.setData({
      count: this.data.count + 1
    })
    console.log("count:" + this.data.count);
  },

/**
 * 跳转到购物车界面
 */
bindGoodCar: function(event){
  wx.navigateTo({
    url: '../goodcar/goodcar',
  })
  },
  /**
   * 跳转到订单确认界面
   */
  bindOrder: function(event){
    if (this.data.type == '' || this.data.color == '') {
      wx.showToast({
        title: '请选择颜色规格',
        icon: 'none',
        duration: 2000
      })
    } else {
      var productToOrder = this.data.productInfo;
      productToOrder.color = this.data.color;
      productToOrder.type = this.data.type;
      productToOrder.count = this.data.count;
      wx.setStorageSync('productToOrder', productToOrder);
      wx.setStorageSync('isGoodcar', 0);
      var that = this;
      wx.navigateTo({
        url: '../orderconfirm/orderconfirm?productId=' + this.data.productInfo.productId,
        data: {
          productId: that.data.productId,
        }
      })
    }
    
  },
  /**
   * 加入购物车
   */
  bindAddGoodCar: function(event) {
    if (this.data.type == '' || this.data.color == '') {
      wx.showToast({
        title: '请选择颜色规格',
        icon: 'none',
        duration: 2000
      })
    } else {
      var that = this;
      wx.request({
        url: app.globalData.urlPath + 'goodcar/goodcarAdd',
        data: {
          token: app.globalData.token,
          productId: that.data.productId,
          color: that.data.color,
          type: that.data.type,
          count: that.data.count,
        },
        success: function(res) {
          wx.showToast({
            title: '已加入购物车',
            icon: 'success',
            duration: 3000
          })
        }
      })
    }

  },
  /**
   * 点击选择
   */
  choose(e) {
    
    this.setData({
      'templateData.showModalStatus': true,
    })
  },

  /**
   * 取消
   */
  cancel: function(e) {
    var currentStatu = e.currentTarget.dataset.statu;
    this.setData({
      color: '',
      type: '',
    })
    this.colorType(currentStatu)
  },
  confirm(e) {
    var currentStatu = e.currentTarget.dataset.statu;
    this.colorType(currentStatu);
    var that = this;
    // wx.showLoading({
    //   title: '加载中',
    //   mask: true
    // })

    // setTimeout(function() {
    //   wx.hideLoading()
    // }, 1000)
  },

  colorType: function(currentStatu) {
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
    var self = this;
    // 第5步：设置定时器到指定时候后，执行第二组动画 
    setTimeout(function() {
      // 执行第二组动画 
      animation.opacity(1).rotateX(0).step();
      // 给数据对象储存的第一组动画，更替为执行完第二组动画的动画对象 
      this.setData({
        animationData: animation
      })

      //关闭 
      if (currentStatu == "close") {
        this.setData({
          'templateData.showModalStatus': false
        });
      }
    }.bind(this), 200)

    // 显示 
    if (currentStatu == "open") {
      this.setData({
        'templateData.showModalStatus': true
      });
    }
  },




  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    var that = this;
    var imageurl = options.productImage;
    console.log("productId:" + options.productId);
    var productId = options.productId;
    that.setData({
      productId: productId
    });
    wx.request({
      url: app.globalData.urlPath + 'product/productInfo',
      data: {
        token: app.globalData.token,
        productId: options.productId,
      },
      success: function(res) {
        console.log(JSON.stringify(res));
        that.setData({
          productImage: res.data[1],
          productInfo: res.data[0],
          'templateData.productColor': res.data[2],
          'templateData.productType': res.data[3],
          'templateData.productImage': imageurl
        });
      },
    })
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