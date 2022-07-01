package com.sixzerofour.parkingsystem.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.sixzerofour.parkingsystem.entity.OrderInfo;
import com.sixzerofour.parkingsystem.service.AlipayService;
import com.sixzerofour.parkingsystem.service.OrderInfoService;
import com.sixzerofour.parkingsystem.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/alipay")
@Api(tags = "支付宝支付")
@Slf4j
public class AlipayController {
    @Resource
    private AlipayService alipayService;

    @Resource
    private Environment config;

    @Resource
    private OrderInfoService orderInfoService;

    @ApiOperation("统一收单交易创建接口")
    @PostMapping("/trade/create")
    public Result<?> tradeCreate(@RequestParam String plate,
                                 @RequestParam Integer fee,
                                 @RequestParam String buyer_id){
        String orderStr = alipayService.tradeCreate(plate, fee, buyer_id);
        HashMap<String,Object> result = new HashMap<>();
        result.put("orderStr",orderStr);
        return new Result<>().success().data(result);
    }

    @ApiOperation("支付通知接口")
    @PostMapping("/trade/notify")
    public String tradeNotify(@RequestParam Map<String,String> params){
        try {
            //异步通知验签
            boolean signVerified = AlipaySignature.rsaCheckV1(
                    params,
                    config.getProperty("alipay.alipay-public-key"),
                    AlipayConstants.CHARSET_UTF8,
                    AlipayConstants.SIGN_TYPE_RSA2
            );
            if(!signVerified){
                log.info("验签失败");
                return "failure";
            }
            log.info("支付成功异步通知验签成功");
            //在步骤四验证签名正确后，必须再严格按照如下描述校验通知数据的正确性。
            //1.商家需要验证该通知数据中的 out_trade_no 是否为商家系统中创建的订单号。
            String outTrade = params.get("out_trade_no");
            OrderInfo order = orderInfoService.getOrderByOrderNo(outTrade);
            if(order==null){
                log.error("订单不存在");
                return "failure";
            }
            //2.判断 total_amount 是否确实为该订单的实际金额（即商家订单创建时的金额）。
            String totalAmount = params.get("total_amount");
            Integer totalAmountInt = Integer.parseInt(totalAmount);
            int totalFee = order.getTotalFee();
            if(totalAmountInt != totalFee){
                log.error("金额校验失败");
                return "failure";
            }
            //3.校验通知中的 seller_id（或者 seller_email ) 是否为 out_trade_no
            // 这笔单据的对应的操作方（有的时候，一个商家可能有多个seller_id/seller_email）。
            String sellerId = params.get("seller_id");
            String sellerIdProperty = config.getProperty("alipay.seller-id");
            if(!sellerId.equals(sellerIdProperty)){
                log.error("商家id校验失败");
                return "failure";
            }
            //4.验证 app_id 是否为该商家本身。上述 1、2、3、4 有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
            // 在上述验证通过后商家必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
            String appId = params.get("app_id");
            String appIdProperty = config.getProperty("alipay.app-id");
            if(!appId.equals(appIdProperty)){
                log.error("appID校验失败");
                return "failure";
            }
            // 在支付宝的业务通知中，只有交易通知状态为 TRADE_SUCCESS 或 TRADE_FINISHED 时，支付宝才会认定为买家付款成功。
            String tradeStatus = params.get("trade_status");
            //TRADE_SUCCESS为支持退款的情况的返回值
            if(!"TRADE_FINISHED".equals(tradeStatus)){
                log.error("支付未完成");
                return "failure";
            }
            //自身业务（修改订单状态）
            alipayService.processOrder(params);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @ApiOperation("用户取消订单")
    @PostMapping("/trade/close/{orderNo}")
    public Result<?> cancelOrder(@PathVariable String orderNo){
        log.info("取消订单");
        alipayService.cancelOrder(orderNo);
        return null;
    }

    @ApiOperation("查询订单接口")
    @GetMapping("/query/{orderNo}")
    public Result<?> queryOrder(@PathVariable String orderNo){
        log.info("查询订单");
        String result = alipayService.queryOrder(orderNo);
        HashMap<String,Object> queryResult = new HashMap<>();
        queryResult.put("result",result);
        return new Result<>().success().setMessage("查询成功").data(queryResult);
    }
}
