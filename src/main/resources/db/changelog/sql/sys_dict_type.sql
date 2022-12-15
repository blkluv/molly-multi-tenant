/*
 Navicat Premium Data Transfer

 Source Server         : my-mysql
 Source Server Type    : MySQL
 Source Server Version : 80031 (8.0.31)
 Source Host           : localhost:3306
 Source Schema         : molly_master

 Target Server Type    : MySQL
 Target Server Version : 80031 (8.0.31)
 File Encoding         : 65001

 Date: 15/12/2022 10:30:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (10001, '用户性别', 'sys_user_sex', '用户性别列表', '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00', 19980817);
INSERT INTO `sys_dict_type` VALUES (10002, '菜单状态', 'sys_show_hide', '菜单状态列表', '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00', 19980817);
INSERT INTO `sys_dict_type` VALUES (10003, '系统开关', 'sys_normal_disable', '系统开关列表', '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00', 19980817);
INSERT INTO `sys_dict_type` VALUES (10004, '任务状态', 'sys_job_status', '任务状态列表', '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00', 19980817);
INSERT INTO `sys_dict_type` VALUES (10005, '任务分组', 'sys_job_group', '任务分组列表', '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00', 19980817);
INSERT INTO `sys_dict_type` VALUES (10006, '系统是否', 'sys_yes_no', '系统是否列表', '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00', 19980817);
INSERT INTO `sys_dict_type` VALUES (10007, '通知类型', 'sys_notice_type', '通知类型列表', '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00', 19980817);
INSERT INTO `sys_dict_type` VALUES (10008, '通知状态', 'sys_notice_status', '通知状态列表', '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00', 19980817);
INSERT INTO `sys_dict_type` VALUES (10009, '操作类型', 'sys_oper_type', '操作类型列表', '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00', 19980817);
INSERT INTO `sys_dict_type` VALUES (10010, '系统状态', 'sys_common_status', '登录状态列表', '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00', 19980817);
INSERT INTO `sys_dict_type` VALUES (10011, '警情类型', 'sys_fault_type', '警情类型列表', '2020-07-01 11:29:23', 19980817, '2020-07-01 11:30:02', 19980817);
INSERT INTO `sys_dict_type` VALUES (10012, '菜单类型', 'sys_menu_type', '菜单类型', '2022-06-25 10:02:55', 19980817, '2022-06-25 10:02:59', 19980817);
INSERT INTO `sys_dict_type` VALUES (10013, '所属用户', 'sys_menu_target', '所属用户列表', '2022-06-25 10:03:27', 19980817, '2022-06-25 10:03:30', 19980817);
INSERT INTO `sys_dict_type` VALUES (10014, '统计查询类型', 'statistics_query_type', '统计查询类型', '2022-07-09 14:05:30', 19980817, '2022-07-09 14:05:30', 19980817);

SET FOREIGN_KEY_CHECKS = 1;
