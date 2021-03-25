/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/1/25
 *
 * Class name:Seckill
 */
package com.bennu.seckill.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 秒杀库存表
 *
 * @author bennu5
 * @version v1.0
 * @date 2021/2/23
 **/
@Data
public class Seckill {
    /**
     * 商品ID
     */
    private Long seckillId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 库存商品数量
     */
    private Integer number;

    /**
     * 秒杀开始时间
     */
    private LocalDateTime startTime;

    /**
     * 秒杀结束时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}