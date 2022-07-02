package com.sixzerofour.parkingsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sixzerofour.parkingsystem.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
