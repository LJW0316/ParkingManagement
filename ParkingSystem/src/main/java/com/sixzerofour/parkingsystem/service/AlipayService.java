package com.sixzerofour.parkingsystem.service;

import java.util.Map;

public interface AlipayService {
    String tradeCreate(String plate, Integer fee, String buyer_id);

    void processOrder(Map<String,String> params);

    void cancelOrder(String orderNo);

    String queryOrder(String orderNo);

    void checkOrderStatus(String orderNo);
}
