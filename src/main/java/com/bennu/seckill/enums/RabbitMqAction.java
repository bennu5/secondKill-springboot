package com.bennu.seckill.enums;/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/10/27
 *
 * Class name:RabbitMqAction
 */

public enum RabbitMqAction {
    // 处理成功
    ACCATP,
    // 失败重试
    RETRY,
    // 失败抛弃
    REJECT
}
