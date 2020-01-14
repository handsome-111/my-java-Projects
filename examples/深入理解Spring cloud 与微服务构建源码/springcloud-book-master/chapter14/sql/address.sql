/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50640
Source Host           : localhost:3306
Source Database       : hdgj

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2019-09-28 20:09:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `address`
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `id` int(15) NOT NULL AUTO_INCREMENT,
  `email` varchar(155) DEFAULT NULL COMMENT '邮箱地址',
  `name` varchar(150) DEFAULT NULL COMMENT '姓名',
  `company` varchar(255) DEFAULT NULL COMMENT '公司',
  `phone` varchar(100) DEFAULT NULL COMMENT '电话',
  `fax` varchar(150) DEFAULT NULL COMMENT '传真',
  `street1` text COMMENT '街道地址1',
  `street2` varchar(255) DEFAULT NULL COMMENT '街道地址2',
  `city` varchar(150) DEFAULT NULL COMMENT '城市',
  `province` varchar(50) DEFAULT NULL COMMENT '省/州',
  `post` varchar(50) DEFAULT NULL COMMENT '邮编',
  `region` varchar(50) DEFAULT NULL COMMENT '收货人所在区',
  `country` varchar(50) DEFAULT NULL COMMENT '国家',
  `userid` int(20) DEFAULT NULL COMMENT '用户的id',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_default` int(11) NOT NULL DEFAULT '2' COMMENT '1代表是默认地址，2代表不是',
  `idCardNo` varchar(50) DEFAULT NULL COMMENT '身份证号',
  PRIMARY KEY (`id`),
  KEY `is_default_index` (`is_default`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of address
-- ----------------------------
INSERT INTO `address` VALUES ('2', '1040978586@qq.com', '张三', '海口高新区', '13379959770', null, '南海大道孵化中心', null, '海口市', '海南省', null, null, null, '11', '2019-09-20 03:05:09', '2019-09-20 03:09:55', '1', null);
INSERT INTO `address` VALUES ('3', '趣味', 'qweqw', 'qwe', 'qewqwe', 'qwqe', '国贸路', null, '三亚市', '海南省', null, null, null, '11', '2019-09-21 18:56:31', '2019-09-21 18:56:31', '2', null);
INSERT INTO `address` VALUES ('10', null, '吴宏健', null, '13379959770', null, '某条该', null, '北京市', '北京市', '', '东城区', null, null, '2019-09-25 21:16:23', '2019-09-25 21:16:23', '1', null);
