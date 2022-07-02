package com.sixzerofour.parkingsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sixzerofour.parkingsystem.entity.OrderInfo;
import com.sixzerofour.parkingsystem.enums.OrderStatus;

import java.util.List;

public interface OrderInfoService extends IService<OrderInfo> {
    OrderInfo createOrder(String plate,Integer fee);

    OrderInfo getUnpaidOrder(String plate);

    OrderInfo getOrderByOrderNo(String orderNo);

    String getOrderStatusByOrderNo(String orderNo);

    void updateStatusByOrderNo(String orderNo, OrderStatus status);

    List<OrderInfo> getUnpaidOrderByDuration(int minutes);
}
