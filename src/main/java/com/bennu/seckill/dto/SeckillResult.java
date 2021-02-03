package com.bennu.seckill.dto;

import lombok.Data;

/**
 * 封装数据结果的 VO：
 * 所有ajax请求返回类型，封装 Json 结果
 *
 * @author bennu5
 * @version v1.0
 * @date 2021/1/20
 **/
@Data
public class SeckillResult<T> {
    private boolean success;

    private T data;

    private String error;

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }
}
