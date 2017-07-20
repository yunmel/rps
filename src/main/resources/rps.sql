/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50540
Source Host           : localhost:3306
Source Database       : rps

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2017-07-20 18:33:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_base_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_base_user`;
CREATE TABLE `t_base_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `name` varchar(16) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `level` int(2) DEFAULT NULL,
  `score` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_base_user
-- ----------------------------
INSERT INTO `t_base_user` VALUES ('2', 'admin', 'admin', '系统管理员', 'admin@wallstreetcn.com', '1', '0');
INSERT INTO `t_base_user` VALUES ('3', 'ad', 'asd', 'asd', 'asd', '1', '0');
INSERT INTO `t_base_user` VALUES ('4', 'qwe', 'qwe', 'qwe', 'qwe', '1', '0');
INSERT INTO `t_base_user` VALUES ('5', 'asd', 'asd', 'asd', 'asd', '1', '0');

-- ----------------------------
-- Table structure for `t_core_project`
-- ----------------------------
DROP TABLE IF EXISTS `t_core_project`;
CREATE TABLE `t_core_project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `sn` varchar(32) DEFAULT NULL,
  `describe` varchar(255) DEFAULT NULL,
  `mngr` bigint(20) DEFAULT NULL,
  `start` date DEFAULT NULL,
  `end` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_core_project
-- ----------------------------
INSERT INTO `t_core_project` VALUES ('1', '安全生产信息化管理系统', ' spms', 'dasdsadasdaaaaaaaaaaadsad', '1', '2017-04-01', '2017-07-30');

-- ----------------------------
-- Table structure for `t_core_task`
-- ----------------------------
DROP TABLE IF EXISTS `t_core_task`;
CREATE TABLE `t_core_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project` bigint(20) DEFAULT NULL,
  `label` varchar(64) DEFAULT NULL,
  `pusher` bigint(20) DEFAULT NULL,
  `exp` int(11) DEFAULT '5',
  `result` varchar(128) DEFAULT NULL,
  `describe` varchar(255) DEFAULT NULL,
  `classify` int(1) DEFAULT NULL,
  `process` int(3) DEFAULT NULL,
  `fettle` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_core_task
-- ----------------------------
