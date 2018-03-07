-- ----------------------------
-- Table structure for acct_permission
-- ----------------------------
DROP TABLE IF EXISTS acct_permission;
CREATE TABLE acct_permission (
  id int(11) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(50) NOT NULL COMMENT '权限名称',
  module varchar(20) NOT NULL COMMENT '模块',
  seq int(10) NOT NULL,
  permission varchar(30) NOT NULL COMMENT '权限值',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限值表';

-- ----------------------------
-- Table structure for acct_role
-- ----------------------------
DROP TABLE IF EXISTS acct_role;
CREATE TABLE acct_role (
  id int(20) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(30) NOT NULL COMMENT '角色名',
  description varchar(100) DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (id),
  UNIQUE KEY name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Table structure for acct_role_permission
-- ----------------------------
DROP TABLE IF EXISTS acct_role_permission;
CREATE TABLE acct_role_permission (
  role_id int(11) unsigned NOT NULL,
  permission_id int(11) unsigned NOT NULL,
  UNIQUE KEY uqk_role_perm (role_id,permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for acct_user
-- ----------------------------
DROP TABLE IF EXISTS acct_user;
CREATE TABLE acct_user (
  id int(11) unsigned NOT NULL AUTO_INCREMENT,
  username varchar(20) NOT NULL COMMENT '用户名',
  name varchar(20) NOT NULL COMMENT '名字',
  mobile varchar(13) DEFAULT NULL COMMENT '电话',
  position varchar(20) DEFAULT NULL COMMENT '岗位',
  acct_type enum('ADMIN','PROD_SUPPLIER','STORE','FINANCE','MATERIAL_MANAGER','MATERIAL_CLERK','CHAIRMAN_FINANCE','REGION_SUPPLIER') DEFAULT NULL COMMENT '账户类型：管理员|商品供货商|门店|财务人员|区域供应商|材料部总管|材料部下单员|董事长财务',
  supplier_id int(11) DEFAULT NULL COMMENT '区域供应商id 或  商品供货商id',
  store_code int(11) DEFAULT NULL COMMENT '门店id',
  PRIMARY KEY (id),
  UNIQUE KEY uqk_user_code (username)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='系统用户表';

-- ----------------------------
-- Table structure for acct_user_role
-- ----------------------------
DROP TABLE IF EXISTS acct_user_role;
CREATE TABLE acct_user_role (
  user_id int(20) unsigned NOT NULL,
  role_id int(20) unsigned NOT NULL,
  UNIQUE KEY uqk_adm_role (user_id,role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员拥有角色表';

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  code varchar(20) DEFAULT NULL COMMENT '客户代码',
  name varchar(20) NOT NULL COMMENT '客户名字',
  mobile varchar(13) NOT NULL COMMENT '客户手机号',
  store_code int(10) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='客户表';

-- ----------------------------
-- Table structure for customer_contract
-- ----------------------------
DROP TABLE IF EXISTS customer_contract;
CREATE TABLE customer_contract (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  dm_contract_code varchar(30) NOT NULL COMMENT '合同编号',
  customer_id int(11) NOT NULL COMMENT '客户id reference customer(id)',
  house_addr varchar(150) NOT NULL COMMENT '客户装修地址',
  designer varchar(20) NOT NULL COMMENT '设计师',
  designer_mobile varchar(13) DEFAULT NULL,
  supervisor varchar(20) DEFAULT NULL COMMENT '监理',
  supervisor_mobile varchar(13) DEFAULT NULL,
  project_manager varchar(20) DEFAULT NULL COMMENT '项目经理',
  pm_mobile varchar(13) DEFAULT NULL COMMENT '项目经理电话',
  PRIMARY KEY (id),
  UNIQUE KEY uqk_dm_contract_code (dm_contract_code)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='客户装修合同';

-- ----------------------------
-- Table structure for dict_measure_unit
-- ----------------------------
DROP TABLE IF EXISTS dict_measure_unit;
CREATE TABLE dict_measure_unit (
  id int(11) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(30) NOT NULL COMMENT '名称',
  status enum('OPEN','LOCK') NOT NULL COMMENT '启用状态：启用,停用',
  PRIMARY KEY (id),
  UNIQUE KEY uqk_measureunit_name (name)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='计量单位';

-- ----------------------------
-- Table structure for dict_reason
-- ----------------------------
DROP TABLE IF EXISTS dict_reason;
CREATE TABLE dict_reason (
  id int(11) unsigned NOT NULL AUTO_INCREMENT,
  reason varchar(30) NOT NULL COMMENT '原因',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='作废原因';

-- ----------------------------
-- Table structure for ident_order_item_otherfee
-- ----------------------------
DROP TABLE IF EXISTS ident_order_item_otherfee;
CREATE TABLE ident_order_item_otherfee (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  fee_value decimal(18,3) NOT NULL COMMENT '费用 值',
  item_id int(11) NOT NULL COMMENT '订货单项目id',
  fee_type varchar(50) NOT NULL COMMENT '费用类型',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for indent_order
-- ----------------------------
DROP TABLE IF EXISTS indent_order;
CREATE TABLE indent_order (
  id int(11) NOT NULL AUTO_INCREMENT,
  order_code varchar(30) NOT NULL COMMENT '订货单号',
  dm_contract_code varchar(30) NOT NULL COMMENT '合同单号 reference customer_contract(dm_contract_code)',
  status enum('DRAFT','NOTIFIED','REJECT','ALREADY_INSTALLED','REJECTINSTALL','SETTLEACCOUNTS','INSTALLEND_WAITCHECK','INSTALLCHECKNOTPASS','INSTALLCHECKPASS','INVALID') DEFAULT NULL,
  note varchar(200) DEFAULT NULL COMMENT '订货单说明',
  reason varchar(200) DEFAULT NULL COMMENT '选择的作废原因',
  creator int(11) NOT NULL COMMENT '创建人/制单人',
  create_time datetime NOT NULL COMMENT '订单创建时间',
  editor int(11) NOT NULL COMMENT '最后编辑人',
  edit_time datetime DEFAULT NULL COMMENT '最后编辑时间',
  place_status enum('NORMAL','CHANGE') DEFAULT NULL COMMENT '制单类型:克隆下单,正常下单',
  branch_no varchar(50) DEFAULT NULL COMMENT '批次号',
  notice_install_time datetime DEFAULT NULL COMMENT '通知安装时间',
  download_date datetime DEFAULT NULL COMMENT '最后一次下载时间',
  download_number int(11) DEFAULT '0' COMMENT '下载次数',
  accept_status enum('NO','YES') DEFAULT 'NO' COMMENT '备货单接收状态',
  accept_date datetime DEFAULT NULL COMMENT '备货单接收时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='订货单';

-- ----------------------------
-- Table structure for indent_order_install_data
-- ----------------------------
DROP TABLE IF EXISTS indent_order_install_data;
CREATE TABLE indent_order_install_data (
  id int(11) NOT NULL AUTO_INCREMENT,
  install_img varchar(1000) DEFAULT NULL COMMENT '安装图片',
  order_id int(11) DEFAULT NULL COMMENT '订单id',
  remark varchar(1000) DEFAULT NULL COMMENT '驳回原因',
  creator varchar(20) DEFAULT NULL COMMENT '创建人',
  creat_time datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for indent_order_item
-- ----------------------------
DROP TABLE IF EXISTS indent_order_item;
CREATE TABLE indent_order_item (
  id int(11) NOT NULL AUTO_INCREMENT,
  order_id int(11) NOT NULL COMMENT '订单id',
  supplier_id int(11) NOT NULL COMMENT '冗余字段, 方便数据查询',
  sku_id int(11) NOT NULL COMMENT 'skuid',
  sku_name varchar(100) NOT NULL COMMENT 'sku名称',
  model varchar(20) DEFAULT NULL COMMENT '型号',
  spec varchar(20) DEFAULT NULL COMMENT '规格',
  attribute1 varchar(20) DEFAULT NULL COMMENT '属性值：如：白色',
  attribute2 varchar(20) DEFAULT NULL COMMENT '属性值：如：XL',
  attribute3 varchar(20) DEFAULT NULL COMMENT '属性值：如：500兆',
  supply_price decimal(9,2) DEFAULT NULL COMMENT '供货商的供货价',
  quantity decimal(9,2) NOT NULL COMMENT '订货数量',
  install_date date DEFAULT NULL COMMENT '预计安装时间',
  note varchar(150) DEFAULT NULL COMMENT '发货备注说明',
  status enum('PENDING_INSTALLATION','ALREADY_INSTALLED','STAY_STORAGE','STORAGE') DEFAULT NULL COMMENT '商品状态:待安装、已安装、入库',
  sender int(11) DEFAULT NULL COMMENT '发货人',
  send_time datetime DEFAULT NULL COMMENT '发货时间',
  pay_status enum('NOT_PAIED','PAIED') DEFAULT NULL COMMENT '结算状态： 待结算、已结算',
  pay_time datetime DEFAULT NULL COMMENT '对账时间',
  operator int(11) DEFAULT NULL COMMENT '对账人',
  review_size_result varchar(1000) DEFAULT NULL COMMENT '复尺结果',
  has_other_fee tinyint(1) NOT NULL COMMENT '是否有其它费用',
  brand_id int(11) DEFAULT NULL COMMENT '品牌id',
  brand_name varchar(20) DEFAULT NULL COMMENT '品牌名称',
  installation_location varchar(50) DEFAULT NULL COMMENT '安装位置',
  batch varchar(50) DEFAULT NULL COMMENT '批次',
  actual_install_date datetime DEFAULT NULL COMMENT '实际安装时间',
  notice_install_date datetime DEFAULT NULL COMMENT '通知安装时间',
  storage_date datetime DEFAULT NULL COMMENT '入库时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='订货单详细';

-- ----------------------------
-- Table structure for material_base_main
-- ----------------------------
DROP TABLE IF EXISTS material_base_main;
CREATE TABLE material_base_main (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  contract_id int(11) NOT NULL COMMENT '合同id',
  project_name varchar(50) NOT NULL COMMENT '项目名称',
  metarial_type enum('MAIN','BASE') NOT NULL COMMENT '材料类型(‘主材’，''基装'')',
  fee_type enum('COMPREHENSIVE','REDUCE','ADD') NOT NULL COMMENT '费用类型（基装综合，减项，增项）',
  unit varchar(10) NOT NULL COMMENT '单位',
  quantity decimal(18,4) DEFAULT NULL,
  wastage decimal(18,4) DEFAULT NULL,
  price decimal(18,4) DEFAULT NULL,
  main_metarial_price decimal(18,4) DEFAULT NULL,
  accessories_metarial_price decimal(18,4) DEFAULT NULL,
  artificial_fee decimal(18,2) DEFAULT NULL COMMENT '人工费',
  fee_total decimal(18,2) DEFAULT NULL COMMENT '费用合计',
  remarks varchar(1000) DEFAULT NULL,
  create_account varchar(50) NOT NULL COMMENT '创建人',
  create_date datetime DEFAULT NULL COMMENT '创建时间',
  del_account varchar(50) DEFAULT NULL,
  del_date datetime DEFAULT NULL,
  del_status int(1) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='选材基装主材信息';

-- ----------------------------
-- Table structure for material_change
-- ----------------------------
DROP TABLE IF EXISTS material_change;
CREATE TABLE material_change (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  contract_id int(11) DEFAULT NULL COMMENT '合同id',
  change_date datetime DEFAULT NULL COMMENT '变更时间',
  change_order_number varchar(50) DEFAULT NULL COMMENT '变更单序号',
  describation varchar(1000) DEFAULT NULL,
  create_account varchar(20) DEFAULT NULL COMMENT '创建人',
  create_date datetime DEFAULT NULL COMMENT '创建时间',
  del_account varchar(50) DEFAULT NULL,
  del_date datetime DEFAULT NULL,
  del_status int(1) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for material_change_detail
-- ----------------------------
DROP TABLE IF EXISTS material_change_detail;
CREATE TABLE material_change_detail (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  change_id int(11) DEFAULT NULL COMMENT '变更id',
  material_type enum('BASECHANGE','CHANGESUBJECTMATERIAL') DEFAULT NULL COMMENT '材料类型  主材变更     基装变更 ',
  project_name varchar(50) DEFAULT NULL COMMENT '项目名称',
  location varchar(500) DEFAULT NULL,
  brand varchar(500) DEFAULT NULL,
  amount decimal(18,4) DEFAULT NULL,
  unit varchar(20) DEFAULT NULL COMMENT '单位',
  specification varchar(500) DEFAULT NULL,
  model varchar(500) DEFAULT NULL,
  price decimal(18,4) DEFAULT NULL,
  total decimal(18,2) DEFAULT NULL COMMENT '合计',
  description varchar(1000) DEFAULT NULL,
  change_type enum('MINUSITEM','COMPENSATE','INCREASE') DEFAULT NULL,
  create_date datetime DEFAULT NULL COMMENT '创建日期',
  create_account varchar(20) DEFAULT NULL COMMENT '创建人',
  wastage_cost decimal(18,2) DEFAULT NULL COMMENT '损耗费',
  material_cost decimal(18,2) DEFAULT NULL COMMENT '材料费',
  labor_cost decimal(18,2) DEFAULT NULL COMMENT '人工费',
  hole_high double(18,2) DEFAULT NULL COMMENT '洞口尺寸（高）',
  hole_wide double(18,2) DEFAULT NULL COMMENT '洞口尺寸（宽）',
  hole_thuck double(18,2) DEFAULT NULL COMMENT '洞口尺寸（厚）',
  add_stack varchar(500) DEFAULT NULL,
  del_account varchar(50) DEFAULT NULL,
  del_date datetime DEFAULT NULL,
  del_status int(1) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for material_contract_operate_turnover
-- ----------------------------
DROP TABLE IF EXISTS material_contract_operate_turnover;
CREATE TABLE material_contract_operate_turnover (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  contract_id int(11) NOT NULL COMMENT '合同id',
  budget_no varchar(50) NOT NULL COMMENT '预算号',
  project_code varchar(50) NOT NULL COMMENT '项目编号',
  customer_name varchar(50) NOT NULL COMMENT '客户姓名',
  customer_phone varchar(20) NOT NULL COMMENT '客户手机号',
  project_address varchar(1000) DEFAULT NULL,
  budget_area double(18,2) NOT NULL COMMENT '建筑面积',
  meal varchar(500) DEFAULT NULL,
  budget_fee decimal(18,2) DEFAULT NULL,
  house_layout varchar(50) NOT NULL COMMENT '房型',
  designer_name varchar(50) NOT NULL COMMENT '设计师',
  designer_phone varchar(500) DEFAULT NULL,
  customer_service varchar(50) DEFAULT NULL COMMENT '客服人员',
  customer_service_phone varchar(20) DEFAULT NULL COMMENT '客服手机号',
  manager_name varchar(50) DEFAULT NULL COMMENT '项目经理',
  manager_phone varchar(20) DEFAULT NULL COMMENT '项目经理手机号',
  inspector_name varchar(50) DEFAULT NULL COMMENT '质检人员',
  inspector_phone varchar(20) DEFAULT NULL COMMENT '质检人员手机号',
  contract_sign_date datetime NOT NULL COMMENT '合同签订时间',
  plan_start_date date DEFAULT NULL COMMENT '合同计划开工时间',
  plan_finish_date date DEFAULT NULL COMMENT '合同计划竣工时间',
  engineering_cost double(18,2) DEFAULT NULL COMMENT '工程造价',
  dismantle_fee double(18,2) DEFAULT NULL COMMENT '旧房拆改费',
  change_fee double(18,2) DEFAULT NULL COMMENT '变更费',
  have_elevator int(1) DEFAULT NULL COMMENT '是否有电梯',
  remarks varchar(500) DEFAULT NULL,
  operate_time datetime NOT NULL COMMENT '操作时间',
  operate_account varchar(50) NOT NULL COMMENT '操作人',
  operate_type enum('DELETE','UPDATE','ADD') NOT NULL COMMENT '操作类型(删除，更新，增加)',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户合同操作流水';

-- ----------------------------
-- Table structure for material_customer_contract
-- ----------------------------
DROP TABLE IF EXISTS material_customer_contract;
CREATE TABLE material_customer_contract (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  budget_no varchar(50) DEFAULT NULL COMMENT '预算号',
  project_code varchar(50) DEFAULT NULL COMMENT '项目编号',
  customer_name varchar(50) DEFAULT NULL COMMENT '客户姓名',
  customer_phone varchar(50) DEFAULT NULL COMMENT '客户手机号',
  project_address varchar(500) DEFAULT NULL,
  budget_area double(18,2) DEFAULT NULL COMMENT '建筑面积',
  meal varchar(500) DEFAULT NULL,
  budget_fee double(18,2) DEFAULT NULL COMMENT '预算造价',
  house_layout varchar(50) DEFAULT NULL COMMENT '房型',
  designer_name varchar(50) DEFAULT NULL COMMENT '设计师',
  designer_phone varchar(500) DEFAULT NULL,
  customer_service varchar(50) DEFAULT NULL COMMENT '客服人员',
  customer_service_phone varchar(500) DEFAULT NULL,
  manager_name varchar(50) DEFAULT NULL COMMENT '项目经理',
  manager_phone varchar(500) DEFAULT NULL,
  inspector_name varchar(50) DEFAULT NULL COMMENT '质检人员',
  inspector_phone varchar(500) DEFAULT NULL,
  contract_sign_date datetime DEFAULT NULL COMMENT '合同签订时间',
  plan_start_date datetime DEFAULT NULL COMMENT '合同计划开工时间',
  plan_finish_date datetime DEFAULT NULL COMMENT '合同计划竣工时间',
  engineering_cost double(18,2) DEFAULT NULL COMMENT '工程造价',
  dismantle_fee double(18,2) DEFAULT NULL COMMENT '旧房拆改费',
  change_fee double(18,2) DEFAULT NULL COMMENT '变更费',
  have_elevator int(1) DEFAULT NULL COMMENT '是否有电梯',
  remarks varchar(500) DEFAULT NULL,
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  create_account varchar(50) DEFAULT NULL COMMENT '创建人',
  status enum('NOTCHECKED','CHECKED','COMPLETED') DEFAULT NULL COMMENT '合同状态（未核对，已核对，已完成）',
  keyboarder varchar(50) DEFAULT NULL,
  input_time datetime DEFAULT NULL,
  verifier varchar(50) DEFAULT NULL,
  review_time datetime DEFAULT NULL,
  store_code varchar(50) DEFAULT NULL,
  store_name varchar(100) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户合同信息';

-- ----------------------------
-- Table structure for material_customer_contract_confirm
-- ----------------------------
DROP TABLE IF EXISTS material_customer_contract_confirm;
CREATE TABLE material_customer_contract_confirm (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  original_id int(11) NOT NULL COMMENT '原始数据id',
  data_type varchar(50) NOT NULL COMMENT '录入类型（input，check）',
  budget_no varchar(50) DEFAULT NULL COMMENT '预算号',
  project_code varchar(50) DEFAULT NULL COMMENT '项目编号',
  customer_name varchar(50) DEFAULT NULL COMMENT '客户姓名',
  customer_phone varchar(50) DEFAULT NULL COMMENT '客户手机号',
  project_address varchar(1000) DEFAULT NULL,
  budget_area double(18,2) DEFAULT NULL COMMENT '建筑面积',
  meal varchar(500) DEFAULT NULL,
  budget_fee decimal(18,2) DEFAULT NULL COMMENT '预算造价',
  house_layout varchar(50) DEFAULT NULL COMMENT '房型',
  designer_name varchar(50) DEFAULT NULL COMMENT '设计师',
  designer_phone varchar(500) DEFAULT NULL,
  customer_service varchar(50) DEFAULT NULL COMMENT '客服人员',
  customer_service_phone varchar(500) DEFAULT NULL,
  manager_name varchar(50) DEFAULT NULL COMMENT '项目经理',
  manager_phone varchar(500) DEFAULT NULL,
  inspector_name varchar(50) DEFAULT NULL COMMENT '质检人员',
  inspector_phone varchar(500) DEFAULT NULL,
  contract_sign_date datetime DEFAULT NULL COMMENT '合同签订时间',
  plan_start_date datetime DEFAULT NULL COMMENT '合同计划开工时间',
  plan_finish_date datetime DEFAULT NULL COMMENT '合同计划竣工时间',
  engineering_cost double(18,2) DEFAULT NULL COMMENT '工程造价',
  dismantle_fee double(18,2) DEFAULT NULL COMMENT '旧房拆改费',
  change_fee double(18,2) DEFAULT NULL COMMENT '变更费',
  have_elevator int(1) DEFAULT NULL COMMENT '是否有电梯',
  remarks varchar(500) DEFAULT NULL,
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  create_account varchar(50) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户合同完善数据';

-- ----------------------------
-- Table structure for material_house_reform
-- ----------------------------
DROP TABLE IF EXISTS material_house_reform;
CREATE TABLE material_house_reform (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  contract_id int(11) DEFAULT NULL COMMENT '合同id',
  refom_project_name varchar(500) DEFAULT NULL,
  unit varchar(500) DEFAULT NULL,
  quantity decimal(18,4) DEFAULT NULL,
  wastage_quantity decimal(18,4) DEFAULT NULL,
  material_master_fee decimal(18,4) DEFAULT NULL,
  matrial_assist_fee decimal(18,4) DEFAULT NULL,
  man_made_fee decimal(18,2) DEFAULT NULL COMMENT '人工费',
  price decimal(18,4) DEFAULT NULL,
  amount decimal(18,2) DEFAULT NULL COMMENT '合计',
  technology_material_explain varchar(1000) DEFAULT NULL,
  create_account varchar(20) DEFAULT NULL COMMENT '创建人',
  create_date datetime DEFAULT NULL COMMENT '创建时间',
  del_account varchar(50) DEFAULT NULL,
  del_date datetime DEFAULT NULL,
  del_status int(1) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for material_standard_config
-- ----------------------------
DROP TABLE IF EXISTS material_standard_config;
CREATE TABLE material_standard_config (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  contract_id int(11) DEFAULT NULL COMMENT '合同id',
  standard_project_name varchar(500) DEFAULT NULL,
  location varchar(500) DEFAULT NULL,
  brand_meal varchar(500) DEFAULT NULL,
  model varchar(500) DEFAULT NULL,
  spec varchar(500) DEFAULT NULL,
  unit varchar(500) DEFAULT NULL,
  quantity decimal(18,4) DEFAULT NULL,
  wastage_quantity decimal(18,4) DEFAULT NULL,
  actual_quantity decimal(18,4) DEFAULT NULL,
  remark varchar(1000) DEFAULT NULL,
  create_account varchar(20) DEFAULT NULL COMMENT '创建人',
  create_date datetime DEFAULT NULL COMMENT '创建时间',
  del_account varchar(50) DEFAULT NULL,
  del_date datetime DEFAULT NULL,
  del_status int(1) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for operator_log
-- ----------------------------
DROP TABLE IF EXISTS operator_log;
CREATE TABLE operator_log (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  operator varchar(20) DEFAULT NULL COMMENT '操作人',
  operator_time datetime DEFAULT NULL COMMENT '操作时间',
  operator_explain varchar(200) DEFAULT NULL COMMENT '操作说明',
  order_id varchar(50) DEFAULT NULL COMMENT '订单id',
  dm_contract_code varchar(50) DEFAULT NULL COMMENT '项目编号',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='操作日志';

-- ----------------------------
-- Table structure for prod_brand
-- ----------------------------
DROP TABLE IF EXISTS prod_brand;
CREATE TABLE prod_brand (
  id int(11) unsigned NOT NULL AUTO_INCREMENT,
  code varchar(30) NOT NULL COMMENT '品牌编码',
  brand_name varchar(50) NOT NULL COMMENT '品牌名称',
  pinyin_initial varchar(30) NOT NULL COMMENT '中文拼音首字母',
  logo varchar(100) DEFAULT NULL COMMENT '品牌LOGO',
  status enum('OPEN','LOCK') NOT NULL COMMENT '状态',
  description varchar(100) DEFAULT NULL COMMENT '品牌描述',
  editor int(11) NOT NULL COMMENT '最后编辑人',
  edit_time datetime NOT NULL COMMENT '最后编辑时间',
  PRIMARY KEY (id),
  UNIQUE KEY uqk_brand_code (code)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品品牌';

-- ----------------------------
-- Table structure for prod_catalog
-- ----------------------------
DROP TABLE IF EXISTS prod_catalog;
CREATE TABLE prod_catalog (
  id int(11) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(50) NOT NULL COMMENT '分类名称',
  url varchar(40) DEFAULT NULL COMMENT '分类路径,例如：12_24_',
  parent_id int(11) DEFAULT NULL COMMENT '父分类Id',
  seq int(11) unsigned NOT NULL COMMENT '排序：数字越大越靠前',
  status enum('OPEN','LOCK') NOT NULL COMMENT '启用状态',
  editor int(11) NOT NULL COMMENT '最后编辑人',
  editor_time datetime NOT NULL COMMENT '最后编辑时间',
  is_check_scale int(1) DEFAULT NULL COMMENT '是否复尺',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品分类';

-- ----------------------------
-- Table structure for prod_product
-- ----------------------------
DROP TABLE IF EXISTS prod_product;
CREATE TABLE prod_product (
  id int(11) unsigned NOT NULL AUTO_INCREMENT,
  code varchar(30) DEFAULT NULL COMMENT '商品编码',
  name varchar(100) NOT NULL COMMENT '商品名称',
  supplier_id int(11) NOT NULL COMMENT '供应商id',
  category_url varchar(50) NOT NULL COMMENT '商品分类',
  brand_id int(11) NOT NULL COMMENT '品牌id',
  measure_unit_id int(11) NOT NULL COMMENT '计量单位',
  spec varchar(100) DEFAULT NULL,
  model varchar(100) DEFAULT NULL,
  detail text COMMENT '商品详细信息',
  status enum('LIST','DELIST') NOT NULL COMMENT '申请审批状态:已上架、已下架',
  has_sku tinyint(1) NOT NULL DEFAULT '0',
  editor int(11) NOT NULL COMMENT '最后编辑人',
  edit_time datetime DEFAULT NULL COMMENT '最后编辑时间',
  PRIMARY KEY (id),
  UNIQUE KEY uqk_product_code (code,supplier_id) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品信息';

-- ----------------------------
-- Table structure for prod_product_image
-- ----------------------------
DROP TABLE IF EXISTS prod_product_image;
CREATE TABLE prod_product_image (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  product_id int(11) NOT NULL COMMENT '产品id',
  sku_id int(11) DEFAULT NULL COMMENT 'skuId',
  img_path varchar(30) NOT NULL COMMENT '图片保存路径',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='sku图片';

-- ----------------------------
-- Table structure for prod_sku
-- ----------------------------
DROP TABLE IF EXISTS prod_sku;
CREATE TABLE prod_sku (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  code varchar(20) NOT NULL COMMENT 'sku编码',
  name varchar(100) NOT NULL COMMENT 'sku名称',
  product_id int(11) NOT NULL COMMENT '产品id',
  supplier_id int(11) NOT NULL,
  attribute1 varchar(100) DEFAULT NULL,
  attribute2 varchar(100) DEFAULT NULL,
  attribute3 varchar(100) DEFAULT NULL,
  process_status varchar(50) DEFAULT NULL COMMENT '流程状态',
  stock int(11) DEFAULT NULL COMMENT '库存数',
  PRIMARY KEY (id),
  UNIQUE KEY uqk_sku_code (code,supplier_id) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='sku表';

-- ----------------------------
-- Table structure for prod_sku_approval_record
-- ----------------------------
DROP TABLE IF EXISTS prod_sku_approval_record;
CREATE TABLE prod_sku_approval_record (
  id int(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  approval_account varchar(50) DEFAULT NULL COMMENT '审批人账号',
  approval_result varchar(50) DEFAULT NULL COMMENT '审批结果',
  approval_note varchar(500) DEFAULT NULL COMMENT '审批说明',
  sku_id int(20) DEFAULT NULL COMMENT 'sku 外键',
  approval_time datetime DEFAULT NULL COMMENT '审批时间',
  approval_node varchar(50) DEFAULT NULL COMMENT '审批节点(如:1:草稿,2:待区域供应商审批,3:区域供应商驳回等)参考数据字典',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for prod_sku_meta
-- ----------------------------
DROP TABLE IF EXISTS prod_sku_meta;
CREATE TABLE prod_sku_meta (
  product_id int(11) NOT NULL,
  attribute1_name varchar(50) DEFAULT NULL,
  attribute2_name varchar(50) DEFAULT NULL,
  attribute3_name varchar(50) DEFAULT NULL,
  UNIQUE KEY sku_meta_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='sku元数据';

-- ----------------------------
-- Table structure for prod_sku_price
-- ----------------------------
DROP TABLE IF EXISTS prod_sku_price;
CREATE TABLE prod_sku_price (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  sku_id int(11) NOT NULL COMMENT 'skuId',
  price_type enum('SUPPLY','STORE','SALE') NOT NULL COMMENT 'SUPPLY("网真采购价"), STORE("门店采购价"), SALE("门店销售价")',
  price decimal(18,3) NOT NULL COMMENT '价格',
  price_start_date date NOT NULL,
  editor int(11) NOT NULL COMMENT '最后编辑人',
  edit_time datetime NOT NULL COMMENT '最后编辑时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='sku图片';

-- ----------------------------
-- Table structure for prod_supplier
-- ----------------------------
DROP TABLE IF EXISTS prod_supplier;
CREATE TABLE prod_supplier (
  id int(11) unsigned NOT NULL AUTO_INCREMENT,
  code varchar(30) NOT NULL COMMENT '供应商编码',
  name varchar(50) NOT NULL COMMENT '供应商名称',
  pinyin_initial varchar(30) NOT NULL COMMENT '中文拼音首字母',
  company_phone varchar(50) DEFAULT NULL COMMENT '公司电话',
  company_address varchar(100) DEFAULT NULL COMMENT '公司地址',
  contactor varchar(30) DEFAULT NULL COMMENT '联系人',
  mobile varchar(11) DEFAULT NULL COMMENT '联系人手机',
  region_supplier_id int(11) NOT NULL COMMENT '所属区域供应商id reference region_supplier(id)',
  status enum('OPEN','LOCK') NOT NULL COMMENT '启用状态:停用、启用',
  description varchar(300) DEFAULT NULL COMMENT '供应商描述',
  editor int(11) NOT NULL COMMENT '最后编辑人',
  edit_time datetime NOT NULL COMMENT '最后编辑时间',
  supplier_abbreviation varchar(100) DEFAULT NULL COMMENT '供应商简称',
  cooperative_brand_name varchar(100) DEFAULT NULL COMMENT '合作品牌名称',
  manager varchar(30) DEFAULT NULL COMMENT '负责人',
  manager_mobile varchar(30) DEFAULT NULL COMMENT '负责人电话',
  business_manager varchar(30) DEFAULT NULL COMMENT '业务负责人',
  business_manager_mobile varchar(30) DEFAULT NULL COMMENT '业务负责人电话',
  opening_bank varchar(30) DEFAULT NULL COMMENT '开户行',
  account_number varchar(30) DEFAULT NULL COMMENT '账号',
  tax_registration_certificate_image_url varchar(100) DEFAULT NULL COMMENT '税务登记证图片',
  business_license_image_url varchar(100) DEFAULT NULL COMMENT '营业执照图片',
  taxpayer_identification_number varchar(100) DEFAULT NULL COMMENT '纳税人识别号',
  PRIMARY KEY (id),
  UNIQUE KEY uqk_supplier_code (code)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品供货商';

-- ----------------------------
-- Table structure for region_supplier
-- ----------------------------
DROP TABLE IF EXISTS region_supplier;
CREATE TABLE region_supplier (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  region_name varchar(20) NOT NULL COMMENT '区域供应商名称',
  store_code int(11) NOT NULL COMMENT '所属门店',
  status enum('LOCK','OPEN') NOT NULL COMMENT '停用，启用',
  PRIMARY KEY (id),
  KEY uqk_region_name (region_name)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='区域供应商';

-- ----------------------------
-- Table structure for review_size_notice
-- ----------------------------
DROP TABLE IF EXISTS review_size_notice;
CREATE TABLE review_size_notice (
  id int(11) NOT NULL AUTO_INCREMENT,
  supplier_id varchar(50) DEFAULT NULL COMMENT '供应商',
  contract_id varchar(50) DEFAULT NULL COMMENT '项目',
  brand_id varchar(50) DEFAULT NULL COMMENT '品牌',
  order_id varchar(50) DEFAULT NULL COMMENT '订货单',
  img_url varchar(500) DEFAULT NULL COMMENT '图片',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  creat_name varchar(20) DEFAULT NULL COMMENT '创建人',
  creat_time datetime DEFAULT NULL COMMENT '创建时间',
  review_status enum('REJECT','YESRIVEEWSIZE','NORIVEEWSIZE') DEFAULT NULL COMMENT '复尺状态',
  prod_catalog_id int(11) DEFAULT NULL COMMENT '品牌id',
  notice_time datetime DEFAULT NULL COMMENT '通知时间',
  upload_url varchar(50) DEFAULT NULL COMMENT '上传文件路径',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for review_size_result
-- ----------------------------
DROP TABLE IF EXISTS review_size_result;
CREATE TABLE review_size_result (
  id int(11) NOT NULL AUTO_INCREMENT,
  product_name varchar(20) DEFAULT NULL COMMENT '商品名称',
  location varchar(20) DEFAULT NULL COMMENT '安装位置',
  model varchar(20) DEFAULT NULL COMMENT '型号',
  specification varchar(20) DEFAULT NULL COMMENT '规格',
  unit varchar(10) DEFAULT NULL COMMENT '单位',
  count double(18,3) DEFAULT NULL COMMENT '数量',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  creater varchar(20) DEFAULT NULL COMMENT '创建人',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  contract_id varchar(50) DEFAULT NULL COMMENT '项目id',
  review_size_notice_id int(11) DEFAULT NULL COMMENT '复尺单id',
  prod_catalog_id int(11) DEFAULT NULL COMMENT '商品类目id',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sequence
-- ----------------------------
DROP TABLE IF EXISTS sequence;
CREATE TABLE sequence (
  id int(11) unsigned NOT NULL AUTO_INCREMENT,
  tab varchar(30) NOT NULL,
  start_val int(6) NOT NULL,
  incr_by int(6) NOT NULL,
  cur_val int(11) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uqk_seq_table (tab)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for store
-- ----------------------------
DROP TABLE IF EXISTS store;
CREATE TABLE store (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(20) NOT NULL COMMENT '门店名称',
  PRIMARY KEY (id),
  UNIQUE KEY uqk_region_suppliername (name)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='门店';

-- ----------------------------
-- Table structure for supplier_reject_record
-- ----------------------------
DROP TABLE IF EXISTS supplier_reject_record;
CREATE TABLE supplier_reject_record (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  source_id int(10) NOT NULL COMMENT '订货单id/通知单id',
  source_type int(1) NOT NULL COMMENT '订货单（1）/通知单（2）',
  reject_reason varchar(250) DEFAULT NULL COMMENT '驳回原因',
  reject_type varchar(50) DEFAULT NULL COMMENT '驳回类型',
  creator int(11) NOT NULL COMMENT '创建人',
  create_time datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='供应商驳回记录';

-- ----------------------------
-- Table structure for system_dictionary
-- ----------------------------
DROP TABLE IF EXISTS system_dictionary;
CREATE TABLE system_dictionary (
  id int(20) NOT NULL AUTO_INCREMENT,
  dic_name varchar(50) DEFAULT NULL COMMENT '字典名称',
  dic_value varchar(50) DEFAULT NULL COMMENT '字典对应的值',
  sort int(20) DEFAULT NULL COMMENT '排序',
  parent_id int(20) DEFAULT NULL COMMENT '父id',
  remarks varchar(80) DEFAULT NULL COMMENT '备注 ',
  status varchar(2) DEFAULT '0' COMMENT '是否删除 0 未删除 1 删除',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  create_account varchar(50) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
