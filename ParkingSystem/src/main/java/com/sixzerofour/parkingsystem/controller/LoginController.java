package com.sixzerofour.parkingsystem.controller;

import com.sixzerofour.parkingsystem.entity.User;
import com.sixzerofour.parkingsystem.service.UserService;
import com.sixzerofour.parkingsystem.vo.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;


@Api(tags="登录")
@RestController
@RequestMapping("/api/user")
public class LoginController {
    @Resource
    UserService userService;

    @PostMapping("/login")
    public Result<?> loginUser(@RequestBody HashMap<String,Object> map){
        List<User> userList=userService.listByMap(map);
        if(!userList.isEmpty()){
            return new Result<>().success().data(userList.get(0).getUsername());
        }
        return new Result<>().error().setMessage("用户名或密码错误");
    }
}
