-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE seckill;
-- 使用数据库
USE seckill;
-- 创建秒杀库存表
CREATE TABLE seckill
(
  `seckill_id`  bigint       NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name`        varchar(120) NOT NULL COMMENT '商品名称',
  `number`      int          NOT NULL COMMENT '库存商品数量',
  `start_time`  timestamp    NOT NULL DEFAULT 0 COMMENT '秒杀开始时间',
  `end_time`    timestamp    NOT NULL DEFAULT 0 COMMENT '秒杀结束时间',
  `create_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (seckill_id),
  key idx_start_time (start_time),
  key idx_end_time (end_time),
  key idx_create_time (create_time)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT ='秒杀库存表';
-- 初始化数据
INSERT INTO seckill(name, number, start_time, end_time)
VALUES ('400元秒杀iPhone', 100, '2021-01-10 00:00:00', '2021-03-20 00:00:00'),
       ('300元秒杀iPad', 200, '2021-01-10 00:00:00', '2021-03-20 00:00:00'),
       ('200元秒杀Honor8', 300, '2021-01-20 00:00:00', '2021-03-20 00:00:00'),
       ('100元秒杀Mi8', 400, '2021-01-20 00:00:00', '2021-03-20 00:00:00');
-- 秒杀成功交易明细表
-- 用户登录认证相关信息
CREATE TABLE success_killed
(
  `seckill_id`  bigint    NOT NULL COMMENT '秒伤商品ID',
  `user_phone`  bigint    NOT NULL COMMENT '用户手机号',
  `create_time` timestamp NOT NULL COMMENT '交易创建时间',
  `state`       tinyint   NOT NULL DEFAULT -1 COMMENT '秒杀状态：-1：无效 0：成功 1 已付款 2：已发货',
  PRIMARY KEY (seckill_id, user_phone),/*联合主键*/
  key idx_create_time (create_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='秒杀成功交易明细表';
-- 连接数据库控制台
