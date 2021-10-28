package com.yostar.uniqueid.model;

public class BaseRsp<T> {
    public int code;
    public String msg;
    public T data;


    public BaseRsp(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
