package com.lcx.common;


import lombok.Data;
import lombok.ToString;

//统一返回结果的类
@ToString
@Data
public class Result<T> {
    //状态码

    public static final Integer SUCCESS_CODE = 200;
    public static final Integer ERROR_CODE = 500;
    private Integer status = 200;
    //消息
    private String message;

    private T data = null;

    public static <T> Result<T> ok() {
        return new Result<>(SUCCESS_CODE, "操作成功", null);
    }


    public static <T> Result<T> ok(Integer status, String message) {
        return new Result<>();
    }


    public static <T> Result<T> ok(T data) {
        return new Result<>("操作成功", data);
    }

    public static <T> Result<T> ok(String msg, T data) {
        return new Result<>(msg, data);
    }

    public static <T> Result<T> ok(Integer status, String msg, T data) {
        return new Result<>(SUCCESS_CODE, msg, data);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(ERROR_CODE, message, null);
    }

    public static <T> Result<T> fail(Integer status, String msg) {
        return new Result<>(ERROR_CODE, msg, null);
    }

    public static <T> Result<T> fail() {
        return new Result<>(ERROR_CODE, "操作失败", null);
    }

    public Result() {

    }

    public Result(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Result(T data) {
        this.data = data;
    }

    public Result(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public Result(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}