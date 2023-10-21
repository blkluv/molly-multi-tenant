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

 Date: 15/12/2022 10:30:32
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Records of cms_project
-- ----------------------------
INSERT INTO `cms_project` (`project_id`, `project_name`, `linkman`, `contact_number`, `area_code`, `address`,
                           `sort`, `password`, `status`, `dept_id`, `create_time`, `create_user`,
                           `last_update_time`, `last_update_user`)
VALUES (10001, '默认项目', '李默认', '0755-1234567', 440307000000, '南湾街道 左右云创谷A栋1座',
        1, '$2a$10$omnFqGPZ3/pMfctkVkxiQOYMUC8pp1iNsaJDI8vQmLdtIdAXvlS8K',
        1, 10001, '2023-08-11 11:17:33', 19980817, '2023-08-11 10:12:24', 19980817);

SET
FOREIGN_KEY_CHECKS = 1;
