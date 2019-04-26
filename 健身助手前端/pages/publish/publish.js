// pages/share/share.js
const app = getApp();
const urlPath = app.globalData.urlPath;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    imgList: [],
    content: "",
    onlyMe: {},
    praiseIcon: '../../resources/images/Ic_Praise_Normal.png',
  },

  /**
   * 获取失去焦点时的值
   */
  bindblur(event) {
    this.setData({
      content: event.detail.value
    });
    console.log("发表的内容: " + event.detail.value);
  },

  /**
   * 发布
   */
  confirmPublish() {
    var that = this;
    if (that.data.content.length <= 0 && that.data.imgList.length <= 0) {
      wx.showToast({
        title: '请输入要分享的内容',
        icon: 'none'
      })
    } else {
      // 请求
      wx.request({
        url: urlPath + '/userContent/publish',
        data: {
          token: app.globalData.token,
          content: that.data.content,
          onlyMe: that.data.onlyMe,
          praiseIcon: that.data.praiseIcon,
        },
        success(res) {
          // 上传图片跟内容分开上传,返回内容的id,
          that.startUpload(res.data);
        }
      })

      var length = that.data.imgList.length;
      var duration = length * 0.6 * 1000;
      wx.showToast({
        title: '正在发表...',
        icon: 'loading',
        duration: duration,
        mask: true,
        success: function() {
          setTimeout(function() {
            wx.showToast({
              title: '获得100运动币',
              icon: 'success',
              duration: 1000,
              success(res) {
                setTimeout(function() {
                  //要延时执行的代码
                  wx.switchTab({
                    url: '/pages/community/community',
                  });
                }, 1000)
              }
            })
          }, duration);
        }
      })
    }
  },

  /**
   * 发表前，删除图片
   */
  deleteSelectedImg(event) {
    var images = this.data.imgList;
    console.log("images:" + JSON.stringify(images));
    //获取图片的index,删除一个
    images.splice(event.currentTarget.dataset.index, 1);
    this.setData({
      imgList: images
    })
  },

  /**
   * 添加图片
   */
  chooseImage() {
    var that = this;
    var count = this.data.imgList.length;
    console.log("count:" + count);
    var maxAddCount = 9 - count;
    console.log(maxAddCount);
    if (maxAddCount > 0) {
      wx.chooseImage({
        count: maxAddCount, //控制最多选择的图片数
        //original	原图
        // compressed	压缩图
        sizeType: ['original', 'compressed'],
        // album	从相册选图
        // camera	使用相机
        sourceType: ['album', 'camera'],
        success(res) {
          // tempFilePath可以作为img标签的临时src属性显示图片，后台自己生成一个url,存入数据库
          const tempFilePaths = res.tempFilePaths;
          //一次可以选择多张，会自动以,分隔图片url,这个url,外部访问不到
          console.log("tempFilePaths:" + tempFilePaths);
          //concat拼接图片url,
          var imgList = that.data.imgList.concat(tempFilePaths);
          that.setData({
            imgList: imgList
          })
        }
      })
    }
  },

  /**
   * 开始上传
   */
  startUpload(contentId) {
    var imgList = this.data.imgList
    var leng = imgList.length
    for (var i = 0; i < leng; i++) {
      this.uploadImg(i, contentId)
    }
  },
  /**
   *  用户上传图片
   */
  uploadImg(index, contentId) {
    var imgList = this.data.imgList;
    //图片要用uploadFile来请求，不能用reqeust
    const uploadProcess = wx.uploadFile({
      url: urlPath + '/userContent/upload',
      filePath: imgList[index],
      name: 'file',
      header: {
        "Content-Type": "multipart/form-data"
      },
      formData: {
        token: app.globalData.token,
        contentId: contentId,//评论的id
        imgNo: index + 1 //第几张图片的用户,
      },
      success: function(res) {
        console.log(res)
      },
      fail(res) {
        console.log(res)
      },
      complete(res) {
        // console.log(res)
      }
    })

    //查看一下上传进度
    uploadProcess.onProgressUpdate((res) => {
      console.log('上传进度', res.progress)
      console.log('已经上传的数据长度', res.totalBytesSent)
      console.log('预期需要上传的数据总长度', res.totalBytesExpectedToSend)
    })
  },


  /**
   * 设置是否仅自己可见
   */
  onlyMe(e) {
    this.setData({
      onlyMe: e.detail.value
    })
    console.log("onlyMe:" + e.detail.value);//true,false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {

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
    this.setData({
      onlyMe: false
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