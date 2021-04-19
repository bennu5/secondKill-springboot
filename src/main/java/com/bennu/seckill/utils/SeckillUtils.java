/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/4/15
 *
 * Class name:SeckillUtils
 */
package com.bennu.seckill.utils;

import com.bennu.seckill.constant.Constant;
import org.springframework.util.DigestUtils;

/**
 * 功能描述
 *
 * @author bennu5
 * @version v1.0
 * @date 2021/4/15
 **/
public class SeckillUtils {
    public static String getMd5(Long seckillId) {
        String base = seckillId + "/" + Constant.slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
