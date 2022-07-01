package com.sixzerofour.parkingsystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AlipayTradeStatus {

    /**
     * 支付成功
     * */
    SUCCESS("TRADE_SUCCESS"),

    /**
     * 未支付
     * */
    UNPAID("WAIT_BUYER_PAY"),

    /**
     * 已关闭
     * */
    CLOSED("TRADE_CLOSED");

    private final String type;
}
