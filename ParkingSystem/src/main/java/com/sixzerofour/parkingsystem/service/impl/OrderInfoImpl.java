package com.sixzerofour.parkingsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sixzerofour.parkingsystem.entity.OrderInfo;
import com.sixzerofour.parkingsystem.enums.OrderStatus;
import com.sixzerofour.parkingsystem.mapper.OrderInfoMapper;
import com.sixzerofour.parkingsystem.service.OrderInfoService;
import com.sixzerofour.parkingsystem.util.OrderNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class OrderInfoImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    @Override
    public OrderInfo createOrder(String plate,Integer fee) {
        OrderInfo orderInfo = this.getUnpaidOrder(plate);
        if(orderInfo != null){
            return orderInfo;
        }
        orderInfo = new OrderInfo();
        orderInfo.setPlate(plate);
        orderInfo.setOrderNo(OrderNoUtils.getOrderNo());
        orderInfo.setTotalFee(fee);
        Date timeNow = new Date();
        orderInfo.setCreateTime(timeNow);
        orderInfo.setOrderStatus(OrderStatus.UNPAID.getType());
        baseMapper.insert(orderInfo);

        return orderInfo;
    }

    @Override
    public OrderInfo getUnpaidOrder(String plate) {
        QueryWrapper<OrderInfo> orderInfoQueryWrapper = new QueryWrapper<>();
        orderInfoQueryWrapper.select().eq("plate",plate).eq("order_status",OrderStatus.UNPAID.getType());
        return getOne(orderInfoQueryWrapper);
    }

    @Override
    public String getOrderStatusByOrderNo(String orderNo) {
        QueryWrapper<OrderInfo> orderInfoQueryWrapper = new QueryWrapper<>();
        orderInfoQueryWrapper.select().eq("order_no",orderNo);
        return getOne(orderInfoQueryWrapper).getOrderStatus();
    }

    @Override
    public void updateStatusByOrderNo(String orderNo, OrderStatus status) {
        log.info("更新订单状态");
        QueryWrapper<OrderInfo> orderInfoQueryWrapper = new QueryWrapper<>();
        orderInfoQueryWrapper.select().eq("order_no",orderNo);
        OrderInfo orderInfo = getOne(orderInfoQueryWrapper);
        orderInfo.setOrderStatus(status.getType());
        Date timeNow = new Date();
        orderInfo.setUpdateTime(timeNow);
        updateById(orderInfo);
    }

    @Override
    public List<OrderInfo> getUnpaidOrderByDuration(int minutes) {

        Instant instant = Instant.now().minus(Duration.ofMinutes(minutes));

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_status",OrderStatus.UNPAID.getType());
        queryWrapper.le("create_time",instant);

        List<OrderInfo> orderInfoList = baseMapper.selectList(queryWrapper);
        return orderInfoList;
    }

    @Override
    public OrderInfo getOrderByOrderNo(String orderNo) {
        QueryWrapper<OrderInfo> orderInfoQueryWrapper = new QueryWrapper<>();
        orderInfoQueryWrapper.select().eq("order_no",orderNo);
        return getOne(orderInfoQueryWrapper);
    }
}
