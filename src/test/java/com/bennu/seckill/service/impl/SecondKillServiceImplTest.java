/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/2/2
 *
 * Class name:SecondKillServiceImplTest
 */
package com.bennu.seckill.service.impl;

import com.bennu.seckill.entity.Seckill;
import com.bennu.seckill.service.SecondKillService;
import com.github.pagehelper.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
@SpringBootTest
class SecondKillServiceImplTest {

    @Autowired
    private SecondKillService secondKillService;

    @Test
    void getSeckillList() {
        Page<Seckill> list = secondKillService.getSeckillList();
        Assert.notEmpty(list);
    }
}