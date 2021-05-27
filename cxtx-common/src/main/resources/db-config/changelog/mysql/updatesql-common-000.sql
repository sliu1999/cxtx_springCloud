-- 基础表创建 --
CREATE TABLE IF NOT EXISTS `tb_sys_attached_tab` (
  `ID` varchar(10) NOT NULL,
  `TABLE_NAME` varchar(50) DEFAULT NULL COMMENT '表名',
  `REMARK` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='附件 信息表';##

CREATE TABLE IF NOT EXISTS `tb_sys_parameter` (
  `ID` varchar(40) NOT NULL,
  `PARAMETER_ID` varchar(50) NOT NULL,
  `PARAMETER_VALUE` varchar(300) DEFAULT NULL,
  `REMARK` varchar(1000) DEFAULT NULL,
  `CAN_DELETE` varchar(1) DEFAULT NULL COMMENT '是否可以删除，有些以及写在程序里就可删除',
  PRIMARY KEY (`PARAMETER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统参数表';##

CREATE TABLE IF NOT EXISTS  `tb_sys_task` (
  `ID` varchar(40) NOT NULL,
  `NAME` varchar(1000) DEFAULT NULL,
  `MOTHED` varchar(1000) DEFAULT NULL COMMENT '类名 ，首字母小写',
  `CRON` varchar(1000) DEFAULT NULL,
  `ENABLE` varchar(10) DEFAULT NULL COMMENT '是否有效',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='定时任务表';##
