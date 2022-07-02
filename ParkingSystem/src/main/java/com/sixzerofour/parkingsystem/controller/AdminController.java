package com.sixzerofour.parkingsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sixzerofour.parkingsystem.entity.Car;
import com.sixzerofour.parkingsystem.entity.OrderInfo;
import com.sixzerofour.parkingsystem.service.CarService;
import com.sixzerofour.parkingsystem.service.OrderInfoService;
import com.sixzerofour.parkingsystem.service.UserService;
import com.sixzerofour.parkingsystem.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "管理员接口")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Resource
    CarService carService;

    @Resource
    OrderInfoService orderInfoService;

    @Resource
    UserService userService;

    @ApiOperation("车辆记录接口")
    @GetMapping("/get_cars")
    public Result<?> getCars(@RequestParam(defaultValue = "1") Integer pageNum,//默认起始页码为1
                             @RequestParam(defaultValue = "10") Integer pageSize,//默认最大页码为10
                             @RequestParam(defaultValue = "") String search) {//默认空字符串
        LambdaQueryWrapper<Car> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(search)) {
            wrapper.like(Car::getCarNum, search);
        }
        Page<Car> carPage = carService.page(new Page<>(pageNum,pageSize),wrapper);
        return new Result<>().success().data(carPage);
    }

    @ApiOperation("删除车辆记录接口")
    @DeleteMapping("/delete_car/{id}")
    public Result<?> deleteCar(@PathVariable("id") int id){
        carService.removeById(id);
        return new Result<>().success();
    }

    @ApiOperation("订单记录接口")
    @GetMapping("/get_orders")
    public Result<?> getOrders(@RequestParam(defaultValue = "1") Integer pageNum,//默认起始页码为1
                             @RequestParam(defaultValue = "10") Integer pageSize,//默认最大页码为10
                             @RequestParam(defaultValue = "") String search) {//默认空字符串
        LambdaQueryWrapper<OrderInfo> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(search)) {
            wrapper.like(OrderInfo::getPlate, search);
        }
        Page<OrderInfo> orderInfoPage = orderInfoService.page(new Page<>(pageNum,pageSize),wrapper);
        return new Result<>().success().data(orderInfoPage);
    }
}
