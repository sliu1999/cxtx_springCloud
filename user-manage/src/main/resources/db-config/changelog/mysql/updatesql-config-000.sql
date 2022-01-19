-- usermanage 0.0.1 字典表创建 --
-- 性别 --
CREATE TABLE IF NOT EXISTS  `tb_dic_sex` (
  `ID` int(9) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `SORT` int(9) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工性别字典表';
 ##
INSERT INTO tb_dic_sex (ID,NAME,SORT) VALUES (0,'女',0) ##
INSERT INTO tb_dic_sex (ID,NAME,SORT) VALUES (1,'男',1) ##


-- 在线状态 --
CREATE TABLE IF NOT EXISTS `tb_dic_online` (
  `ID` int(9) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `SORT` int(9) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户在线状态字典表';
 ##
INSERT INTO tb_dic_online (ID,NAME,SORT) VALUES (0,'在线',0) ##
INSERT INTO tb_dic_online (ID,NAME,SORT) VALUES (1,'离线',1) ##

-- 菜单类型 --
CREATE TABLE IF NOT EXISTS `tb_dic_menu_type` (
  `ID` int(9) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `SORT` int(9) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单类型字典表';
 ##
INSERT INTO tb_dic_menu_type (ID,NAME,SORT) VALUES (0,'模块',0) ##
INSERT INTO tb_dic_menu_type (ID,NAME,SORT) VALUES (1,'权限',1) ##

-- 系统类型 --
CREATE TABLE IF NOT EXISTS `tb_dic_sys_type` (
  `ID` int(9) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `SORT` int(9) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统类型字典表';
 ##
INSERT INTO tb_dic_sys_type (ID,NAME,SORT) VALUES (0,'PC版',0) ##
INSERT INTO tb_dic_sys_type (ID,NAME,SORT) VALUES (1,'手机版',1) ##
INSERT INTO tb_dic_sys_type (ID,NAME,SORT) VALUES (2,'pad版',2) ##
INSERT INTO tb_dic_sys_type (ID,NAME,SORT) VALUES (3,'电视盒子版',3) ##
INSERT INTO tb_dic_sys_type (ID,NAME,SORT) VALUES (4,'触摸屏版',4) ##

-- 在职状态 --
CREATE TABLE IF NOT EXISTS  `tb_dic_staff_type` (
  `ID` int(9) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `TYPE` int(9) NOT NULL COMMENT '是否在职状态',
  `SORT` int(9) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工类型字典表';
##
INSERT INTO tb_dic_staff_type (ID,NAME,TYPE,SORT) VALUES (0,'兼职',1,0) ##
INSERT INTO tb_dic_staff_type (ID,NAME,TYPE,SORT) VALUES (1,'实习',1,1) ##
INSERT INTO tb_dic_staff_type (ID,NAME,TYPE,SORT) VALUES (2,'试用',1,2) ##
INSERT INTO tb_dic_staff_type (ID,NAME,TYPE,SORT) VALUES (3,'全职',1,3) ##
INSERT INTO tb_dic_staff_type (ID,NAME,TYPE,SORT) VALUES (4,'离职',0,4) ##

-- 字典表
CREATE TABLE IF NOT EXISTS `tb_sys_dictionary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父ID',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `level` int(11) DEFAULT NULL COMMENT '级别',
  `sort` bigint(20) DEFAULT NULL COMMENT '排序',
  `status` int(11) DEFAULT '1' COMMENT '数据状态 1可用，0不可用',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表';
##

