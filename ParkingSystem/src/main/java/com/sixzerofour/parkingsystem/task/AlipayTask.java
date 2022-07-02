package com.sixzerofour.parkingsystem.task;

import com.sixzerofour.parkingsystem.entity.OrderInfo;
import com.sixzerofour.parkingsystem.service.AlipayService;
import com.sixzerofour.parkingsystem.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class AlipayTask {
    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private AlipayService alipayService;

    @Scheduled(cron = "0/30 * * * * ? ")
    public void orderConfirm(){

        log.info("orderConfirm被执行");
        List<OrderInfo> orderInfoList = orderInfoService.getUnpaidOrderByDuration(5);
        for(OrderInfo orderInfo:orderInfoList){
            String orderNo = orderInfo.getOrderNo();
            log.warn("订单超时：{}",orderNo);
            alipayService.checkOrderStatus(orderNo);

        }
    }
}
