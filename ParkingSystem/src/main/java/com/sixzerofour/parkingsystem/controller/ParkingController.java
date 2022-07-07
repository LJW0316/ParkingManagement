package com.sixzerofour.parkingsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sixzerofour.parkingsystem.entity.Car;
import com.sixzerofour.parkingsystem.entity.OrderInfo;
import com.sixzerofour.parkingsystem.enums.OrderStatus;
import com.sixzerofour.parkingsystem.service.CarService;
import com.sixzerofour.parkingsystem.service.OrderInfoService;
import com.sixzerofour.parkingsystem.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;

@Api(tags="停车场管理")
@RestController
@CrossOrigin
@RequestMapping("/api/parking")
public class ParkingController {
    private static Integer num = 100;
    private static Integer fee = 10;

    @Resource
    CarService carService;

    @Resource
    OrderInfoService orderInfoService;

    @ApiOperation("获取车位数量接口")
    @GetMapping("/get_num")
    public Result<?> getNum(){
        return new Result<>().success().data(num);
    }

    @ApiOperation("获取停车费接口")
    @GetMapping("/get_fee")
    public Result<?> getFee(){
        return new Result<>().success().data(fee);
    }


    @ApiOperation("剩余车位接口")
    @GetMapping("/place_available")
    public Result<?> getAvailable(){
        QueryWrapper<Car> carQueryWrapper = new QueryWrapper<>();
        carQueryWrapper.select().isNull("out_time");
        return new Result<>().success().data(num- carService.count(carQueryWrapper));
    }

    @ApiOperation("修改车位数量接口")
    @PutMapping("/alter_num")
    public Result<?> alterAvailable(@RequestParam Integer newNum){
        if(newNum<0)
            return new Result<>().error().setMessage("车位数量不能为负数");
        num = newNum;
        return new Result<>().success().setMessage("成功修改车位数量");
    }

    @ApiOperation("修改停车费（每小时）接口")
    @PutMapping("/alter_fee")
    public Result<?> alterFee(@RequestParam Integer newFee){
        if(newFee<0)
            return new Result<>().error().setMessage("停车费不能为负");
        fee = newFee;
        return new Result<>().success().setMessage("成功修改停车费为"+fee+"元/每小时");
    }

    @ApiOperation("确认支付接口")
    @GetMapping("/pay_confirm")
    public Result<?> payConfirm(@RequestParam String plate){
        //车牌格式合法性检查
        if(!carService.isLegal(plate))
            return new Result<>().error().setMessage("请正确输入车牌号");
        try {
            Car car = carService.getCar(plate);
            Date timeNow = new Date();
            long period = timeNow.getTime() - car.getInTime().getTime();
            Integer hours = (int) (period / (1000 * 60 * 60));
            Integer minutes = (int) (period / (1000 * 60)-hours * 60);
            Integer payment = hours * fee;
            String interval = hours + "小时" + minutes + "分";
            OrderInfo orderInfo = orderInfoService.getNullOrUnpaidOrder(plate);
            if(orderInfo==null)
                orderInfoService.createOrder(plate,payment);
            else{
                Date date = new Date();
                if(payment==0){
                    orderInfo.setOrderStatus(OrderStatus.SUCCESS.getType());
                }else{
                    orderInfo.setOrderStatus(OrderStatus.UNPAID.getType());
                    orderInfo.setTotalFee(payment);
                }
                orderInfo.setCreateTime(date);
                orderInfoService.updateById(orderInfo);
            }
            HashMap<String,Object> result = new HashMap<>();
            result.put("parkingTime",interval);
            result.put("fee",payment);
            return new Result<>().success().data(result);
        }
        catch(Exception e){
            return new Result<>().carNotFound();
        }
    }

    @ApiOperation("车辆入库接口")
    @PostMapping("/in_car")
    public Result<?> vehicleIn(@RequestParam String plate){
        //车牌格式合法性检查
        if(!carService.isLegal(plate))
            return new Result<>().error().setMessage("请正确输入车牌号");
        //检查车辆是否仍在场内
        if(carService.getCar(plate) != null)
            return new Result<>().stillInError();
        // 设置车辆入库时间
        Car car = new Car();
        car.setCarNum(plate);
        car.setInTime(new Date());
        car.setOutTime(null);
        carService.save(car);
        OrderInfo orderInfo = orderInfoService.createOrder(plate, 0);
        orderInfo.setCreateTime(null);
        orderInfo.setOrderStatus(null);
        orderInfoService.updateById(orderInfo);
        return new Result<>().success();
    }

    @ApiOperation("车辆出库接口")
    @DeleteMapping("/out_car")
    public Result<?> vehicleOut(@RequestParam String plate){
        OrderInfo orderInfo = orderInfoService.getNullOrUnpaidOrder(plate);
        if(orderInfo!=null)
            return new Result<>().error().setMessage("有待支付订单");
        Car car = carService.getCar(plate);
        Date timeNow = new Date();
        //设置车辆出场时间
        car.setOutTime(timeNow);
        carService.updateById(car);
        return new Result<>().success();
    }
}
