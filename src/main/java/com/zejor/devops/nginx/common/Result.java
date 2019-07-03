package com.zejor.devops.nginx.common;

import lombok.*;

/**
 * <p>通用返回结果封装</p>
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private int code;

    private String msg;

    private T data;



    /**
     * 成功时候的调用
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }


    public static <T> Result<T> getInstance(int code, String msg, T data) {
        Result result = new Result<T>(data);
        result.code = 0;
        result.msg = msg;
        result.data = data;
        return result;
    }


    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

}