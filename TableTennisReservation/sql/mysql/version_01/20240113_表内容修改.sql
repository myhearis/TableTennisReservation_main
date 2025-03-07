-- 用户表增加邮箱字段
ALTER TABLE CAUSUER ADD EMAIL VARCHAR(100) COMMENT '用户邮箱';
-- 预订单增加主键和球桌编号
ALTER TABLE RESERVE ADD GUID VARCHAR(50) PRIMARY KEY COMMENT '预订单主键';
ALTER TABLE RESERVE ADD TABLE_CODE VARCHAR(20)  COMMENT '球桌编号';

-- 给预订单表增加计算列，用于直接获取年-月-日
ALTER TABLE RESERVE
ADD COLUMN PROCESS_START_DATE VARCHAR(10) AS (SUBSTRING(START_DATE , 1, 10));