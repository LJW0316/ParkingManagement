Page({
  data: {
    parkingTime: '',
    fee: 0,
    plate: '',
    tradeNO: '',
    buyer_id: '2088622987055020',
  },
  onLoad() { },
  bindKeyInput(e) {
    console.log(e.detail.value);
    this.setData({
      plate: e.detail.value,
    });
  },
  pay() {
    if (this.data.plate === '') { //拍照为空
      my.alert({
        title: '警告',
        content: '车牌号不能为空！',
        buttonText: '确认'
      })
      return;
    }
    my.request({ //请求停车时长和费用信息
      url: "http://47.103.85.74:8090/api/parking/pay_confirm",
      method: 'GET',
      data: {
        plate: this.data.plate,
      },
      dataType: 'json',
      success: (res) => {
        console.log(res);
        if (res.data.code === -1 || res.data.code === 400) { //车牌错误，或不在库中
          console.log("车牌错误");
          my.alert({
            title: '错误',
            content: res.data.message,
            buttonText: '我知道了',
          });
        } else {
          this.setData({
            //根据后端结果修改对应信息
            parkingTime: res.data.data.parkingTime,
            fee: res.data.data.fee,
          })
          my.confirm({
            title: '支付确认',
            content: '停车时长：' + this.data.parkingTime + ' \n停车费用：' + this.data.fee + '元',
            confirmButtonText: '支付',
            cancelButtonText: '取消',
            success: (res) => {
              console.log(res);
              if (res.confirm) { //用户点击支付按钮
                if (this.data.fee == 0) {
                  my.navigateTo({ url: '../success/success' });
                } else {
                  my.request({ //请求订单号（后端创建订单）
                    url: 'http://47.103.85.74:8090/api/alipay/trade/create',
                    headers: {
                      'content-type': 'application/x-www-form-urlencoded'
                    },
                    method: 'POST',
                    data: {
                      plate: this.data.plate,
                      fee: this.data.fee,
                      buyer_id: this.data.buyer_id,
                    },
                    success: (res) => {
                      console.log(res);
                      let orderStr = res.data.data.orderStr;
                      orderStr = JSON.parse(orderStr);
                      console.log('订单：', orderStr);
                      this.setData({
                        tradeNO: orderStr.alipay_trade_create_response.trade_no,
                      })
                      console.log('订单号：', this.data.tradeNO);
                      //调用支付
                      my.tradePay({
                        tradeNO: this.data.tradeNO,
                        success: (res) => {
                          console.log(res);
                          //查询支付状态
                          if (res.resultCode === '9000') { //支付成功
                            my.navigateTo({ url: '../success/success' });
                          } else {
                            my.alert({
                              content: '支付失败！',
                            })
                          }
                        },
                        fail: (res) => {
                          my.alert({
                            content: JSON.stringify(res),
                          });
                        }
                      })
                    }
                  })
                }

              }
            }
          })
        }
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
