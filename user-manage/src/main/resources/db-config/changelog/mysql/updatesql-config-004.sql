-- usermanage 0.0.5 基础数据插入--

DROP FUNCTION IF EXISTS getMenuChildList;
##

CREATE FUNCTION getMenuChildList(rootId VARCHAR(36))
RETURNS VARCHAR(4000)
BEGIN
    DECLARE sTemp VARCHAR(4000);
    DECLARE sTempChd VARCHAR(4000);

    SET sTemp = '$';
    SET sTempChd =CAST(rootId as CHAR);

    WHILE sTempChd is not null DO
    	SET sTemp = concat(sTemp,',',sTempChd);
        SELECT group_concat(ID ORDER BY SORT) INTO sTempChd FROM tb_sys_menu WHERE FIND_IN_SET(PARENT_ID,sTempChd)>0;
   	END WHILE;
    RETURN sTemp;
END;
##

DROP FUNCTION IF EXISTS getDepartChildList;
##

CREATE FUNCTION getDepartChildList(rootId VARCHAR(36))
RETURNS VARCHAR(4000)
BEGIN
    DECLARE sTemp VARCHAR(4000);
    DECLARE sTempChd VARCHAR(4000);

    SET sTemp = '$';
    SET sTempChd =CAST(rootId as CHAR);

    WHILE sTempChd is not null DO
    	SET sTemp = concat(sTemp,',',sTempChd);
        SELECT group_concat(ID ORDER BY SORT) INTO sTempChd FROM tb_sys_department WHERE FIND_IN_SET(PARENT_ID,sTempChd)>0;
   	END WHILE;
    RETURN sTemp;
END;
##

DROP FUNCTION IF EXISTS getStaffChildList;
##

CREATE FUNCTION getStaffChildList(rootId VARCHAR(36))
RETURNS VARCHAR(4000)
BEGIN
    DECLARE sTemp VARCHAR(4000);
    DECLARE sTempChd VARCHAR(4000);

    SET sTemp = '$';
    SET sTempChd =CAST(rootId as CHAR);

    WHILE sTempChd is not null DO
    	SET sTemp = concat(sTemp,',',sTempChd);
        SELECT group_concat(ID ORDER BY SORT) INTO sTempChd FROM tb_sys_staff WHERE FIND_IN_SET(LEADER,sTempChd)>0;
   	END WHILE;
    RETURN sTemp;
END;
##

-- 插入默认数据 --
-- INSERT INTO tb_sys_menu (ID,NAME,ACTION,SORT,PARENT_ID,STYLE,MENU_TYPE) VALUES
-- ('2018113001','个人中心','personalList',0,NULL,'fa fa-user',0),
-- ('2018113002','系统信息','sysInfo',1,NULL,'fa fa-cogs',0),
-- ('2018113003','菜单管理','menus',2,NULL,'fa fa-th-large',0),
-- ('2018113004','角色管理','roles',3,NULL,'fa fa-delicious',0),
-- ('2018113005','组织架构',NULL,5,NULL,'fa fa-sitemap',0),
-- ('2018113006','用户管理','users',4,NULL,'fa fa-users	',0),
-- ('2018113007','部门管理','departments',1,'2018113005',NULL,0),
-- ('2018113008','员工管理','staffs',2,'2018113005',NULL,0);
INSERT INTO `tb_sys_menu` VALUES ('1', null, '/system', 'Layout', '/system/role/role-list', '系统管理', 'system', 'documentation', '', null);
INSERT INTO `tb_sys_menu` VALUES ('3', '1', 'role', 'system/role/index', '/system/role/role-list', '角色管理', 'role', '', '', null);
INSERT INTO `tb_sys_menu` VALUES ('4', '1', 'user', 'system/user/list', '', '用户管理', 'user', '', '', null);
INSERT INTO `tb_sys_menu` VALUES ('5', '3', 'role-list', 'system/role/manage/list', '', '角色列表', 'role-list', '', 'true', null);
INSERT INTO `tb_sys_menu` VALUES ('6', '3', 'role-detail', 'system/role/manage/detail', '', '角色详情', 'role-detail', '', 'true', null);
INSERT INTO `tb_sys_menu` VALUES ('7', '1', 'menu', 'system/menu/index', '', '菜单管理', 'menu', '', '', null);
##

INSERT INTO tb_sys_role (ID,NAME,REMARK) VALUES ('2018113009','管理员','系统管理员');
##

INSERT INTO tb_sys_role_r_menu (ID,ROLE_ID,MENU_ID) VALUES
('2018113010','2018113009','1'),
('2018113011','2018113009','3'),
('2018113012','2018113009','4'),
('2018113013','2018113009','5'),
('2018113014','2018113009','6'),
('2018113015','2018113009','7');
##

INSERT INTO tb_sys_user (ID,LOGIN_ID,PASSWORD,CREATE_TIME,UPDATE_TIME,REMARK) VALUES
('2018113018','admin','e10adc3949ba59abbe56e057f20f883e',SYSDATE(),SYSDATE(),'超级管理员');
##

INSERT INTO tb_sys_user_r_role (ID,LOGIN_ID,ROLE_ID,IS_DEFAULT) VALUES
('2018113019','admin','2018113009',1);
##
