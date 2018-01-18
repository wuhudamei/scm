-- 区域供应商表  新增所属  门店列

-- 新增 门店表

-- 用户表，加了 门店管理员 枚举

-- product 表 新增  是否有含sku列

-- sku表 删除价格相关的字段

-- 新增 prod_sku_price 表

--2017/10/26 10:59
--修改人：Paul
--描  述：将原来store_id 都改为store_code
--执行状态: 未执行
alter table store add code VARCHAR(20) COMMENT '门店编码';
alter table acct_user change store_id store_code varchar(20) COMMENT '门店编码';
alter table customer change store_id store_code varchar(20) COMMENT '门店编码';
alter table material_customer_contract change store_id store_code varchar(20) COMMENT '门店编码';
alter table region_supplier change store_id store_code varchar(20) COMMENT '门店编码';
--平米转片
alter table indent_order_item ADD spec_unit varchar(20) comment '商品单位';
alter table indent_order_item ADD tablet_num int(11) comment '片数';
alter table prod_catalog ADD convert_unit  varchar(50) comment '计量单位转换';
--2017/11/15
--修改人：巢帅
--描述：基装数据录入合同表添加完成时间字段
--执行状态：已执行
alter table material_customer_contract ADD column complete_date date comment '竣工时间';
--2018/01/05
--修改人：Paul
--描述：修改备货单表 最后编辑者 可为null
--执行状态：未执行
alter table indent_order MODIFY column editor int(11) DEFAULT NULL comment '最后编辑人';