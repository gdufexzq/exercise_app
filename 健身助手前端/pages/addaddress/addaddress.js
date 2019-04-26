// pages/addaddress/addaddress.js
const app = getApp();   
var area = require('../../utils/area.js')

var areaInfo = [];//所有省市区县数据

var provinces = [];//省

var citys = [];//城市

var countys = [];//区县

var index = [0, 0, 0];

var cellId;

var t = 0;
var show = false;
var moveY = 200;

Page({

  /**
   * 页面的初始数据
   */
  data: {
    show: show,
    provinces: provinces,
    citys: citys,
    countys: countys,
    value: [0, 0, 0],
    shouhuo_address: '',
  },
  //滑动事件
  bindChange: function (e) {
    var val = e.detail.value
    // console.log(e)
    //判断滑动的是第几个column
    //若省份column做了滑动则定位到地级市和区县第一位
    if (index[0] != val[0]) {
      val[1] = 0;
      val[2] = 0;
      getCityArr(val[0], this);//获取地级市数据
      getCountyInfo(val[0], val[1], this);//获取区县数据
    } else {    //若省份column未做滑动，地级市做了滑动则定位区县第一位
      if (index[1] != val[1]) {
        val[2] = 0;
        getCountyInfo(val[0], val[1], this);//获取区县数据
      }
    }
    index = val;

    console.log(index + " => " + val);

    //更新数据
    this.setData({
      value: [val[0], val[1], val[2]],
      province: provinces[val[0]].name,
      city: citys[val[1]].name,
      county: countys[val[2]].name
    })
    console.log(provinces[val[0]].name);//省
    console.log(citys[val[1]].name);//市
    console.log(countys[val[2]].name);//区
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    cellId = options.cellId;
    var that = this;
    var date = new Date()
    console.log(date.getFullYear() + "年" + (date.getMonth() + 1) + "月" + date.getDate() + "日");

    //获取省市区县数据
    area.getAreaInfo(function (arr) {
      areaInfo = arr;
      //获取省份数据
      getProvinceData(that);
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    this.animation = wx.createAnimation({
      transformOrigin: "50% 50%",
      duration: 0,
      timingFunction: "ease",
      delay: 0
    }
    )
    this.animation.translateY(200 + 'vh').step();
    this.setData({
      animation: this.animation.export(),
      show: show
    })
  },
  //移动按钮点击事件
  translate: function (e) {
    if (t == 0) {
      moveY = 0;
      show = false;
      t = 1;
    } else {
      moveY = 200;
      show = true;
      t = 0;
    }
    // this.animation.translate(arr[0], arr[1]).step();
    animationEvents(this, moveY, show);

  },
  //隐藏弹窗浮层
  hiddenFloatView_cancle(e) {
    console.log(e);
    moveY = 200;
    show = true;
    t = 0;
    animationEvents(this, moveY, show);
    //初始化
    getCityArr(0, this);
    getCountyInfo(0, 0, this);
    this.setData({
      province: "北京市",
      city: "市辖区",
      county: "东城区",
    })
    console.log(this.data.province);//省
    console.log(this.data.city);//市
    console.log(this.data.county);//区
    this.setData({ shouhuo_address: '' });
  },
  hiddenFloatView_confirm(e) {
    console.log(e);
    moveY = 200;
    show = true;
    t = 0;
    animationEvents(this, moveY, show);
    console.log(this.data.province);//省
    console.log(this.data.city);//市
    console.log(this.data.county);//区
    var address = this.data.province + this.data.city + this.data.county;
    this.setData({ shouhuo_address: address });
  },
  bindsave: function(event) {
    var that = this;
    console.log(1);
    console.log(JSON.stringify(event));
    if(event.detail.value.addressName == ''){
      wx.showToast({
        title: '请填写收货人姓名',
        icon: 'none'
      })
    }
    if (event.detail.value.addressPhone == '') {
      wx.showToast({
        title: '请填写收货人电话',
        icon: 'none'
      })
    }
    if (event.detail.value.addressPCC == '') {
      wx.showToast({
        title: '请填写收货人地址',
        icon: 'none'
      })
    }
    if (event.detail.value.addressDetail == '') {
      wx.showToast({
        title: '请填写收货人详细地址',
        icon: 'none'
      })
    }

    wx.request({
      url: app.globalData.urlPath + 'address/addAddress',
      data: {   
        token: app.globalData.token,
        addressName: event.detail.value.addressName,
        addressPhone: event.detail.value.addressPhone,
        addressPCC: event.detail.value.addressPCC,
        addressDetail: event.detail.value.addressDetail,
      },
      success: function(res) {
        wx.showToast({
          title: '添加地址成功',
          icon: 'none',
        })
      }
    });

    wx.navigateTo({
      url: '../addresslist/addresslist',
    });

  },
  //页面滑至底部事件
  onReachBottom: function () {
    // Do something when page reach bottom.
  },
  tiaozhuan() {
    wx.navigateTo({
      url: '../../pages/modelTest/modelTest',
    })
  }
})
//动画事件
function animationEvents(that, moveY, show) {
  console.log("moveY:" + moveY + "\nshow:" + show);
  that.animation = wx.createAnimation({
    transformOrigin: "50% 50%",
    duration: 400,
    timingFunction: "ease",
    delay: 0
  }
  )
  that.animation.translateY(moveY + 'vh').step()

  that.setData({
    animation: that.animation.export(),
    show: show
  })

}
// ---------------- 分割线 ---------------- 

//获取省份数据
function getProvinceData(that) {
  var s;
  provinces = [];
  var num = 0;
  for (var i = 0; i < areaInfo.length; i++) {
    s = areaInfo[i];
    if (s.di == "00" && s.xian == "00") {
      provinces[num] = s;
      num++;
    }
  }
  that.setData({
    provinces: provinces
  })

  //初始化调一次
  getCityArr(0, that);
  getCountyInfo(0, 0, that);
  that.setData({
    province: "北京市",
    city: "市辖区",
    county: "东城区",
  })

}

// 获取地级市数据
function getCityArr(count, that) {
  var c;
  citys = [];
  var num = 0;
  for (var i = 0; i < areaInfo.length; i++) {
    c = areaInfo[i];
    if (c.xian == "00" && c.sheng == provinces[count].sheng && c.di != "00") {
      citys[num] = c;
      num++;
    }
  }
  if (citys.length == 0) {
    citys[0] = { name: '' };
  }

  that.setData({
    city: "",
    citys: citys,
    value: [count, 0, 0]
  })
}

// 获取区县数据
function getCountyInfo(column0, column1, that) {
  var c;
  countys = [];
  var num = 0;
  for (var i = 0; i < areaInfo.length; i++) {
    c = areaInfo[i];
    if (c.xian != "00" && c.sheng == provinces[column0].sheng && c.di == citys[column1].di) {
      countys[num] = c;
      num++;
    }
  }
  if (countys.length == 0) {
    countys[0] = { name: '' };
  }
  that.setData({
    county: "",
    countys: countys,
    value: [column0, column1, 0]
  })
}