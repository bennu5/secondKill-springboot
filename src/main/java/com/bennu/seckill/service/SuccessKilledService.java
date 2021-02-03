package com.bennu.seckill.service;

import java.util.List;
import com.bennu.seckill.entity.SuccessKilled;
    /*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/1/25
 *
 * Class name:SuccessKilledService
 */
public interface SuccessKilledService{


    int deleteByPrimaryKey(Long seckillId,Long userPhone);

    int insert(SuccessKilled record);

    int insertSelective(SuccessKilled record);

    SuccessKilled selectByPrimaryKey(Long seckillId,Long userPhone);

    int updateByPrimaryKeySelective(SuccessKilled record);

    int updateByPrimaryKey(SuccessKilled record);

    int updateBatch(List<SuccessKilled> list);

    int batchInsert(List<SuccessKilled> list);

}
