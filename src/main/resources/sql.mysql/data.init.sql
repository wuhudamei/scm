INSERT INTO acct_role(id,name,description) values (1,'administrator','系统内置超级管理员');


INSERT INTO acct_permission VALUES ('1', '所有权限', 'embed',0, '*');
INSERT INTO acct_permission VALUES ('2', '角色管理', '首页', 6,'system:rolemaintain');
INSERT INTO acct_permission VALUES ('3', '用户管理', '首页', 6,'system:usermaintain');
INSERT INTO acct_permission VALUES ('4', '留言管理', '首页', 6,'system:levelmsgmaintain');
INSERT INTO acct_permission VALUES ('5', '客户管理', '首页', 6,'system:gusermaintain');

INSERT INTO acct_permission VALUES ('6', '主页', '首页', 6,'system:home');
INSERT INTO acct_permission VALUES ('7', 'banner管理', '首页', 6,'system:banner');
INSERT INTO acct_permission VALUES ('8', '品牌管理', '首页', 6,'system:brand');
INSERT INTO acct_permission VALUES ('9', '分类管理', '首页', 6,'system:catalog');
INSERT INTO acct_permission VALUES ('10', '供应商管理', '首页', 6,'system:supplier');
INSERT INTO acct_permission VALUES ('11', '商品管理', '首页', 6,'system:product');
INSERT INTO acct_permission VALUES ('12', '商品采购价管理', '首页', 6,'system:proprice');
INSERT INTO acct_permission VALUES ('13', '商品浏览统计', '首页', 6,'system:statis');

INSERT INTO prod_catalog (id, name, code, attention_real, status, create_time, edit_user_id, edit_time)
VALUES
	(1,'生活电器','shdq',0,'1','2016-12-16 13:15:17',1,'2016-12-16 13:15:17'),
	(2,'厨电','chudian',0,'1','2016-12-16 13:16:38',1,'2016-12-16 13:16:38'),
	(3,'个护','gehu',0,'1','2016-12-16 13:16:27',1,'2016-12-16 13:16:27'),
	(4,'家居','jiaju',0,'1','2016-12-16 13:16:18',1,'2016-12-16 13:16:18'),
	(5,'厨具','chuju',0,'1','2016-12-16 13:16:09',1,'2016-12-16 13:16:09'),
	(6,'商旅','shanglv',0,'1','2016-12-16 13:17:03',1,'2016-12-16 13:17:03'),
	(7,'户外','huwai',0,'1','2016-12-16 13:17:03',1,'2016-12-16 13:17:03'),
	(8,'电子','dianzi',0,'1','2016-12-16 13:17:42',1,'2016-12-16 13:17:42'),
	(9,'汽车','qiche',0,'1','2016-12-16 13:17:51',1,'2016-12-16 13:17:51'),
	(10,'母婴','muying',0,'1','2016-12-16 13:18:03',1,'2016-12-16 13:18:03'),
	(11,'精品','jingpin',0,'1','2016-12-16 13:18:17',1,'2016-12-16 13:18:17'),
	(12,'卡券','kaquan',0,'1','2016-12-16 13:18:27',1,'2016-12-16 13:18:27'),
	(13,'虚拟','xuni',0,'1','2016-12-16 13:18:37',1,'2016-12-16 13:18:37');


INSERT INTO acct_role_permission(role_id,permission_id) VALUES (1,1);
INSERT INTO acct_user(id,username,pwd,salt,name,position,acct_type,status) VALUES (1,'admin','3195993f8ce9306a24a8b9cba0389c5da05c5e16','70e58484fcf228fa','admin','系统内置管理员','ADMIN','OPEN');
INSERT INTO acct_user_role(user_id,role_id) VALUES (1,1);