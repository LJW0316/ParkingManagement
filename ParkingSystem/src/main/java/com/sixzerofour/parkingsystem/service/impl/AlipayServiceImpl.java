package com.sixzerofour.parkingsystem.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.sixzerofour.parkingsystem.entity.OrderInfo;
import com.sixzerofour.parkingsystem.enums.AlipayTradeStatus;
import com.sixzerofour.parkingsystem.enums.OrderStatus;
import com.sixzerofour.parkingsystem.service.AlipayService;
import com.sixzerofour.parkingsystem.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class AlipayServiceImpl implements AlipayService  {
    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private AlipayClient alipayClient;

    @Resource
    private Environment config;

    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void cancelOrder(String orderNo) {
        this.closeOrder(orderNo);
        //更新订单状态
        orderInfoService.updateStatusByOrderNo(orderNo,OrderStatus.CANCEL);
    }

    @Override
    public String queryOrder(String orderNo) {
        try {
            log.info("查单接口调用{}",orderNo);

            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no",orderNo);
            request.setBizContent(bizContent.toString());

            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                log.info("调用成功" + response.getBody());
                return response.getBody();
            }else{
                log.info("调用失败，返回码"+response.getCode()+"错误内容"+response.getBody());
                return null;//订单不存在
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("查单接口调用失败");
        }
    }

    @Override
    public void checkOrderStatus(String orderNo) {
        log.warn("根据订单号核实订单状态 ===> {}",orderNo);

        String result = this.queryOrder(orderNo);
        //解析查单响应结果
        Gson gson = new Gson();
        HashMap<String,LinkedTreeMap> resultMap = gson.fromJson(result,HashMap.class);
        LinkedTreeMap alipayTradeQueryResponse = resultMap.get("alipay_trade_query_response");
        Object tradeStatus = alipayTradeQueryResponse.get("trade_status");
        if(AlipayTradeStatus.UNPAID.getType().equals(tradeStatus)){
            this.closeOrder(orderNo);

            orderInfoService.updateStatusByOrderNo(orderNo,OrderStatus.CLOSED);
        }
        if(AlipayTradeStatus.SUCCESS.getType().equals(tradeStatus)){
            log.info("核实订单已支付 ===> {}",orderNo);

            orderInfoService.updateStatusByOrderNo(orderNo,OrderStatus.SUCCESS);
        }
    }

    private void closeOrder(String orderNo) {
        try {
            log.info("关单接口调用,订单号===>{}",orderNo);

            AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no",orderNo);
            request.setBizContent(bizContent.toString());
            AlipayTradeCloseResponse response = alipayClient.execute(request);

            if(response.isSuccess()){
                log.info("调用成功" + response.getBody());
            }else{
                log.info("调用失败，返回码"+response.getCode()+"错误内容"+response.getBody());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("关单接口调用失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processOrder(Map<String, String> params) {
        log.info("处理订单");
        //利用数据锁进行并发控制
        if(lock.tryLock()){
            try{
                String orderNo = params.get("out_trade_no");
                String orderStatus = orderInfoService.getOrderStatusByOrderNo(orderNo);
                if(!OrderStatus.UNPAID.getType().equals(orderStatus)){
                    return;
                }

                orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);
            }finally {
                lock.unlock();
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String tradeCreate(String plate, Integer fee, String buyer_id) {
        try {
            //生成订单
            OrderInfo orderInfo = orderInfoService.createOrder(plate,fee);

            //调用支付宝API
            AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
            //  request.setNotifyUrl("传入外网可以访问的异步地址");//用于获得支付宝服务端返回的支付结果，以POST请求发送方式同步商户服务器
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderInfo.getOrderNo());
            bizContent.put("total_amount", orderInfo.getTotalFee());
            bizContent.put("subject", "停车费");
            bizContent.put("buyer_id", buyer_id);
            request.setBizContent(bizContent.toString());
            request.setNotifyUrl(config.getProperty("alipay.notify-url"));
            AlipayTradeCreateResponse response = alipayClient.execute(request);
            log.info(response.getBody());
            if(response.isSuccess()){
                log.info("调用成功，返回结果=====>"+ response.getBody());
                return response.getBody();
            }else {
                log.info("调用失败，结果码=====>"+ response.getCode()+"错误描述"+response.getMsg());
                throw new RuntimeException("创建支付交易失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("创建支付交易失败");
        }
    }
}
