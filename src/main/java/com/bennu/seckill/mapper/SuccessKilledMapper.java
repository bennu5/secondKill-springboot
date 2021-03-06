package com.bennu.seckill.mapper;

import com.bennu.seckill.entity.SuccessKilled;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/1/25
 *
 * Class name:SuccessKilledMapper
 */
@Mapper
public interface SuccessKilledMapper {
    /**
     * delete by primary key
     * @param seckillId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(@Param("seckillId") Long seckillId, @Param("userPhone") Long userPhone);

    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(SuccessKilled record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(SuccessKilled record);

    /**
     * select by primary key
     * @param seckillId primary key
     * @return object by primary key
     */
    SuccessKilled selectByPrimaryKey(@Param("seckillId") Long seckillId, @Param("userPhone") Long userPhone);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(SuccessKilled record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(SuccessKilled record);

    int updateBatch(List<SuccessKilled> list);

    int batchInsert(@Param("list") List<SuccessKilled> list);
}