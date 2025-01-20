CREATE  TABLE MENU(
		GUID VARCHAR(50) PRIMARY KEY COMMENT 'ID主键',
		PARENT_ID VARCHAR(50) COMMENT '父节点ID',
		IS_LEAF INT COMMENT '是否为叶子节点 0 否 1 是',
		NAME VARCHAR(100) COMMENT '菜单名称',
		CODE VARCHAR(50)  COMMENT '菜单编码',
		SORT INT COMMENT '排序字段(数值越小，优先级越高)',
		TYPE INT  COMMENT '类型 1: HEADR  菜单头 2: ITEM 菜单项'
);
-- 菜单表增加url字段
ALTER TABLE MENU ADD URL VARCHAR(100)  COMMENT '请求路径';