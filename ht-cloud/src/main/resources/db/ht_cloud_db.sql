/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50642
Source Host           : localhost:3306
Source Database       : ht_cloud_db

Target Server Type    : MYSQL
Target Server Version : 50642
File Encoding         : 65001

Date: 2022-06-10 15:00:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `cluster_table`
-- ----------------------------
DROP TABLE IF EXISTS `cluster_table`;
CREATE TABLE `cluster_table` (
  `cluster_id` bigint(20) NOT NULL COMMENT '集群UUID，集群唯一标识',
  `cluster_name` varchar(36) NOT NULL COMMENT '集群名称',
  `data_center_id` bigint(20) NOT NULL COMMENT '所属数据中心',
  `drs_switch` int(11) DEFAULT NULL COMMENT '是否开启分布式资源调度。1为开启，2为关闭',
  `ha_switch` int(11) DEFAULT NULL COMMENT '是否开启高可用。1为开启，2为关闭',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  PRIMARY KEY (`cluster_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cluster_table
-- ----------------------------

-- ----------------------------
-- Table structure for `data_center_table`
-- ----------------------------
DROP TABLE IF EXISTS `data_center_table`;
CREATE TABLE `data_center_table` (
  `data_center_id` bigint(20) NOT NULL COMMENT '数据中心UUID，数据中心唯一标识',
  `data_center_name` varchar(36) DEFAULT NULL COMMENT '数据中心名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  PRIMARY KEY (`data_center_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of data_center_table
-- ----------------------------

-- ----------------------------
-- Table structure for `flow_control_table`
-- ----------------------------
DROP TABLE IF EXISTS `flow_control_table`;
CREATE TABLE `flow_control_table` (
  `flow_control_id` bigint(20) NOT NULL COMMENT '流量控制的唯一标识',
  `average_bw` varchar(36) DEFAULT NULL COMMENT '平均带宽',
  `peak_bw` varchar(36) DEFAULT NULL COMMENT '峰值带宽',
  `proruption_bw` varchar(36) DEFAULT NULL COMMENT '突发大小',
  `port_id` bigint(20) DEFAULT NULL COMMENT '端口id（作用于端口有值，否则为空）',
  `vm_switch_id` bigint(20) DEFAULT NULL COMMENT '虚拟交换机的UUID（作用于交换机有值，否则为空）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  PRIMARY KEY (`flow_control_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of flow_control_table
-- ----------------------------

-- ----------------------------
-- Table structure for `host_table`
-- ----------------------------
DROP TABLE IF EXISTS `host_table`;
CREATE TABLE `host_table` (
  `host_id` bigint(20) NOT NULL,
  `host_name` varchar(36) DEFAULT NULL COMMENT '主机名',
  `host_user` varchar(36) DEFAULT NULL COMMENT '主机用户名（拥有root权限）',
  `host_password` varchar(36) DEFAULT NULL COMMENT '主机密码（需加密保护）',
  `os_ip` varchar(15) DEFAULT NULL COMMENT '操作系统管理IP',
  `bmc_ip` varchar(15) DEFAULT NULL COMMENT '服务器BMC地址',
  `cpu_type` varchar(36) DEFAULT NULL COMMENT 'cpu型号',
  `virtual_version` varchar(15) DEFAULT NULL COMMENT '虚拟化平台版本',
  `cluster_id` bigint(20) DEFAULT NULL COMMENT '服务器所属集群，如果服务器不在集群中，此字段为NULL',
  `data_center_id` bigint(20) DEFAULT NULL COMMENT '服务器所属数据中心UUID',
  `state` varchar(36) DEFAULT NULL COMMENT '服务器当前状态，有以下几种“运行”“关机” “维护模式”“异常”“失联”',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  PRIMARY KEY (`host_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of host_table
-- ----------------------------

-- ----------------------------
-- Table structure for `network_table`
-- ----------------------------
DROP TABLE IF EXISTS `network_table`;
CREATE TABLE `network_table` (
  `network_id` bigint(20) NOT NULL COMMENT '网卡唯一标识',
  `vm_network_name` varchar(36) DEFAULT NULL COMMENT '网卡名称',
  `vm_rate` varchar(36) DEFAULT NULL COMMENT '速率双工',
  `host_id` bigint(20) DEFAULT NULL COMMENT '物理网卡所在主机唯一标识',
  `vm_switch_id` bigint(20) DEFAULT NULL COMMENT '虚拟交换机唯一标识',
  `vm_id` bigint(20) DEFAULT NULL COMMENT '虚拟机唯一标识',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`network_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of network_table
-- ----------------------------

-- ----------------------------
-- Table structure for `port_table`
-- ----------------------------
DROP TABLE IF EXISTS `port_table`;
CREATE TABLE `port_table` (
  `port_id` bigint(20) NOT NULL COMMENT '上行端口的唯一标识',
  `port_type` varchar(36) DEFAULT NULL COMMENT '端口类型（普通端口、上行端口、管理端口）',
  `vlan` int(11) DEFAULT NULL COMMENT 'vlan',
  `port_name` varchar(36) DEFAULT NULL COMMENT '端口名称',
  `type` varchar(36) DEFAULT NULL COMMENT '网桥类型',
  `model` varchar(200) DEFAULT NULL COMMENT '网卡驱动',
  `mac` varchar(36) DEFAULT NULL COMMENT 'mac地址',
  `aggregate_model` varchar(36) DEFAULT NULL COMMENT '如为多网卡选择端口聚合模式（主备模式、负载均衡模式）',
  `vm_switch_id` bigint(20) DEFAULT NULL COMMENT '虚拟交换机的UUID',
  `vm_id` bigint(20) DEFAULT NULL COMMENT '虚拟机唯一标识',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  PRIMARY KEY (`port_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of port_table
-- ----------------------------

-- ----------------------------
-- Table structure for `security_group_table`
-- ----------------------------
DROP TABLE IF EXISTS `security_group_table`;
CREATE TABLE `security_group_table` (
  `security_group_id` bigint(20) NOT NULL COMMENT '安全组唯一标识',
  `security_group_name` varchar(36) DEFAULT NULL COMMENT '安全组名称',
  `security_group_remark` varchar(200) DEFAULT NULL COMMENT '安全组描述',
  `create_time` datetime DEFAULT NULL COMMENT '安全组创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  PRIMARY KEY (`security_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of security_group_table
-- ----------------------------

-- ----------------------------
-- Table structure for `security_group_vm_table`
-- ----------------------------
DROP TABLE IF EXISTS `security_group_vm_table`;
CREATE TABLE `security_group_vm_table` (
  `id` bigint(20) NOT NULL COMMENT '安全组与虚拟机关联唯一标识',
  `vm_id` bigint(20) DEFAULT NULL COMMENT '虚拟机唯一标识',
  `security_group_id` bigint(20) DEFAULT NULL COMMENT '安全组唯一标识',
  `port_id` bigint(20) DEFAULT NULL COMMENT '端口唯一标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of security_group_vm_table
-- ----------------------------

-- ----------------------------
-- Table structure for `security_rule_table`
-- ----------------------------
DROP TABLE IF EXISTS `security_rule_table`;
CREATE TABLE `security_rule_table` (
  `rule_id` bigint(20) NOT NULL COMMENT '安全组规则唯一标识',
  `source_ip` varchar(20) DEFAULT NULL COMMENT '源ip',
  `dest_ip` varchar(20) DEFAULT NULL COMMENT '目的ip',
  `source_mask` varchar(36) DEFAULT NULL COMMENT '源子网掩码',
  `dest_mask` varchar(36) DEFAULT NULL COMMENT '目的子网掩码',
  `source_port` int(11) DEFAULT NULL COMMENT '源端口',
  `dest_port` int(11) DEFAULT NULL COMMENT '目的端口',
  `agree_type` varchar(20) DEFAULT NULL COMMENT '协议类型',
  `in_out_flow` varchar(20) DEFAULT NULL COMMENT '流量进出方向',
  `action` varchar(20) DEFAULT NULL COMMENT '流量控制(accept为接受，drop为丢弃)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `security_group_id` bigint(20) DEFAULT NULL COMMENT '安全组唯一标识',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  PRIMARY KEY (`rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of security_rule_table
-- ----------------------------

-- ----------------------------
-- Table structure for `storage_pool_table`
-- ----------------------------
DROP TABLE IF EXISTS `storage_pool_table`;
CREATE TABLE `storage_pool_table` (
  `storage_pool_id` bigint(20) NOT NULL COMMENT '存储资源池唯一标识',
  `pool_uuid` varchar(50) DEFAULT NULL COMMENT '存储卷uuid',
  `pool_show_name` varchar(36) DEFAULT NULL COMMENT '显示名称',
  `storage_pool_name` varchar(36) DEFAULT NULL COMMENT '存储资源池名称',
  `storage_pool_path` varchar(200) DEFAULT NULL COMMENT '存储池路径',
  `storage_ip` varchar(50) DEFAULT NULL COMMENT '存储ip',
  `pool_type` varchar(36) DEFAULT NULL COMMENT '存储池类型(dir本地,fc,iscsi,lvm)',
  `capacity` varchar(36) DEFAULT NULL COMMENT '存储的容量',
  `used_space` varchar(36) DEFAULT NULL COMMENT '已用容量',
  `free_space` varchar(36) DEFAULT NULL COMMENT '剩余容量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `cluster_id` bigint(20) DEFAULT NULL COMMENT '存储资源池所属集群UUID',
  `host_id` bigint(20) DEFAULT NULL COMMENT '主机id',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `status` int(11) DEFAULT NULL COMMENT '状态(1为活跃，2为不活跃，3为需要格式化,4为暂停)',
  `vm_template_type` varchar (20) DEFAULT NULL COMMENT '虚拟机模板类型',
  `judge` varchar (20) DEFAULT NULL COMMENT '区分存储卷是否为模板（0-普通存储卷 1-模板存储卷）',
  PRIMARY KEY (`storage_pool_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of storage_pool_table
-- ----------------------------

-- ----------------------------
-- Table structure for `storage_table`
-- ----------------------------
DROP TABLE IF EXISTS `storage_table`;
CREATE TABLE `storage_table` (
  `storage_id` bigint(20) NOT NULL COMMENT '存储唯一标识',
  `volume_uuid` varchar(50) DEFAULT NULL COMMENT '存储卷uuid',
  `storage_volume_name` varchar(36) DEFAULT NULL COMMENT '存储卷名称',
  `filesystem` varchar(10) DEFAULT NULL COMMENT '文件系统类型（虚拟机镜像、iso文件镜像）',
  `file_size` varchar(36) DEFAULT NULL COMMENT '镜像文件大小',
  `capacity` varchar(36) DEFAULT NULL COMMENT '存储的容量',
  `storage_type` int(11) DEFAULT NULL COMMENT '存储方式（本地存储、NAS存储、ISCSI存储、FC存储）',
  `storage_path` varchar(255) DEFAULT NULL COMMENT '存储路径',
  `storage_pool_id` bigint(20) DEFAULT NULL COMMENT '存储所属资源池',
  `basic_volume_id` bigint(20) DEFAULT NULL COMMENT '基础镜像id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `judge` varchar(20) DEFAULT NULL COMMENT '区分存储卷是否为模板（0-普通存储卷 1-模板存储卷）',
  `status` int(11) DEFAULT NULL COMMENT '1为正常，2为放入回收站',
  `identifier` varchar(255) DEFAULT NULL COMMENT '文件标识--上传文件时才有',
  PRIMARY KEY (`storage_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of storage_table
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_captcha`
-- ----------------------------
DROP TABLE IF EXISTS `sys_captcha`;
CREATE TABLE `sys_captcha` (
  `uuid` char(36) NOT NULL COMMENT 'uuid',
  `code` varchar(6) NOT NULL COMMENT '验证码',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统验证码';

-- ----------------------------
-- Records of sys_captcha
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `oper_obj` varchar(200) DEFAULT NULL COMMENT '操作对象',
  `oper_mark` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作描述',
  `error_msg` varchar(5000) DEFAULT NULL COMMENT '失败原因',
  `result` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '执行结果',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='系统日志';

-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COMMENT='菜单管理';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '0', '系统管理', null, null, '0', 'system', '0');
INSERT INTO `sys_menu` VALUES ('2', '1', '管理员列表', 'sys/user', null, '1', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('3', '1', '角色管理', 'sys/role', null, '1', 'role', '2');
INSERT INTO `sys_menu` VALUES ('4', '1', '菜单管理', 'sys/menu', null, '1', 'menu', '3');
INSERT INTO `sys_menu` VALUES ('5', '0', '主机管理', null, null, '0', 'system', '1');
INSERT INTO `sys_menu` VALUES ('6', '5', '数据中心管理', '/datacenter', null, '1', null, '1');
INSERT INTO `sys_menu` VALUES ('7', '5', '集群管理', '/cluster', null, '1', null, '2');
INSERT INTO `sys_menu` VALUES ('8', '0', '虚拟机管理', null, null, '0', null, '2');
INSERT INTO `sys_menu` VALUES ('9', '0', '网络管理', null, null, '1', null, '3');
INSERT INTO `sys_menu` VALUES ('10', '0', '存储管理', null, null, '0', null, '4');
INSERT INTO `sys_menu` VALUES ('11', '5', '主机管理', '/host', null, '0', null, '3');
INSERT INTO `sys_menu` VALUES ('12', '9', '网络管理', '/network', null, '1', null, '0');
INSERT INTO `sys_menu` VALUES ('13', '9', '交换机管理', '/vmSwitch', null, '1', null, '1');
INSERT INTO `sys_menu` VALUES ('14', '9', '安全组管理', '/securityGroup', null, '1', null, '2');
INSERT INTO `sys_menu` VALUES ('15', '9', '安全规则管理', '/securityRule', null, '1', null, '3');
INSERT INTO `sys_menu` VALUES ('16', '0', '任务管理', null, null, '0', null, '4');
INSERT INTO `sys_menu` VALUES ('17', '16', '任务管理', '/task', null, '0', null, '4');
INSERT INTO `sys_menu` VALUES ('18', '8', '虚拟机管理', '/vm', null, '0', null, '2');
INSERT INTO `sys_menu` VALUES ('19', '8', '虚拟机配置管理', '/vmHardware', null, '0', null, '2');
INSERT INTO `sys_menu` VALUES ('20', '8', '虚拟机备份恢复管理', '/vmBackup', null, '0', null, '2');
INSERT INTO `sys_menu` VALUES ('21', '8', '虚拟机快照管理', '/vmSnapShot', null, '0', null, '2');
INSERT INTO `sys_menu` VALUES ('22', '8', '虚拟机模板管理', '/vmTemplate', null, '0', null, '2');
INSERT INTO `sys_menu` VALUES ('23', '10', '存储池管理', '/storagePool', null, '0', null, '4');
INSERT INTO `sys_menu` VALUES ('24', '10', '存储管理', '/storage', null, '0', null, '4');
INSERT INTO `sys_menu` VALUES ('30', '6', '查看', null, 'host:datacenter:list,host:datacenter:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('31', '6', '新增', null, 'host:datacenter:save', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('32', '6', '修改', null, 'host:datacenter:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('33', '6', '删除', null, 'host:datacenter:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('34', '7', '查看', null, 'vm:cluster:list,vm:cluster:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('35', '7', '新增', null, 'vm:cluster:save', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('36', '7', '修改', null, 'vm:cluster:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('37', '7', '删除', null, 'vm:cluster:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('38', '11', '查看', null, 'host:machine:list,host:machine:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('39', '11', '新增', null, 'host:machine:save', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('40', '11', '修改', null, 'host:machine:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('41', '11', '删除', null, 'host:machine:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('105', '11', '开机', null, 'host:machine:startUp', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('106', '11', '关机', null, 'host:machine:turnOff', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('107', '11', '重启', null, 'host:machine:reboot', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('108', '11', '进入维护模式', null, 'host:machine:inMaintenance', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('109', '11', '退出维护模式', null, 'host:machine:outMaintenance', '2', null, '0');

INSERT INTO `sys_menu` VALUES ('42', '2', '查看', null, 'sys:user:list,sys:user:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('43', '2', '新增', null, 'sys:user:save,sys:role:select', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('44', '2', '修改', null, 'sys:user:update,sys:role:select', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('45', '2', '删除', null, 'sys:user:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('46', '3', '查看', null, 'sys:role:list,sys:role:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('47', '3', '新增', null, 'sys:role:save,sys:menu:list', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('48', '3', '修改', null, 'sys:role:update,sys:menu:list', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('49', '3', '删除', null, 'sys:role:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('50', '4', '查看', null, 'sys:menu:list,sys:menu:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('51', '4', '新增', null, 'sys:menu:save,sys:menu:select', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('52', '4', '修改', null, 'sys:menu:update,sys:menu:select', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('53', '4', '删除', null, 'sys:menu:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('54', '1', '系统日志', 'sys/log', 'sys:log:list', '1', 'log', '7');
INSERT INTO `sys_menu` VALUES ('55', '12', '查看', null, 'net:machine:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('56', '13', '查看', null, 'vm:switch:list,vm:switch:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('57', '13', '新增', null, 'vm:switch:save', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('58', '13', '修改', null, 'vm:switch:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('59', '13', '删除', null, 'vm:switch:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('60', '14', '查看', null, 'network:securityGroup:list,network:securityGroup:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('61', '14', '新增', null, 'network:securityGroup:save', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('62', '14', '修改', null, 'network:securityGroup:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('63', '14', '删除', null, 'network:securityGroup:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('64', '15', '查看', null, 'network:securityRule:list,network:securityRule:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('65', '15', '新增', null, 'network:securityRule:save', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('66', '15', '修改', null, 'network:securityRule:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('67', '15', '删除', null, 'network:securityRule:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('68', '18', '查看', null, 'vm:vm:list,vm:vm:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('69', '18', '新增', null, 'vm:vm:add', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('70', '18', '修改', null, 'vm:vm:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('71', '18', '删除', null, 'vm:vm:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('72', '19', '查看', null, 'vm:hardware:list', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('73', '19', '新增', null, 'vm:hardware:add', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('74', '19', '修改', null, 'vm:hardware:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('75', '19', '删除', null, 'vm:hardware:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('76', '20', '查看', null, 'vm:backup:list', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('77', '20', '新增', null, 'vm:backup:add', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('78', '20', '修改', null, 'vm:backup:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('79', '20', '删除', null, 'vm:backup:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('80', '21', '查看', null, 'vm:snapshot:list', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('81', '21', '新增', null, 'vm:snapshot:add', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('82', '21', '修改', null, 'vm:snapshot:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('83', '21', '删除', null, 'vm:snapshot:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('84', '22', '查看', null, 'vm:template:list', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('85', '22', '新增', null, 'vm:template:add', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('86', '22', '修改', null, 'vm:template:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('87', '22', '删除', null, 'vm:template:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('88', '23', '查看', null, 'vm:storagePool:list,vm:storagePool:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('89', '23', '新增', null, 'vm:storagePool:save', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('90', '23', '修改', null, 'vm:storagePool:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('91', '23', '删除', null, 'vm:storagePool:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('93', '17', '查看', null, 'vm:task:list,vm:task:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('94', '17', '导出', null, 'vm:task:export', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('95', '23', '查看', null, 'vm:storage:list,vm:storage:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('96', '23', '新增', null, 'vm:storage:save', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('97', '23', '上传', null, 'vm:storage:upload', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('98', '23', '删除', null, 'vm:storage:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('99', '11', '开机', null, 'host:machine:startUp', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('100', '11', '关机', null, 'host:machine:turnOff', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('101', '11', '重启', null, 'host:machine:reboot', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('102', '11', '进入维护模式', null, 'host:machine:inMaintenance', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('103', '11', '退出维护模式', null, 'host:machine:outMaintenance', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('104', '18', '迁移', null, 'vm:vm:move', '2', null, '0');

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色与菜单对应关系';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) DEFAULT NULL COMMENT '盐',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='系统用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '9ec9750e709431dad22365cabc5c625482e574c74adaebba7dd02f1129e4ce1d', 'YzcmCZNvbXocrsz9dm8e', 'root@renren.io', '13612345678', '1', '1', '2016-11-11 11:11:11');

-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户与角色对应关系';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_user_token`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token` (
  `user_id` bigint(20) NOT NULL,
  `token` varchar(100) NOT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户Token';

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------
INSERT INTO `sys_user_token` VALUES ('1', '03f68da4bed8ba6bcbdcad88f725feab', '2022-06-10 23:40:33', '2022-06-10 11:40:33');

-- ----------------------------
-- Table structure for `task_table`
-- ----------------------------
DROP TABLE IF EXISTS `task_table`;
CREATE TABLE `task_table` (
  `task_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '任务唯一标识',
  `task_name` varchar(50) DEFAULT NULL COMMENT '任务名称',
  `task_type` int(11) DEFAULT NULL COMMENT '任务类型 1为系统任务，2为定时任务',
  `create_time` datetime DEFAULT NULL COMMENT '任务创建时间',
  `end_time` datetime DEFAULT NULL COMMENT '任务结束时间',
  `take_up_time` bigint(20) DEFAULT NULL COMMENT '任务耗时 毫秒',
  `run_monitor` varchar(255) DEFAULT NULL COMMENT '任务运行状况',
  `task_result` int(11) DEFAULT NULL COMMENT '任务执行结果 1为成功 2为失败',
  `fail_msg` varchar(500) DEFAULT NULL COMMENT '失败原因',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '任务创建者',
  `host_id` bigint(20) DEFAULT NULL COMMENT '所属主机',
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_table
-- ----------------------------

-- ----------------------------
-- Table structure for `vm_backup_table`
-- ----------------------------
DROP TABLE IF EXISTS `vm_backup_table`;
CREATE TABLE `vm_backup_table` (
  `vm_backup_id` bigint(20) NOT NULL COMMENT '虚拟机备份id',
  `vm_backup_type` varchar(36) DEFAULT NULL COMMENT '虚拟机备份类型（全量、增量、差异、定时）',
  `vm_backup_path` varchar(36) DEFAULT NULL COMMENT '虚拟机备份路径',
  `last_time` datetime DEFAULT NULL COMMENT '虚拟机最后一次备份时间',
  `vm_id` bigint(20) DEFAULT NULL COMMENT '备份所属虚拟机',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  PRIMARY KEY (`vm_backup_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of vm_backup_table
-- ----------------------------

-- ----------------------------
-- Table structure for `vm_hardware_table`
-- ----------------------------
DROP TABLE IF EXISTS `vm_hardware_table`;
CREATE TABLE `vm_hardware_table` (
  `vm_hardware_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '虚拟机配置唯一标识',
  `vm_id` bigint(20) DEFAULT NULL COMMENT '所属虚拟机',
  `vm_os` varchar(36) DEFAULT NULL COMMENT '虚拟机操作系统',
  `vm_os_path` varchar(200) DEFAULT NULL COMMENT '虚拟机操作系统镜像文件路径',
  `vm_storage_location` varchar(200) DEFAULT NULL COMMENT '虚拟机存储位置（本地、分布式存储、磁盘阵列）',
  `vm_cpu_num` int(11) DEFAULT NULL COMMENT '虚拟机cpu个数',
  `vm_cpu_aduit` int(11) DEFAULT NULL COMMENT '虚拟机cpu核数',
  `vm_mem_size` varchar(15) DEFAULT NULL COMMENT '虚拟机内存大小',
  `vm_disk_size` bigint(20) DEFAULT NULL COMMENT '虚拟机磁盘大小',
  `vm_disk_type` varchar(15) DEFAULT NULL COMMENT '虚拟机硬盘控制类型（IDE，SCSI，NVME，virtio）',
  `vm_cd_driver` varchar(15) DEFAULT NULL COMMENT '虚拟机光驱',
  `vm_network_mac` varchar(500) DEFAULT NULL COMMENT '虚拟机网卡信息及mac地址[{“network”:””,”mac”:””}]',
  `disk_create_type` int(11) DEFAULT NULL COMMENT '磁盘创建类型 1为新建文件、2为已有文件、3为块设备',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `vm_switch_id` bigint(20) DEFAULT NULL COMMENT '所属虚拟交换机',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  PRIMARY KEY (`vm_hardware_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of vm_hardware_table
-- ----------------------------

-- ----------------------------
-- Table structure for `vm_snapshot_table`
-- ----------------------------
DROP TABLE IF EXISTS `vm_snapshot_table`;
CREATE TABLE `vm_snapshot_table` (
  `vm_snapshot_id` bigint(20) NOT NULL COMMENT '虚拟快照唯一标识',
  `vm_snapshot_name` varchar(36) DEFAULT NULL COMMENT '虚拟机快照名称',
  `vm_snapshot_path` varchar(200) DEFAULT NULL COMMENT '虚拟机快照路径',
  `create_time` datetime DEFAULT NULL COMMENT '虚拟机快照创建时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '虚拟机快照描述',
  `vm_id` bigint(20) DEFAULT NULL COMMENT '快照所属虚拟机',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  PRIMARY KEY (`vm_snapshot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of vm_snapshot_table
-- ----------------------------

-- ----------------------------
-- Table structure for `vm_switch_table`
-- ----------------------------
DROP TABLE IF EXISTS `vm_switch_table`;
CREATE TABLE `vm_switch_table` (
  `vm_switch_id` bigint(20) NOT NULL COMMENT '虚拟交换机UUID，虚拟交换机的唯一标识',
  `vm_switch_name` varchar(36) DEFAULT NULL COMMENT '虚拟交换机名称',
  `network_type` varchar(36) DEFAULT NULL COMMENT '网络类型可多选,多个以|分割(1为管理网络，2为业务网络，3为存储网络)',
  `net_machine` varchar(36) DEFAULT NULL COMMENT '物理网卡(多个以|分割)',
  `ip` varchar(36) DEFAULT NULL COMMENT 'ip地址',
  `gateway` varchar(36) DEFAULT NULL COMMENT '网关地址',
  `net_mask` varchar(36) DEFAULT NULL COMMENT '子网掩码',
  `mtu_size` int(11) DEFAULT NULL COMMENT 'MTU大小（默认1500）',
  `vlan` int(11) DEFAULT NULL COMMENT '范围2-4094',
  `link_mode` varchar(36) DEFAULT NULL COMMENT '链路聚合模式(静态、动态)',
  `load_mode` varchar(36) DEFAULT NULL COMMENT '负载分担模式(均衡、主备)',
  `host_id` bigint(20) DEFAULT NULL COMMENT '虚拟交换机所在服务器的UUID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  PRIMARY KEY (`vm_switch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of vm_switch_table
-- ----------------------------

-- ----------------------------
-- Table structure for `vm_table`
-- ----------------------------
DROP TABLE IF EXISTS `vm_table`;
CREATE TABLE `vm_table` (
  `vm_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'primary key，虚拟机UUID，虚拟机的唯一标识',
  `vm_name` varchar(36) DEFAULT NULL COMMENT '虚拟机名称',
  `host_id` bigint(20) DEFAULT NULL COMMENT '虚拟机当前所在的服务器UUID',
  `os_ip` varchar(15) DEFAULT NULL COMMENT '虚拟机操作系统IP',
  `vm_mark` varchar(200) DEFAULT NULL COMMENT '虚拟机描述',
  `cluster_id` bigint(20) DEFAULT NULL COMMENT '虚拟机所属集群，如果不在集群中，此字段为NULL',
  `data_center_id` bigint(20) DEFAULT NULL COMMENT '虚拟机所属数据中心UUID',
  `state` varchar(36) DEFAULT NULL COMMENT '虚拟机当前状态，有以下几种“运行”“关机”“异常”“挂起”“暂停”',
  `storage_volume_id` bigint(20) DEFAULT NULL COMMENT '存储卷id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者',
  PRIMARY KEY (`vm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of vm_table
-- ----------------------------

-- ----------------------------
-- Table structure for `vm_template_table`
-- ----------------------------
DROP TABLE IF EXISTS `vm_template_table`;
CREATE TABLE `vm_template_table` (
  `vm_template_id` bigint(20) NOT NULL COMMENT '虚拟机模板唯一标识',
  `vm_id` bigint(20) DEFAULT NULL COMMENT '虚拟机id',
  `vm_hardware_id` bigint(20) DEFAULT NULL COMMENT '虚拟机配置id',
  `vm_template_gen` varchar(255) DEFAULT NULL COMMENT '虚拟机模板生成方式（转换、克隆）',
  `create_time` datetime DEFAULT NULL COMMENT '虚拟机模板创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `vm_template_path` varchar(255) DEFAULT NULL COMMENT '虚拟机模板路径',
  `vm_template_type` varchar(20) DEFAULT NULL COMMENT '虚拟机模板类型',
  `vm_template_name` varchar(50) DEFAULT NULL COMMENT '虚拟机模板名称',
  PRIMARY KEY (`vm_template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of vm_template_table
-- ----------------------------

-- ----------------------------
-- Table structure for `config_table`
-- ----------------------------
DROP TABLE IF EXISTS `config_table`;
CREATE TABLE `config_table` (
  `config_id` bigint(20) NOT NULL COMMENT '配置id',
  `config_code` varchar(255) DEFAULT NULL COMMENT '配置键',
  `config_value` varchar(255) DEFAULT NULL COMMENT '配置值',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of config_table
-- ----------------------------
INSERT INTO `config_table` VALUES ('1', 'cluster_monitor_code', '0 */5 * * * ?', null, '1');
INSERT INTO `config_table` VALUES ('2', 'host_monitor_code', '0 */5 * * * ?', null, '1');
INSERT INTO `config_table` VALUES ('3', 'vm_monitor_code', '0 */5 * * * ?', null, '1');
INSERT INTO `config_table` VALUES ('4', 'ip_monitor_code', '0 */5 * * * ?', null, '1');
INSERT INTO `config_table` VALUES ('5', 'vlan_monitor_code', '0 */5 * * * ?', null, '1');
INSERT INTO `config_table` VALUES ('6', 'storage_monitor_code', '0 */5 * * * ?', null, '1');
INSERT INTO `config_table` VALUES ('7', 'host_status_sync_code', '0 */1 * * * ?', null, '1');

-- ----------------------------
-- Table structure for `storage_chunk_table`
-- ----------------------------
DROP TABLE IF EXISTS `storage_chunk_table`;
CREATE TABLE `storage_chunk_table` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `chunk_number` int(11) DEFAULT NULL COMMENT '当前文件块，从1开始',
  `chunk_size` bigint(20) DEFAULT NULL COMMENT '分块大小',
  `current_chunk_size` bigint(20) DEFAULT NULL COMMENT '当前分块大小',
  `total_size` bigint(20) DEFAULT NULL COMMENT '总大小',
  `identifier` varchar(255) DEFAULT NULL COMMENT '文件标识',
  `filename` varchar(255) DEFAULT NULL COMMENT '文件名',
  `relative_path` varchar(255) DEFAULT NULL COMMENT '相对路径',
  `total_chunks` int(11) DEFAULT NULL COMMENT '总块数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of storage_chunk_table
-- ----------------------------