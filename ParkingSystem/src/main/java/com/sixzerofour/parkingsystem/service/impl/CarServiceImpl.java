package com.sixzerofour.parkingsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sixzerofour.parkingsystem.entity.Car;
import com.sixzerofour.parkingsystem.mapper.CarMapper;
import com.sixzerofour.parkingsystem.service.CarService;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CarServiceImpl extends ServiceImpl<CarMapper,Car> implements CarService {
    Pattern pattern = Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]" +
            "[A-Z][A-Z0-9]{4}[A-Z0-9挂学警港澳]$");
    @Override
    public Boolean isLegal(String carNum){
        Matcher matcher = pattern.matcher(carNum);
        return matcher.matches();
    }

    @Override
    public Car getCar(String plate){
        QueryWrapper<Car> carQueryWrapper = new QueryWrapper<>();
        carQueryWrapper.select().eq("car_num",plate).isNull("out_time");
        return getOne(carQueryWrapper);
    }
}
