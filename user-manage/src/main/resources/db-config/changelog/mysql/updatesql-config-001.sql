-- usermanage 0.0.1系统管理 业务表创建 --

-- 依赖表 --
CREATE TABLE IF NOT EXISTS  `tb_sys_attached_tab` (
  `ID` varchar(10) NOT NULL,
  `TABLE_NAME` varchar(50) DEFAULT NULL COMMENT '表名',
  `REMARK` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='附件 信息表';
##

-- 系统信息 --
CREATE TABLE IF NOT EXISTS  `tb_sys_info` (
  `ID` varchar(36) NOT NULL,
  `CN_NAME` varchar(300) DEFAULT NULL,
  `EN_NAME` varchar(300) DEFAULT NULL,
  `IP` varchar(100) DEFAULT NULL,
  `PORT` varchar(10) DEFAULT NULL,
  `VERSION` varchar(100) DEFAULT NULL,
  `COPY_RIGHT` varchar(300) DEFAULT NULL COMMENT '版权信息',
  `URL` varchar(1000) DEFAULT NULL,
  `SYS_TYPE` int(9) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统信息表';
##

-- 菜单管理 --
-- CREATE TABLE IF NOT EXISTS  `tb_sys_menu` (
--   `ID` varchar(36) NOT NULL,
--   `NAME` varchar(100) DEFAULT NULL COMMENT '菜单名称',
--   `ACTION` varchar(100) DEFAULT NULL COMMENT '菜单action',
--   `SORT` int(9) DEFAULT NULL,
--   `PARENT_ID` varchar(36) DEFAULT NULL COMMENT '父级菜单id',
--   `STYLE` varchar(300) DEFAULT NULL COMMENT '菜单样式',
--   `MENU_TYPE` int(9) DEFAULT NULL COMMENT '菜单类型',
--   PRIMARY KEY (`ID`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';
CREATE TABLE IF NOT EXISTS `tb_sys_menu` (
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `PARENT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '父级菜单id',
  `PATH` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单路径',
  `COMPONENT` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '视图组件',
  `REDIRECT` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '跳转路径',
  `TITLE` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '面包屑展示',
  `NAME` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `ICON` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单图标',
  `HIDDEN` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单是否隐藏',
  `SORT` int(9) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理端菜单';


 ##

DROP FUNCTION IF EXISTS getMenuChildList; ##

CREATE FUNCTION getMenuChildList(rootId VARCHAR(36))
RETURNS VARCHAR(4000)
BEGIN
    DECLARE sTemp VARCHAR(4000);
    DECLARE sTempChd VARCHAR(4000);

    SET sTemp = '$';
    SET sTempChd =CAST(rootId as CHAR);

    WHILE sTempChd is not null DO
    	SET sTemp = concat(sTemp,',',sTempChd);
        SELECT group_concat(ID) INTO sTempChd FROM tb_sys_menu WHERE FIND_IN_SET(PARENT_ID,sTempChd)>0;
   	END WHILE;
    RETURN sTemp;
END; ##

-- 角色管理 --
CREATE TABLE IF NOT EXISTS `tb_sys_role` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `REMARK` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';
##

CREATE TABLE IF NOT EXISTS `tb_sys_role_r_menu` (
  `ID` varchar(36) NOT NULL,
  `ROLE_ID` varchar(36) DEFAULT NULL,
  `MENU_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色跟菜单关联表';##

-- 用户管理 --
CREATE TABLE IF NOT EXISTS `tb_sys_user` (
  `ID` bigint(36) NOT NULL AUTO_INCREMENT,
  `LOGIN_ID` varchar(100) NOT NULL,
  `PASSWORD` varchar(100) DEFAULT NULL COMMENT 'MD5加密后的密码',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `ONLINE_ID` int(9) DEFAULT NULL COMMENT '在线状态id',
  `REMARK` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='系统用户表';

##

CREATE TABLE IF NOT EXISTS `tb_sys_user_r_role` (
  `ID` varchar(36) NOT NULL,
  `LOGIN_ID` varchar(100) DEFAULT NULL,
  `ROLE_ID` varchar(36) DEFAULT NULL,
  `IS_DEFAULT` int(9) DEFAULT NULL COMMENT '是否为默认角色',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户关联角色表';##

-- 部门管理 --
CREATE TABLE IF NOT EXISTS `tb_sys_department` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(300) DEFAULT NULL,
  `PARENT_ID` varchar(36) DEFAULT NULL COMMENT '父级部门ID',
  `SORT` int(9) DEFAULT NULL,
  `REMARK` varchar(300) DEFAULT NULL COMMENT '名称',
  `LEADER` varchar(100) DEFAULT NULL COMMENT '部门负责人id',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门表';##

DROP FUNCTION IF EXISTS getDepartChildList; ##

CREATE FUNCTION getDepartChildList(rootId VARCHAR(36))
RETURNS VARCHAR(4000)
BEGIN
    DECLARE sTemp VARCHAR(4000);
    DECLARE sTempChd VARCHAR(4000);

    SET sTemp = '$';
    SET sTempChd =CAST(rootId as CHAR);

    WHILE sTempChd is not null DO
    	SET sTemp = concat(sTemp,',',sTempChd);
        SELECT group_concat(ID) INTO sTempChd FROM tb_sys_department WHERE FIND_IN_SET(PARENT_ID,sTempChd)>0;
   	END WHILE;
    RETURN sTemp;
END; ##

-- 员工管理 --
CREATE TABLE IF NOT EXISTS `tb_sys_staff` (
  `ID` varchar(36) NOT NULL,
  `STAFF_NO` varchar(100) DEFAULT NULL COMMENT '工号',
  `LOGIN_ID` varchar(100) DEFAULT NULL COMMENT '登录账号',
  `NAME` varchar(100) DEFAULT NULL COMMENT '姓名',
  `PHONE` varchar(15) DEFAULT NULL COMMENT '电话',
  `OFFICE_PHONE` varchar(15) DEFAULT NULL COMMENT '固定电话',
  `ENTRY_DATE` varchar(10) DEFAULT NULL COMMENT '入职时间',
  `IDENTITY_CARD` varchar(20) DEFAULT NULL COMMENT '证件号',
  `ADDRESS` varchar(1000) DEFAULT NULL COMMENT '地址',
  `BIRTHDAY` varchar(10) DEFAULT NULL COMMENT '生日',
  `SEX` int(9) DEFAULT NULL,
  `STAFF_TYPE` int(9) DEFAULT NULL COMMENT '员工类型编码',
  `SORT` int(9) DEFAULT NULL,
  `PIN_YIN` varchar(100) DEFAULT NULL COMMENT '姓名拼音',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `LEADER` varchar(100) DEFAULT NULL COMMENT '直属上级id',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工信息表';##

DROP FUNCTION IF EXISTS getStaffChildList; ##

CREATE FUNCTION getStaffChildList(rootId VARCHAR(36))
RETURNS VARCHAR(4000)
BEGIN
    DECLARE sTemp VARCHAR(4000);
    DECLARE sTempChd VARCHAR(4000);

    SET sTemp = '$';
    SET sTempChd =CAST(rootId as CHAR);

    WHILE sTempChd is not null DO
    	SET sTemp = concat(sTemp,',',sTempChd);
        SELECT group_concat(LOGIN_ID) INTO sTempChd FROM tb_sys_staff WHERE FIND_IN_SET(LEADER,sTempChd)>0;
   	END WHILE;
    RETURN sTemp;
END; ##

CREATE TABLE IF NOT EXISTS `tb_sys_staff_icon_card` (
  `ID` varchar(36) NOT NULL,
  `MAIN_ID` varchar(36) DEFAULT NULL,
  `FILE_NAME` varchar(1000) DEFAULT NULL,
  `SAVE_NAME` varchar(1000) DEFAULT NULL,
  `FILE_TYPE` varchar(1000) DEFAULT NULL,
  `UPLOAD_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `DIR` varchar(1000) DEFAULT NULL,
  `SYSTEM_NAME` varchar(1000) DEFAULT NULL,
  `IS_COVER` int(9) DEFAULT NULL,
  `ORDER_NUM` int(9) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工证件照附件表';
##
INSERT INTO tb_sys_attached_tab (ID,TABLE_NAME,REMARK) VALUES ('2018111401','TB_SYS_STAFF_ICON_CARD','员工证件照') ##

CREATE TABLE IF NOT EXISTS `tb_sys_staff_icon_photo` (
  `ID` varchar(36) NOT NULL,
  `MAIN_ID` varchar(36) DEFAULT NULL,
  `FILE_NAME` varchar(1000) DEFAULT NULL,
  `SAVE_NAME` varchar(1000) DEFAULT NULL,
  `FILE_TYPE` varchar(1000) DEFAULT NULL,
  `UPLOAD_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `DIR` varchar(1000) DEFAULT NULL,
  `SYSTEM_NAME` varchar(1000) DEFAULT NULL,
  `IS_COVER` int(9) DEFAULT NULL,
  `ORDER_NUM` int(9) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工头像附件表';
##
INSERT INTO tb_sys_attached_tab (ID,TABLE_NAME,REMARK) VALUES ('2018111402','TB_SYS_STAFF_ICON_PHOTO','员工头像') ##

CREATE TABLE IF NOT EXISTS `tb_sys_staff_r_department` (
  `ID` varchar(36) NOT NULL,
  `STAFF_ID` varchar(36) DEFAULT NULL,
  `DEPARTMENT_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工跟部门关联表';##
