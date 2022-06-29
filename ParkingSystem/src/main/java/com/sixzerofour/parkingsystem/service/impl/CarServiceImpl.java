package com.sixzerofour.parkingsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sixzerofour.parkingsystem.bean.Car;
import com.sixzerofour.parkingsystem.mapper.CarMapper;
import com.sixzerofour.parkingsystem.service.CarService;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;

@Service
public class CarServiceImpl extends ServiceImpl<CarMapper,Car> implements CarService {
    @Override
    public Boolean isLegal(String carNum){
        Matcher matcher = pattern.matcher(carNum);
        return matcher.matches();
    }
}
