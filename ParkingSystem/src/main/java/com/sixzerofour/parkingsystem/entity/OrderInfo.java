package com.sixzerofour.parkingsystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("order_info")
public class OrderInfo {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String plate;

    @TableField("order_no")
    private String orderNo;

    @TableField(value = "total_fee", updateStrategy = FieldStrategy.IGNORED)
    private Integer totalFee;


    @TableField(value = "order_status", updateStrategy = FieldStrategy.IGNORED)
    private String orderStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", updateStrategy = FieldStrategy.IGNORED)
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime;
}
