// pages/orderconfirm/orderconfirm.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    productImages: [],
    totalPrice: 0,
    isChoose: [],
    totalCount: 0,
    //商品详情信息
    productDetail: [],
    //收货地址
    addressInfo: null,
  },
  /**
   * 提交订单信息
   */
  confirm_submit: function(event) {
    if (this.data.addressInfo == null) {
      wx.showToast({
        title: '请填写收货信息',
        icon: 'none',
      })

    } else {
      var isGoodcar = wx.getStorageSync('isGoodcar');

      if (isGoodcar == 1) {
        //购物车生成的订单
        console.log(JSON.stringify(this.data.addressInfo));
        var addressInfo = this.data.addressInfo;
        var productOrderDetail = wx.getStorageSync('productOrderDetail');
        console.log('goodcar:' + JSON.stringify(productOrderDetail));
        var that = this;
        // var orderId = res.data;
        wx.showModal({
          title: '付款',
          content: '确认付款',
          success: function(res) {
            console.log("res1:" + JSON.stringify(res.data));
            if (res.confirm) {
              console.log('购物车弹框后点确定')
              wx.request({
                url: app.globalData.urlPath + 'order/goodcarAddOrder',
                data: {
                  token: app.globalData.token,
                  addressId: addressInfo.id,
                  totalPrice: that.data.totalPrice,
                  productOrderDetail: productOrderDetail,
                },
                fail: function(res){
                    console.log("fail:" + JSON.stringify(res));
                },
                success: function(res) {
                  console.log("res2:" + JSON.stringify(res.data));
                  var orderId = res.data;
                  wx.request({
                    url: app.globalData.urlPath + 'order/goodcarUpdateTotalCoin',
                    data: {
                      token: app.globalData.token,
                      totalPrice: that.data.totalPrice,
                      productOrderDetail: productOrderDetail,

                    },
                    success: function(res) {
                      if (res.data == 1) {
                        //修改订单的状态
                        wx.request({
                          url: app.globalData.urlPath + 'order/updateOrderStatus',
                          data: {
                            token: app.globalData.token,
                            orderId: orderId,
                          },
                          success: function(res) {
                            wx.switchTab({
                              url: '../exercise/exercise',
                            });
                            console.log("修改订单状态成功")
                          }
                        })
                      } else {
                        wx.showToast({
                          title: '运动币不足',
                          icon: 'none',
                        })
                      }

                    }
                  })

                }
              });

            } else {
              console.log('弹框后点取消')
              wx.request({
                url: app.globalData.urlPath + 'order/goodcarAddOrder',
                data: {
                  token: app.globalData.token,
                  addressId: addressInfo.id,
                  productOrderDetail: productOrderDetail,
                  totalPrice: that.data.totalPrice,
                },
                success: function(res) {}
              });
            }
          }
        })
      } else {
        //商品生成的订单
        console.log(JSON.stringify(this.data.addressInfo));
        var addressInfo = this.data.addressInfo;
        var productOrderDetail = wx.getStorageSync('productOrderDetail');
        console.log(JSON.stringify(productOrderDetail));
        var that = this;
        // var orderId = res.data;
        // console.log('orderId:' + JSON.stringify(res.data));
        var totalPrice = that.data.totalPrice;
        wx.showModal({
          title: '付款',
          content: '确认付款',
          success: function(res) {
            if (res.confirm) {
              console.log('弹框后点确定')
              wx.request({
                url: app.globalData.urlPath + 'order/productAddOrder',
                data: {
                  token: app.globalData.token,
                  addressId: addressInfo.id,
                  productId: productOrderDetail[0].id,
                  color: productOrderDetail[0].color,
                  type: productOrderDetail[0].type,
                  count: productOrderDetail[0].count,
                  totalPrice: that.data.totalPrice,
                  productName: productOrderDetail[0].productName,
                  productImage: productOrderDetail[0].productImage,
                  productPrice: productOrderDetail[0].productPrice,

                },
                success: function(res) {
                  var orderId = res.data;
                  //付款
                  wx.request({
                    url: app.globalData.urlPath + 'order/productUpdateTotalCoin',
                    data: {
                      token: app.globalData.token,
                      totalPrice: totalPrice,
                      productId: productOrderDetail[0].id,
                      color: productOrderDetail[0].color,
                      type: productOrderDetail[0].type,
                      count: productOrderDetail[0].count,
                    },
                    success: function(res) {

                      if (res.data == 1) {

                        //修改订单的状态
                        wx.request({
                          url: app.globalData.urlPath + 'order/updateOrderStatus',
                          data: {
                            token: app.globalData.token,
                            orderId: orderId,
                          },
                          success: function(res) {
                            console.log("修改订单状态成功")

                            wx.switchTab({
                              url: '../exercise/exercise',
                            });
                          }
                        })
                      } else {
                        wx.showToast({
                          title: '运动币不足',
                          icon: 'none',
                        })
                      }
                    }
                  });
                }
              });
            } else {
              console.log('弹框后点取消')
              wx.request({
                url: app.globalData.urlPath + 'order/productAddOrder',
                data: {
                  token: app.globalData.token,
                  addressId: addressInfo.id,
                  productId: productOrderDetail[0].id,
                  color: productOrderDetail[0].color,
                  type: productOrderDetail[0].type,
                  count: productOrderDetail[0].count,
                  totalPrice: that.data.totalPrice,
                  productName: productOrderDetail[0].productName,
                  productImage: productOrderDetail[0].productImage,
                  productPrice: productOrderDetail[0].productPrice,

                },
                success: function(res) {}
              });
            }
          }
        })

      }
    }
  },
  /**
   * 查看商品详情
   */
  productOrderDetail: function(event) {
    //使用productDetail参数
    wx.navigateTo({
      url: '../productOrderDetail/productOrderDetail',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    // console.log("orderconfirm:" + JSON.stringify(options));

    // console.log("goodcar---:" + JSON.stringify(goodcar));
    var that = this;
    var productId = options.productId;
    var addressId = options.addressId;
    if (productId == undefined && addressId == undefined) {
      //购物车生成的订单
      // var isChoose = options.isChoose;
      // console.log(isChoose[3]);
      var totalPrice = options.totalPrice;
      console.log(totalPrice);
      //获取数据
      // var totalPrice = wx.getStorageSync('totalPrice');
      var goodcar = wx.getStorageSync('goodcar');
      var isChoose = wx.getStorageSync('isChoose');
      console.log("test: " + isChoose);
      var imagelen = 0;
      for (var i = 0; i < len; i++) {
        if (isChoose[i]) {
          imagelen = imagelen + 1;
        }
      }
      var len = goodcar.length;
      var count = 0;
      var productImages = new Array(imagelen);
      var productDetail = new Array(imagelen);
      for (var i = 0, j = 0; i < len; i++) {
        //统计数量
        if (isChoose[i]) {
          console.log("isChoose....");
          count = count + goodcar[i].productCount;
          productImages[j] = goodcar[i].productImage;
          productDetail[j] = goodcar[i];
          //为了跟一个订单的对应
          productDetail[j].color = goodcar[i].productColor;
          productDetail[j].type = goodcar[i].productType;
          productDetail[j].count = goodcar[i].productCount;
          j++;
        }

      }
      console.log("orderconfirm-producgDetail" + JSON.stringify(productDetail));
      wx.setStorageSync('productOrderDetail', productDetail);
      wx.setStorageSync('orderconfirm_totalPrice', totalPrice);
      wx.setStorageSync('orderconfirm_totalCount', count);
      wx.setStorageSync('orderconfirm_productImages', productImages);
      that.setData({
        totalPrice: totalPrice,
        totalCount: count,
        productImages: productImages
      });

    } else {
      if (addressId == undefined) {
        //这是一个商品生成的订单
        var productToOrder = wx.getStorageSync('productToOrder');
        console.log("productToOrder: " + JSON.stringify(productToOrder));
        var count = productToOrder.count;
        var price = productToOrder.productPrice;
        var totalPrice = count * price;
        var images = new Array(1);
        var productOrderDetail = new Array(1);
        productOrderDetail[0] = productToOrder;
        wx.setStorageSync('productOrderDetail', productOrderDetail);
        images[0] = productToOrder.productImage;
        wx.setStorageSync('orderconfirm_totalPrice', totalPrice);
        wx.setStorageSync('orderconfirm_totalCount', count);
        wx.setStorageSync('orderconfirm_productImages', images);
        that.setData({
          totalPrice: totalPrice,
          totalCount: count,
          productImages: images
        })
      } else {
        //这是地址页面转发来这个页面的
        // 获取之前的页面看是购物车转来的还是单个商品转来的
        var totalPrice = wx.getStorageSync('orderconfirm_totalPrice');
        var totalCount = wx.getStorageSync('orderconfirm_totalCount');
        var productImages = wx.getStorageSync('orderconfirm_productImages');
        var addressInfo = wx.getStorageSync('addressInfo');
        this.setData({
          totalPrice: totalPrice,
          totalCount: totalCount,
          productImages: productImages,
          addressInfo: addressInfo,
        });
      }

    }
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