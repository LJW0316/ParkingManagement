package com.sixzerofour.parkingsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sixzerofour.parkingsystem.bean.Car;

import java.util.regex.Pattern;

public interface CarService extends IService<Car> {
     Pattern pattern = Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]" +
            "[A-Z][A-Z0-9]{4}[A-Z0-9挂学警港澳]$");
     Boolean isLegal(String carNum);
}
