package com.sixzerofour.parkingsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sixzerofour.parkingsystem.bean.Car;
import com.sixzerofour.parkingsystem.service.CarService;
import com.sixzerofour.parkingsystem.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@Api(tags="停车场管理")
@RestController
@RequestMapping("/api/parking")
public class ParkingController {
    private static Integer num = 100;
    private static Integer fee = 10;

    @Resource
    CarService carService;

    @ApiOperation("测试接口")
    @GetMapping("/test")
    public Result test(){
        return new Result().success().data("车位",num).data("费用",fee);
    }

    @ApiOperation("剩余车位接口")
    @GetMapping("/place_available")
    public Result getAvailable(){
        return new Result().success().data("剩余车位",num- carService.count());
    }

    @ApiOperation("修改车位数量接口")
    @PutMapping("/alter_num")
    public Result alterAvailable(Integer newNum){
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
    public Result alterFee(Integer newFee){
        if(newFee<0) {
            return new Result().error();
        }
        fee = newFee;
        return new Result().success();
    }

    @ApiOperation("查询停车费接口")
    @GetMapping("/get_fee")
    public Result getFee(String carNum){
        //车牌格式合法性检查
        if(!carService.isLegal(carNum)) {
            Result result = new Result().error();
            result.setMessage("请正确输入车牌号");
            return result;
        }
        try {
            QueryWrapper<Car> carQueryWrapper = new QueryWrapper<>();
            carQueryWrapper.select().like("car_num", carNum);
            Date timeNow = new Date();
            Car car = carService.getOne(carQueryWrapper);
            car.setOutTime(timeNow);
            long period = timeNow.getTime() - car.getInTime().getTime();
            Integer hours = (int) (period / (1000 * 60 * 60));
            Integer minutes = (int) (period / (1000 * 60)-hours * 60);
            Integer payment = hours * fee;
            String interval = hours + "小时" + minutes + "分";
            return new Result().success().data("已停时长", interval).data("费用", payment);
        }
        catch(Exception e){
            return new Result().error();
        }
    }

    @ApiOperation("车辆入库接口")
    @PostMapping("/in_car")
    public Result vehicleIn(Car car){
        //车牌格式合法性检查
        if(!carService.isLegal(car.getCarNum())) {
            Result result = new Result().error();
            result.setMessage("请正确输入车牌号");
            return result;
        }
        // 设置车辆入库时间
        car.setInTime(new Date());
        carService.save(car);
        return new Result().success();
    }

    @ApiOperation("车辆出库接口")
    @DeleteMapping("/out_car")
    public Result vehicleOut(Car car){
        QueryWrapper<Car> wrapper = new QueryWrapper<>();
        wrapper.eq("car_num",car.getCarNum());
        carService.remove(wrapper);
        return new Result().success();
    }
}
