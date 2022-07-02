package com.sixzerofour.parkingsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sixzerofour.parkingsystem.entity.Car;

public interface CarService extends IService<Car> {

     Boolean isLegal(String carNum);
     Car getCar(String plate);
}
