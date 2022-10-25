package com.mincong.doordashplus.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class ResponseModel<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <T> ResponseModel<T> success(T object) {
        ResponseModel<T> res = new ResponseModel<T>();
        res.data = object;
        res.code = 1;
        return res;
    }


    public static <T> ResponseModel<T> error(String msg) {
        ResponseModel res = new ResponseModel();
        res.msg = msg;
        res.code = 0;
        return res;
    }

    public ResponseModel<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}

