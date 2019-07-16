/*
Navicat MySQL Data Transfer

Source Server         : 183.136.213.178
Source Server Version : 50644
Source Host           : 183.136.213.178:3306
Source Database       : nginx

Target Server Type    : MYSQL
Target Server Version : 50644
File Encoding         : 65001

Date: 2019-07-16 14:57:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for api
-- ----------------------------
DROP TABLE IF EXISTS `api`;
CREATE TABLE `api` (
  `api_id` int(11) NOT NULL AUTO_INCREMENT,
  `api_name` varchar(255) DEFAULT NULL,
  `api_uri` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`api_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of api
-- ----------------------------
INSERT INTO `api` VALUES ('1', '获取指定server下location', '/admin/nginx/3/server/abc.com/80');
INSERT INTO `api` VALUES ('2', '添加location', '/admin/nginx/3/server/abc.com/80');

-- ----------------------------
-- Table structure for api_env
-- ----------------------------
DROP TABLE IF EXISTS `api_env`;
CREATE TABLE `api_env` (
  `env_id` int(11) NOT NULL,
  `env_name` varchar(255) DEFAULT NULL,
  `env_ctx` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`env_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of api_env
-- ----------------------------
INSERT INTO `api_env` VALUES ('1', '开发环境', 'http://127.0.0.1:8888');
INSERT INTO `api_env` VALUES ('2', '测试环境', 'http://nginx.eziep.net');

-- ----------------------------
-- Table structure for api_request
-- ----------------------------
DROP TABLE IF EXISTS `api_request`;
CREATE TABLE `api_request` (
  `request_id` int(11) NOT NULL,
  `request_method` int(2) DEFAULT NULL COMMENT '1:get,2:post,3:put,4:delete',
  `request_body_type` varchar(255) DEFAULT NULL,
  `reuqest_body` varchar(255) DEFAULT NULL,
  `request_content_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of api_request
-- ----------------------------
INSERT INTO `api_request` VALUES ('1', '1', '', '', '');
INSERT INTO `api_request` VALUES ('2', '2', 'String', '{\r\n            \"path\":\"/test1\",\r\n            \"root\":\"html\"\r\n        }', 'application/json;UTF-8');
INSERT INTO `api_request` VALUES ('3', '2', 'String', '{\r\n            \"path\":\"/4444444444444444444444444444\",\r\n            \"root\":\"html\"\r\n        }', 'application/json;UTF-8');

-- ----------------------------
-- Table structure for build_info
-- ----------------------------
DROP TABLE IF EXISTS `build_info`;
CREATE TABLE `build_info` (
  `build_id` int(11) NOT NULL AUTO_INCREMENT,
  `build_name` varchar(255) DEFAULT NULL,
  `build_type_id` int(11) DEFAULT '1',
  `build_args` text,
  `start_shell` text,
  `stop_shell` text,
  `status_shell` text,
  PRIMARY KEY (`build_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of build_info
-- ----------------------------
INSERT INTO `build_info` VALUES ('1', 'smart-nginx自动编译', '1', '{\"workspace\":\"/root/workspace\",\"mavenHome\":\"/root/workspace/apache-maven-3.6.1\",\"git\":\"git@github.com:mulhyac/smart-nginx.git\",\"javaHome\":\"/root/workspace/jdk1.8.0_131\",\"log\":\"/home/eziep/Desktop/log\",\"runtime\":\"/root/workspace/runtime\"}', 'nohup java -jar ${runtime}/${moduleName}-${version}.jar >>${runtime}/${moduleName}.log &', 'ps -ef |grep ${moduleName}-${version}.jar |grep -v grep|awk \'{print $2}\'|xargs kill -9', null);

-- ----------------------------
-- Table structure for build_linux
-- ----------------------------
DROP TABLE IF EXISTS `build_linux`;
CREATE TABLE `build_linux` (
  `build_linux_id` int(11) NOT NULL AUTO_INCREMENT,
  `build_id` int(11) DEFAULT NULL,
  `linux_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`build_linux_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of build_linux
-- ----------------------------
INSERT INTO `build_linux` VALUES ('1', '1', '3');

-- ----------------------------
-- Table structure for build_type
-- ----------------------------
DROP TABLE IF EXISTS `build_type`;
CREATE TABLE `build_type` (
  `build_type_id` int(11) NOT NULL,
  `build_type_name` varchar(255) DEFAULT NULL,
  `build_type_args` text,
  `build_class` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`build_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of build_type
-- ----------------------------
INSERT INTO `build_type` VALUES ('1', 'SpringBoot编译', '[{\"workspace\":\"\",\"title\":\"工作空间\",\"form\":\"text\"},{\"git\":\"\",\"title\":\"仓库地址\",\"form\":\"text\"},{\"logPath\":\"/root\",\"title\":\"日志路径\",\"form\":\"text\"},{\"mavenHome\":\"\",\"title\":\"mavenHome\",\"form\":\"text\"},{\"javaHome\":\"\",\"title\":\"javaHome\",\"form\":\"text\"},{\"runtime\":\"\",\"title\":\"运行目录\",\"form\":\"text\"}]', 'com.zejor.devops.autobuild.springboot.SpringBootBuild');

-- ----------------------------
-- Table structure for linux
-- ----------------------------
DROP TABLE IF EXISTS `linux`;
CREATE TABLE `linux` (
  `linux_id` int(11) NOT NULL AUTO_INCREMENT,
  `linux_name` varchar(255) DEFAULT NULL,
  `linux_host` varchar(255) DEFAULT NULL,
  `linux_port` int(8) DEFAULT NULL,
  `linux_user` varchar(32) DEFAULT NULL,
  `linux_pwd` varchar(128) DEFAULT NULL,
  `linux_intro` varchar(255) DEFAULT NULL,
  `add_time` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`linux_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of linux
-- ----------------------------
INSERT INTO `linux` VALUES ('1', '开发笔记本', '127.0.0.1', '22', 'root', '', '我要用根用户进行启动，配置管理，不用eziep', null);
INSERT INTO `linux` VALUES ('2', '公司184', '192.168.1.184', '22', 'root', '', '这里你可以随便添加一些介绍或备注信息，这句话只是一个测试', null);
INSERT INTO `linux` VALUES ('3', '托管机器', '183.136.213.178', '22', 'root', '', null, null);

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `log_type` int(1) DEFAULT '1' COMMENT '1:本地,2:远程',
  `linux_id` int(8) DEFAULT NULL,
  `log_path` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ----------------------------
-- Table structure for nginx
-- ----------------------------
DROP TABLE IF EXISTS `nginx`;
CREATE TABLE `nginx` (
  `nginx_id` int(11) NOT NULL AUTO_INCREMENT,
  `nginx_name` varchar(32) DEFAULT NULL,
  `nginx_home` varchar(255) DEFAULT NULL,
  `nginx_conf` varchar(255) DEFAULT NULL,
  `nginx_intro` varchar(255) DEFAULT NULL,
  `nginx_status` int(2) DEFAULT '0' COMMENT '0:未启动，1:已启动，2:不可用',
  `host_id` int(11) DEFAULT NULL,
  `add_time` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`nginx_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of nginx
-- ----------------------------
INSERT INTO `nginx` VALUES ('3', '该数据不可进行任何操作', '/usr/local/nginx', '/home/eziep/Desktop/nginx/1/nginx2.conf', '80', '0', '1', null);
INSERT INTO `nginx` VALUES ('4', '该数据不可进行任何操作', '/usr/local/nginx', '/usr/local/nginx/conf/nginx.conf', '80', '0', '80', null);
INSERT INTO `nginx` VALUES ('5', '不要删除，可任意操作', 'fdasf', '/root/nginx.conf', 'sadf', '1', '1', '2019-06-27 14:59:55 720');
INSERT INTO `nginx` VALUES ('6', 'fafa', 'fafa', 'fdafda', 'ewrwr', '1', '2', null);
INSERT INTO `nginx` VALUES ('7', 'qqqqqqqq', 'qqqqqqqqq', 'fafa', 'faf', '1', '1', null);
INSERT INTO `nginx` VALUES ('8', 'aaaa', 'aaaa', 'aaa', 'aaa', '1', '2', null);
INSERT INTO `nginx` VALUES ('23', '2222222222222', '22222222222222', '222222222222222', '222222222222222', '1', '2', null);

-- ----------------------------
-- Table structure for spring_boot
-- ----------------------------
DROP TABLE IF EXISTS `spring_boot`;
CREATE TABLE `spring_boot` (
  `boot_id` int(11) NOT NULL,
  `boot_name` varchar(255) DEFAULT NULL,
  `git_url` varchar(255) DEFAULT NULL,
  `git_user` varchar(255) DEFAULT NULL,
  `git_pwd` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`boot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of spring_boot
-- ----------------------------
INSERT INTO `spring_boot` VALUES ('1', '快速运维开发辅', null, null, null);
