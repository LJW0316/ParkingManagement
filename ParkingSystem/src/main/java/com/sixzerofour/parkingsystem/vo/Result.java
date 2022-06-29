package com.sixzerofour.parkingsystem.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {
    private Integer code;//返回码
    private String message;//返回消息
    private Map<String,Object> data = new HashMap<>();

    public Result success(){
        Result result = new Result();
        result.setCode(200);
        result.setMessage("成功");
        return result;
    }

    public Result error(){
        Result result = new Result();
        result.setCode(400);
        result.setMessage("失败");
        return result;
    }

    public Result data(String key,Object value){
        this.data.put(key,value);
        return this;
    }
}
