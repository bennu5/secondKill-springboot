package com.bennu.seckill.mapper;

import com.bennu.seckill.entity.Seckill;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/1/25
 *
 * Class name:SeckillMapper
 */
@Mapper
public interface SeckillMapper {
    /**
     * delete by primary key
     *
     * @param seckillId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Long seckillId);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(Seckill record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(Seckill record);

    /**
     * select by primary key
     *
     * @param seckillId primary key
     * @return object by primary key
     */
    Seckill selectByPrimaryKey(Long seckillId);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(Seckill record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(Seckill record);

    int updateBatch(List<Seckill> list);

    int batchInsert(@Param("list") List<Seckill> list);

    /**
     * 分页查询秒杀记录
     *
     * @return 分页秒杀记录
     */
    Page<Seckill> getSeckillList();
}