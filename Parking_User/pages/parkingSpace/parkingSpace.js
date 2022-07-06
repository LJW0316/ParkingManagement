Page({
  data: {
    parkingNum: 100,
  },
  onLoad() {
    my.request({
      url: 'http://47.103.85.74:8090/api/parking/place_available',
      method: 'GET',
      dataType: 'json',
      success: (res) => {
        console.log(res);
        this.setData ({
          parkingNum: res.data.data,
        })
      },
      fail: (err) => {
        console.log('error', err);
        my.alert({
          title: '错误',
          content: err,
        })
      }
    })
  },
});
