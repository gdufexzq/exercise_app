// pages/goodcar/goodcar.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    total_price_quanxuan: 0, //便于全选功能的商品总价的获取
    goodcar:[],
    total_price: 0,
    isChoose: [], //判断每个商品是否被选中
    isChooseUrl: [
      // '../../resources/images/Ic_Goodcar_Focus.png'
      ],
    chooseAllUrl: '../../resources/images/Ic_Goodcar_Focus.png', //全选
    isChooseAll: true, //默认全选
  },
  jiesuan: function(){
    wx.setStorageSync('goodcar', this.data.goodcar);
    wx.setStorageSync('isChoose', this.data.isChoose);
    wx.setStorageSync('isGoodcar', 1);
    wx.navigateTo({
      url: '../orderconfirm/orderconfirm?totalPrice='+this.data.total_price,
    })
  },
  chooseAll: function(event) {
    var isChooseAll = !this.data.isChooseAll;
    var len = this.data.isChoose.length;
    var isChoose = new Array(len);
    var isChooseUrl = new Array(len);
    if(isChooseAll) { //全部选中
      for(var i=0; i<len; i++) {
        isChoose[i] = true;
        isChooseUrl[i] = '../../resources/images/Ic_Goodcar_Focus.png';
      }
      var total_price = this.data.total_price_quanxuan;
      this.setData({ total_price: total_price, isChoose: isChoose, 
      isChooseUrl: isChooseUrl, isChooseAll: isChooseAll,
        chooseAllUrl: '../../resources/images/Ic_Goodcar_Focus.png'})
    }else {
      for (var i = 0; i < len; i++) {
        isChoose[i] = false;
        isChooseUrl[i] = '../../resources/images/Ic_Goodcar_Normal.png';
      }
      
      this.setData({
        total_price: 0, isChoose: isChoose,
        isChooseUrl: isChooseUrl,
        isChooseAll: isChooseAll,
        chooseAllUrl: '../../resources/images/Ic_Goodcar_Normal.png'
      })
    }
  },
  productChoose: function(event){
    console.log(JSON.stringify(event));
    var index = event.currentTarget.dataset.index;
    //通过编号获取商品的价格
    var price = this.data.goodcar[index].productPrice;
    console.log("price:" + price);
    var count = this.data.goodcar[index].productCount;
    
    var isChoose = !this.data.isChoose[index];
    var chooses = 'isChoose[' + index + ']';
    this.setData({[chooses]: isChoose});
    if(isChoose){
      var productPrice = this.data.total_price + price * count;
      var chooseUrl = 'isChooseUrl[' + index + ']';
      this.setData({ [chooseUrl]:'../../resources/images/Ic_Goodcar_Focus.png',
        total_price: productPrice,
      });
    }else{
      var productPrice = this.data.total_price - price * count;
      var chooseUrl = 'isChooseUrl[' + index + ']';
      this.setData({
        [chooseUrl]: '../../resources/images/Ic_Goodcar_Normal.png',
        total_price: productPrice,
    })
  }
  },
  reduceCount: function(event) {
    var index = event.currentTarget.dataset.index;
    console.log("index:" + index);
    //通过编号获取商品的价格
    var price = this.data.goodcar[index].productPrice;
    console.log("price:" + price);
    var count = this.data.goodcar[index].productCount;
    var goodcarId = this.data.goodcar[index].id;
    var productPrice = this.data.total_price - price;
    var productPriceAll = this.data.total_price_quanxuan - price;
    var productCountName = 'goodcar[' + index + '].productCount';//要有单引号
    var productCount = this.data.goodcar[index].productCount - 1;
    if(productCount < 1) {
      productCount = 1; //最少为1
    }
    else {
      this.setData({ [productCountName]: productCount, total_price: productPrice,
        total_price_quanxuan: productPriceAll,
         })
      console.log("count:" + this.data.goodcar[index].productCount);
      wx.request({
        url: app.globalData.urlPath + 'goodcar/updateCount',
        data: {
          token: app.globalData.token,
          count: count - 1,
          goodcarId: goodcarId,
        },
        success: function (res) {
          console.log("减少数量成功");
        }
      })
    }
    

  },
  addCount: function(event){
    var index = event.currentTarget.dataset.index;
    console.log("index:" + index);
    //通过编号获取商品的价格
    var price = this.data.goodcar[index].productPrice;
    console.log("price:" + price);
    var count = this.data.goodcar[index].productCount;
    var goodcarId = this.data.goodcar[index].id;
    console.log("id:" + goodcarId);
    var productPrice = this.data.total_price + price;
    var productPriceAll = this.data.total_price_quanxuan + price;
    var productCountName = 'goodcar[' + index + '].productCount';//要有单引号
    var productCount = this.data.goodcar[index].productCount + 1;
    console.log("pre:" + productCount);
    this.setData({ [productCountName]: productCount, total_price: productPrice,
      total_price_quanxuan: productPriceAll,
     });
    console.log("cur:" + [productCountName]);
    console.log("cur-:" + this.data.goodcar[index].productCount);//修改数组的值不起作用
    wx.request({
      url: app.globalData.urlPath + 'goodcar/updateCount',
      data: {
        token: app.globalData.token,
        count: count + 1,
        goodcarId: goodcarId,
      },
      success: function (res) {
        console.log("增加数量成功");
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      var that = this;
      wx.request({
      url: app.globalData.urlPath + 'goodcar/goodcarInfo',
      data: {
        token: app.globalData.token,
      },

      success: function(res){
        console.log("goodcar: " + JSON.stringify(res))
        var len = res.data[0].length;
        var urls = new Array(len);
        var chooses = new Array(len);
        for(var i = 0;i<len;i++){
          urls[i] = '../../resources/images/Ic_Goodcar_Focus.png';
          chooses[i] = true;
        }
        that.setData({ goodcar: res.data[0], total_price: res.data[1],
          isChooseUrl: urls, isChoose: chooses,
          total_price_quanxuan: res.data[1],});
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
    this
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