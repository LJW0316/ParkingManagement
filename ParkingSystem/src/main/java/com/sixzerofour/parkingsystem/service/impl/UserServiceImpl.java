package com.sixzerofour.parkingsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sixzerofour.parkingsystem.entity.User;
import com.sixzerofour.parkingsystem.mapper.UserMapper;
import com.sixzerofour.parkingsystem.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
}
