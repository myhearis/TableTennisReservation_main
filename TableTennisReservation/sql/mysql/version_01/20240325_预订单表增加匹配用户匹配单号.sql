-- 预订单表增加匹配用户匹配单id  用于标识匹配用户的订单跟主单用户的订单区分
ALTER TABLE RESERVE ADD MATCH_USER_MATCH_INFO_ID VARCHAR(50)  COMMENT '匹配用户的匹配单id,匹配用户使用，主单用户留空';
-- 增加逻辑删除字段
ALTER TABLE RESERVE ADD IS_DELETED INT  COMMENT '逻辑删除： 0 未删除 1 删除';