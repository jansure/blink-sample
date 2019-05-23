
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `jobs`
-- ----------------------------
DROP TABLE IF EXISTS `jobs`;
CREATE TABLE `jobs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(62) NOT NULL,
  `job_status` tinyint(1) NOT NULL COMMENT '任务状态：0，创建中；1，成功；-1，失败',
  `create_time` datetime NOT NULL,
  `end_time` datetime DEFAULT NULL,
  `error` varchar(1024) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jobs
-- ----------------------------

-- ----------------------------
-- Table structure for `schame`
-- ----------------------------
DROP TABLE IF EXISTS `schame`;
CREATE TABLE `schame` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(64) NOT NULL,
  `describe` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
