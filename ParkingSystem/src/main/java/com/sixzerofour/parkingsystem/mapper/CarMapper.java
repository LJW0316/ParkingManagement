package com.sixzerofour.parkingsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sixzerofour.parkingsystem.bean.Car;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CarMapper extends BaseMapper<Car> {
}
