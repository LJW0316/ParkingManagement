package com.sixzerofour.parkingsystem.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result<T> {
    private Integer code;//返回码
    private String message;//返回消息
    private T data;

    public Result<?> success(){
        Result result = new Result();
        result.setCode(200);
        result.setMessage("成功");
        return result;
    }

    public Result<?> error(){
        Result result = new Result();
        result.setCode(400);
        result.setMessage("失败");
        return result;
    }

    public Result<?> carNotFound(){
        Result result = new Result();
        result.setCode(-1);
        result.setMessage("车辆不在场内");
        return result;
    }

    public Result<?> stillInError(){
        Result result = new Result();
        result.setCode(-1);
        result.setMessage("车辆仍在场内");
        return result;
    }


    public Result<?> data(T data){
        setData(data);
        return this;
    }
}
