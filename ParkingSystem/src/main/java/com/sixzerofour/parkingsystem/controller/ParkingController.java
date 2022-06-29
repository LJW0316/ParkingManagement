package com.sixzerofour.parkingsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sixzerofour.parkingsystem.entity.Car;
import com.sixzerofour.parkingsystem.service.CarService;
import com.sixzerofour.parkingsystem.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;

@Api(tags="停车场管理")
@RestController
@RequestMapping("/api/parking")
public class ParkingController {
    private static Integer num = 100;
    private static Integer fee = 10;

    @Resource
    CarService carService;

    @ApiOperation("剩余车位接口")
    @GetMapping("/place_available")
    public Result<?> getAvailable(){
        return new Result().success().data(num- carService.count());
    }

    @ApiOperation("修改车位数量接口")
    @PutMapping("/alter_num")
    public Result<?> alterAvailable(Integer newNum){
        if(newNum<0) {
            Result result = new Result().error();
            result.setMessage("车位数量不能为负");
            return result;
        }
        num = newNum;
        return new Result().success();
    }

    @ApiOperation("修改停车费（每小时）接口")
    @PutMapping("/alter_fee")
    public Result<?> alterFee(Integer newFee){
        if(newFee<0) {
            return new Result().error();
        }
        fee = newFee;
        return new Result().success();
    }

    @ApiOperation("确认支付接口")
    @GetMapping("/pay_confirm")
    public Result<?> payConfirm(@RequestParam String plate){
        //车牌格式合法性检查
        if(!carService.isLegal(plate)) {
            Result result = new Result().error();
            result.setMessage("请正确输入车牌号");
            return result;
        }
        try {
            Car car = carService.getCar(plate);
            Date timeNow = new Date();
            long period = timeNow.getTime() - car.getInTime().getTime();
            Integer hours = (int) (period / (1000 * 60 * 60));
            Integer minutes = (int) (period / (1000 * 60)-hours * 60);
            Integer payment = hours * fee;
            String interval = hours + "小时" + minutes + "分";
            HashMap result = new HashMap<>();
            result.put("parkingTime",interval);
            result.put("fee",payment);
            return new Result().success().data(result);
        }
        catch(Exception e){
            return new Result().carNotFound();
        }
    }

    @ApiOperation("车辆入库接口")
    @PostMapping("/in_car")
    public Result<?> vehicleIn(String plate){
        //车牌格式合法性检查
        if(!carService.isLegal(plate)){
            Result result = new Result().error();
            result.setMessage("请正确输入车牌号");
            return result;
        }
        //检查车辆是否仍在场内
        if(carService.getCar(plate) != null)
            return new Result().stillInError();
        // 设置车辆入库时间
        Car car = new Car();
        car.setCarNum(plate);
        car.setInTime(new Date());
        car.setOutTime(null);
        carService.save(car);
        return new Result().success();
    }

    @ApiOperation("车辆出库接口")
    @DeleteMapping("/out_car")
    public Result vehicleOut(String plate){
        QueryWrapper<Car> wrapper = new QueryWrapper<>();
        wrapper.eq("car_num",plate);
        Car car = carService.getOne(wrapper);
        Date timeNow = new Date();
        //设置车辆出场时间
        car.setOutTime(timeNow);
        carService.updateById(car);
        return new Result().success();
    }
}
