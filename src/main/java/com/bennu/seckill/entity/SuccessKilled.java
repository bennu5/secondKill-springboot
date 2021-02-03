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
 * Class name:SuccessKilled
 */
/**
    * 秒杀成功交易明细表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessKilled {
    /**
    * 秒伤商品ID
    */
    private Long seckillId;

    /**
    * 用户手机号
    */
    private Long userPhone;

    /**
    * 交易创建时间
    */
    private LocalDateTime createTime;

    /**
    * 秒杀状态：-1：无效 0：成功 1 已付款 2：已发货
    */
    private Integer state;
}