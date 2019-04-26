const app = getApp();
const urlPath = app.globalData.urlPath;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    contentList: [],
    contentListId: -1,
    curPage: 1,
    total_count: 0,
    pageSize: 10,
    isMe: 0, //0-全部，1-个人
    inputShow: false,
    contentId: -1,
    conContext: '',
    allImage: '../../resources/images/Ic_Homepage_Focus.png',
    oneImage: '../../resources/images/Ic_Me_Normal.png',
  },
  bindPublish: function(event) {
    wx.navigateTo({
      url: '../publish/publish',
    })
  },
  bindPageContent: function(event) {
    var that = this;
    var curPage = curPage + 1;
    var totalCount =  this.data.total_count;
    var pageCount = 0;
    if(totalCount % 10 == 0){
      pageCount = totalCount / 10;
    }else {
      pageCount = totalCoount / 10 + 1;
    }
    
    
    if(curPage > pageCount) {
      wx.showToast({
        title: '没有更多动态了',
        icon: 'none',
      })
    } else {
      wx.request({
        url: urlPath + 'userContent/pageContent',
        data: {
          curPage: curPage,
        },
        success: function (res) {

        }
      })
    }
  },
  /**
   * 预览图片
   */
  bindPreviewImage: function(event) {
    console.log(JSON.stringify(event))
    var index = event.currentTarget.dataset.index;
    
    var url = event.currentTarget.dataset.url;
    console.log("contentList[index]: " + JSON.stringify(this.data.contentList[index][0].contentImage));
    var contentImage = this.data.contentList[index][0].contentImage;
    console.log("contentImage:" + contentImage);
    var images = contentImage.split(",");
   console.log(JSON.stringify(event))
    wx.previewImage({
      current: images[url], // 当前显示图片的http链接
      urls: images // 需要预览的图片http链接列表
    })
  },
  bindAllContext: function(event) {
    var that = this;
    //获取评论列表
    wx.request({
      url: app.globalData.urlPath + 'userContent/contentList',
      data: {
        token: app.globalData.token,
        isMe: 0,
      },
      success: function (res) {
        console.log("content:" + JSON.stringify(res.data));
        var totalCount = res.data[0];
        var contentList = res.data[1];
        that.setData({ contentList: contentList, total_count: totalCount,
        allImage: '../../resources/images/Ic_Homepage_Focus.png',
        oneImage: '../../resources/images/Ic_Me_Normal.png', });
      }
    })
  },

  bindMyContext: function (event) {
    var that = this;
    //获取评论列表
    wx.request({
      url: app.globalData.urlPath + 'userContent/contentList',
      data: {
        token: app.globalData.token,
        isMe: 1,
      },
      success: function (res) {
        console.log("content:" + JSON.stringify(res.data));
        var totalCount = res.data[0];
        var contentList = res.data[1];
        that.setData({ contentList: contentList, total_count: totalCount,
          allImage: '../../resources/images/Ic_Homepage_Normal.png',
          oneImage: '../../resources/images/Ic_Me_Focus.png',
        });
      }
    })
  },
  /**
   * 点赞事件
   */
  bindPraise: function(event) {

    //得到当前说说的id,
    var index = event.currentTarget.dataset.index;
    var list = this.data.contentList[index];
    console.log("list: " + JSON.stringify(list))
    var id = list[0].id;
    var that = this;
    wx.request({
      url: urlPath + 'contentPraise/isPraise',
      data: {
        token: app.globalData.token,
        contentId: id,
      },
      success: function(res) {
        console.log(JSON.stringify(res));
        var isPraise = res.data[0];
        var praiseData = res.data[1];
        // var contentPraiseData = that.data.contentList[index].praiseData;
        var contentPraiseData = "contentList[" + index + "][0].praiseData";
        
        // var contentPraiseIcon = that.data.contentList[index].praiseIcon;
        var contentPraiseIcon = "contentList[" + index + "][0].praiseIcon";

        console.log("praiseData:" + that.data.contentList[index].praiseData);
        if(isPraise == 1) {
          that.setData({ [contentPraiseData]: praiseData,
            [contentPraiseIcon]: '../../resources/images/Ic_Praise_Focus.png'
            })
            console.log("点赞成功")
            wx.showToast({
              title: '点赞成功',
              icon: 'none',
            })
        }else {
          that.setData({
            [contentPraiseData]: praiseData,
            [contentPraiseIcon]: '../../resources/images/Ic_Praise_Normal.png'
          })
          console.log("取消点赞")
          wx.showToast({
            title: '取消点赞',
            icon: 'none',
          })
        }
      }
    })
  },

  /**
   * 评论事件
   */
bindContent: function(event) {
  this.setData({inputShow: true})
  
  var index = event.currentTarget.dataset.index;
  var contentListId = index;
  var content = this.data.contentList[index];
  var contentId = content[0].id;

  console.log(contentId);
  this.setData({ contentId: contentId, contentListId: contentListId});
},
/**获取要发送的评论内容 */
  bindblur: function(event) {
    // console.log(JSON.stringify(event))
    // var conContext = event.detail.value;
    // this.setData({ conContext: conContext});
  },
  bindinput: function (event) {
    console.log(JSON.stringify(event))
    var conContext = event.detail.value;
    this.setData({ conContext: conContext });
  },


  bindSendcomment: function(event) {
    
    var contentId = this.data.contentId;
    var conContext = this.data.conContext;
    console.log(contentId);
    console.log("context:" + conContext);
    console.log("userInfo: " + app.globalData.userInfo);
    var that = this;
    wx.request({
      url: urlPath + 'contentComment/addComment',
      data: {
        token: app.globalData.token,
        contentId: contentId,
        context: conContext,

      },
      success: function(res) {
        //手动添加评论内容
        var contentListId = that.data.contentListId;
        var commentList = that.data.contentList[contentListId][1];
        console.log("commentList:" + commentList);
        var comment = {};
        console.log("userInfo:" + app.globalData.userInfo);
        comment.userName = app.globalData.userInfo.nickName;
        comment.commentContent = conContext;
        commentList.push(comment);
        var cList = 'contentList[' + contentListId + '][1]';
        that.setData({ [cList]: commentList, inputShow: false});
        wx.showToast({
          title: '评论成功',
          icon: 'none',
        })

      }
    })
  },
/**隐藏评论框 */
  disibleComment: function(event){
    this.setData({ inputShow: false});
  },
  /**防止手指移动带动父类移动 */
  noOption: function (event) {
   //不需要任何操作
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    //获取评论列表
    wx.request({
      url: app.globalData.urlPath + 'userContent/contentList',
      data: {
        token: app.globalData.token,
        isMe: that.data.isMe,
      },
      success: function(res) {
        console.log("content:" + JSON.stringify(res.data));
        var totalCount = res.data[0];
        var contentList = res.data[1];
        that.setData({ contentList: contentList, total_count: totalCount});
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
    var that = this;
    //获取评论列表
    wx.request({
      url: app.globalData.urlPath + 'userContent/contentList',
      data: {
        token: app.globalData.token,
        isMe: that.data.isMe,
      },
      success: function (res) {
        console.log("content:" + JSON.stringify(res.data));
        var totalCount = res.data[0];
        var contentList = res.data[1];
        that.setData({ contentList: contentList, total_count: totalCount });
      }
    })
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