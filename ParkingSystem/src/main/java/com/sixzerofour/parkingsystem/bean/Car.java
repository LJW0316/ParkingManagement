package com.sixzerofour.parkingsystem.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Car {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("car_num")
    private String carNum;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("in_time")
    private Date inTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    @TableField("out_time")
    private Date outTime;
}
