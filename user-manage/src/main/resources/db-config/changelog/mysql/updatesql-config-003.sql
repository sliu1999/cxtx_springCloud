

-- 表单，流程表 及数据 --

-- ----------------------------
-- Table structure for oa_flow
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `flow_name` varchar(500) DEFAULT NULL COMMENT '流程名称',
  `flow_type` varchar(255) DEFAULT NULL COMMENT '流程类型',
  `sort` bigint(20) DEFAULT NULL COMMENT '排序',
  `status` int(11) DEFAULT '1' COMMENT '数据状态 1可用，0不可用',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  `height` varchar(100) DEFAULT NULL COMMENT '高度',
  `width` varchar(100) DEFAULT NULL COMMENT '宽度',
  PRIMARY KEY (`id`),
  KEY `pk_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程主表';
##
-- ----------------------------
-- Records of oa_flow
-- ----------------------------

-- ----------------------------
-- Table structure for oa_flow_form
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_flow_form` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `form_model_id` bigint(20) DEFAULT NULL COMMENT '表单模型ID',
  `flow_id` bigint(20) DEFAULT NULL COMMENT '流程ID',
  `flow_model_id` bigint(20) DEFAULT NULL COMMENT '流程模型ID',
  `status` int(11) DEFAULT NULL COMMENT '0不可用，1可用',
  `create_by` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `flowModId_index` (`flow_model_id`) USING BTREE,
  KEY `formModId_index` (`form_model_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程模型和表单模型关联表';
##
-- ----------------------------
-- Records of oa_flow_form
-- ----------------------------

-- ----------------------------
-- Table structure for oa_flow_model
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_flow_model` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `flow_id` bigint(20) DEFAULT NULL COMMENT '流程ID',
  `flow_name` varchar(500) DEFAULT NULL COMMENT '流程名称',
  `sort` bigint(20) DEFAULT NULL COMMENT '排序',
  `status` int(11) DEFAULT '1' COMMENT '数据状态 1可用，0不可用',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `pk_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程模型表';
##
-- ----------------------------
-- Records of oa_flow_model
-- ----------------------------

-- ----------------------------
-- Table structure for oa_flow_model_detail
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_flow_model_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `model_id` bigint(20) DEFAULT NULL COMMENT '流程模型ID',
  `last_node_code` varchar(500) DEFAULT NULL COMMENT '上一节点code',
  `node_code` varchar(500) DEFAULT NULL COMMENT '当前节点code',
  `next_node_code` varchar(500) DEFAULT NULL COMMENT '下一节点code',
  `name` varchar(500) DEFAULT NULL COMMENT '名称',
  `sort` bigint(20) DEFAULT NULL COMMENT '排序',
  `status` int(11) DEFAULT '1' COMMENT '数据状态 1可用，0不可用',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `pk_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程模型节点线性关系明细表';
##
-- ----------------------------
-- Records of oa_flow_model_detail
-- ----------------------------

-- ----------------------------
-- Table structure for oa_flow_model_element
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_flow_model_element` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `model_id` bigint(20) DEFAULT NULL COMMENT '流程模型ID，oa_flow_mod中的ID',
  `code` varchar(500) DEFAULT NULL COMMENT '元素编码',
  `name` varchar(500) DEFAULT NULL COMMENT '名称',
  `sort` bigint(20) DEFAULT NULL COMMENT '排序',
  `status` int(11) DEFAULT '1' COMMENT '数据状态 1可用，0不可用',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  `start_x` varchar(20) DEFAULT NULL,
  `start_y` varchar(20) DEFAULT NULL,
  `height` varchar(20) DEFAULT NULL,
  `width` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pk_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程模型元素表';
##
-- ----------------------------
-- Records of oa_flow_model_element
-- ----------------------------

-- ----------------------------
-- Table structure for oa_flow_model_element_config
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_flow_model_element_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `element_id` bigint(20) DEFAULT NULL COMMENT '节点ID',
  `element_config` text COMMENT '节点配置信息',
  `create_by` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程节点配置信息表';
##
-- ----------------------------
-- Records of oa_flow_model_element_config
-- ----------------------------

-- ----------------------------
-- Table structure for oa_form_add
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_form_add` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `main_id` bigint(20) DEFAULT NULL,
  `form_id` varchar(255) DEFAULT NULL COMMENT '菜单id',
  `dept_id` varchar(255) DEFAULT NULL COMMENT '部门id',
  `role_id` varchar(255) DEFAULT NULL COMMENT '角色id',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `type` varchar(255) DEFAULT NULL COMMENT '类型',
  `sort` bigint(20) DEFAULT NULL COMMENT '排序',
  `status` int(11) DEFAULT '1' COMMENT '数据状态 1可用，0不可用',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `remarks` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `formId_index` (`form_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发起权限子表';
##
-- ----------------------------
-- Records of oa_form_add
-- ----------------------------

-- ----------------------------
-- Table structure for oa_form_file
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_form_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `file_name` varchar(500) DEFAULT NULL COMMENT '附件名称',
  `true_name` varchar(255) DEFAULT NULL COMMENT '附件真实名称',
  `file_desc` varchar(255) DEFAULT NULL COMMENT '附件描述',
  `file_path` varchar(255) DEFAULT NULL COMMENT '附件路径',
  `sort` bigint(20) DEFAULT NULL COMMENT '排序',
  `status` int(11) DEFAULT '1' COMMENT '数据状态 1可用，0不可用',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_name` varchar(255) DEFAULT NULL,
  `size` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图片存储信息表';
##
-- ----------------------------
-- Records of oa_form_file
-- ----------------------------

-- ----------------------------
-- Table structure for oa_form_main
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_form_main` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `types_forms` varchar(1000) DEFAULT NULL COMMENT '可见类型/模块组别(type_id,form_id,type,flag:(0,0选择模块1,0选择全部模块    0,1排除所选模块    1,1排除所有模块)',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `auth_group` varchar(5000) DEFAULT NULL COMMENT '权限组别(userIds,groupIds,roleId,groupRoleIds)',
  `auth_all` varchar(10) DEFAULT NULL COMMENT '是否全部表单可用，0否1可',
  `sort` bigint(20) DEFAULT NULL COMMENT '排序',
  `status` int(11) DEFAULT '1' COMMENT '数据状态 1可用，0不可用',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `remarks` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发起权限表';
##
-- ----------------------------
-- Records of oa_form_main
-- ----------------------------

-- ----------------------------
-- Table structure for oa_form_model
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_form_model` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `form_type` varchar(500) DEFAULT NULL,
  `name` varchar(500) DEFAULT NULL COMMENT '表单名',
  `table_schema` text COMMENT '表结构',
  `table_key` varchar(2000) DEFAULT NULL COMMENT '表名',
  `form_view` text COMMENT '表视图',
  `detail_keys` varchar(2000) DEFAULT NULL COMMENT '关联明细表名',
  `type` bigint(20) DEFAULT NULL,
  `sort` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT '1' COMMENT '数据状态 1可用，0不可用',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表单模型表';
##
-- ----------------------------
-- Records of oa_form_model
-- ----------------------------

-- ----------------------------
-- Table structure for oa_form_process_instance
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_form_process_instance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_form_id` bigint(20) DEFAULT NULL COMMENT '表单appId',
  `process_name` text COMMENT '流程实例名称',
  `process_id` bigint(20) DEFAULT NULL COMMENT '流程实例Id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '发起人Id',
  `form_model_id` bigint(20) DEFAULT NULL COMMENT '表单模型ID',
  `create_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sys_form` (`process_id`,`app_form_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表单实例、流程实例记录表';
##
-- ----------------------------
-- Records of oa_form_process_instance
-- ----------------------------

-- ----------------------------
-- Table structure for oa_process
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_process` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `assignee` text COMMENT '审批人ID',
  `flow_id` bigint(20) DEFAULT NULL COMMENT '流程定义ID',
  `process_name` varchar(255) DEFAULT NULL COMMENT '流程名称',
  `code_name` varchar(255) DEFAULT NULL COMMENT '节点名称',
  `code` varchar(255) DEFAULT NULL COMMENT '节点',
  `state` int(11) DEFAULT NULL COMMENT '流程状态',
  `app_form_id` bigint(20) DEFAULT NULL COMMENT '表单实例id（app）',
  `flow_model_id` bigint(20) DEFAULT NULL COMMENT 'oa_flow_mod中的流程模型ID',
  `create_by` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `createBy_index` (`create_by`) USING BTREE,
  KEY `modId_index` (`flow_model_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程实例表';
##
-- ----------------------------
-- Records of oa_process
-- ----------------------------

-- ----------------------------
-- Table structure for oa_process_his
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_process_his` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `process_id` bigint(20) DEFAULT NULL COMMENT '流程实例ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '审批人ID',
  `create_by` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `PROCESSID_INDEX` (`process_id`),
  KEY `USERID_INDEX` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='历史流程实例表';
##
-- ----------------------------
-- Records of oa_process_his
-- ----------------------------

-- ----------------------------
-- Table structure for oa_process_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_process_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `flow_id` bigint(20) DEFAULT NULL COMMENT '流程定义ID',
  `process_id` bigint(20) DEFAULT NULL COMMENT '流程实例ID',
  `cur_node` varchar(255) DEFAULT NULL COMMENT '当前节点',
  `cur_assignee` text COMMENT '当前审批人',
  `next_node` varchar(255) DEFAULT NULL COMMENT '下一审批节点',
  `next_assignees` longtext COMMENT '下一审批人',
  `message` text COMMENT '审批人携带的审批意见',
  `action_id` int(11) DEFAULT NULL COMMENT '节点状态,1表示发起,2表示通过,3拒绝，4转交，5结束',
  `create_by` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `state_index` (`action_id`),
  KEY `PROCESSID_INDEX` (`process_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程操作日志表';
##
-- ----------------------------
-- Records of oa_process_log
-- ----------------------------

-- ----------------------------
-- Table structure for oa_process_run
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_process_run` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_by` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `process_id` bigint(20) DEFAULT NULL COMMENT '流程实例ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '审批人ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='正在运行的流程实例表';
##
-- ----------------------------
-- Records of oa_process_run
-- ----------------------------

-- ----------------------------
-- Table structure for tb_dic_form_type
-- ----------------------------
DROP TABLE IF EXISTS `tb_dic_form_type`;
CREATE TABLE `tb_dic_form_type` (
  `ID` int(9) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `SORT` int(9) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表单类型表';
##
-- ----------------------------
-- Records of tb_dic_form_type
-- ----------------------------
INSERT INTO `tb_dic_form_type` VALUES ('1', '行政', '1');##
INSERT INTO `tb_dic_form_type` VALUES ('2', '财务', '2');##

-- ----------------------------
-- Table structure for tb_sys_dictionary
-- ----------------------------

