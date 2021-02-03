package com.bennu.seckill.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/1/25
 *
 * Class name:Seckill
 */
/**
    * 秒杀库存表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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