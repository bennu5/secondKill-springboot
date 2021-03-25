package com.bennu.seckill.dto;


import com.bennu.seckill.entity.SuccessKilled;
import com.bennu.seckill.enums.SeckillStatEnum;
import lombok.Data;

/**
 * 封装秒杀执行后的结果
 *
 * @author bennu5
 * @version v1.0
 * @date 2021/1/20
 **/
@Data
public class SeckillExecution {

    private long seckillId;

    /**
     * 秒杀执行结果状态
     */
    private int state;

    /**
     * 秒杀状态表示
     */
    private String stateInfo;

    /**
     * 秒杀成功对象
     */
    private SuccessKilled successKilled;


    public SeckillExecution(long seckillId, SeckillStatEnum statEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
    }
}
