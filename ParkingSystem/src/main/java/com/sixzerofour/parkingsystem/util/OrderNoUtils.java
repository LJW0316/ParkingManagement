package com.sixzerofour.parkingsystem.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 订单号工具类
 * */
public class OrderNoUtils {

    /*
     * 获取订单号
     * */
    public static String getOrderNo(){
        return "ORDER_"+getNo();
    }

    /*
    * 生成订单号
    * */
    public static String getNo(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for(int i=0;i<3;i++){
            result += random.nextInt(10);
        }
        return newDate+result;
    }
}
