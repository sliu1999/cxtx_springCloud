

-- 依赖表 系统参数表 --
CREATE TABLE IF NOT EXISTS `tb_sys_parameter` (
  `ID` varchar(40) NOT NULL,
  `PARAMETER_ID` varchar(50) NOT NULL,
  `PARAMETER_VALUE` varchar(300) DEFAULT NULL,
  `REMARK` varchar(1000) DEFAULT NULL,
  `CAN_DELETE` varchar(1) DEFAULT NULL COMMENT '是否可以删除，有些以及写在程序里就可删除',
  PRIMARY KEY (`PARAMETER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统参数表';
##

-- 修改密码权限 --
INSERT INTO tb_sys_parameter (ID,PARAMETER_ID,PARAMETER_VALUE,REMARK,CAN_DELETE) VALUES
('20181120210230','LIMIT_CHANGE_PASSWORD','admin','可以修改其他用户密码的用户，多个以逗号隔开','0'); ##


##