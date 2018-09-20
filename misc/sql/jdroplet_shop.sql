/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50714
Source Host           : localhost:3306
Source Database       : jdroplet_shop

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2018-09-20 16:49:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for jdroplet_activities
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_activities`;
CREATE TABLE `jdroplet_activities` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL COMMENT '活动名称',
  `shop_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建者',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `start_time` timestamp NOT NULL COMMENT '活动开始时间',
  `expired` timestamp NOT NULL COMMENT '活动失效时间',
  `status` int(10) unsigned DEFAULT '0' COMMENT '状态0正常',
  `type` varchar(32) DEFAULT NULL COMMENT '游戏类型',
  `description` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_activities
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_activitymeta
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_activitymeta`;
CREATE TABLE `jdroplet_activitymeta` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(10) unsigned NOT NULL,
  `meta_key` varchar(255) NOT NULL,
  `meta_value` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_activitymeta
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_activity_users
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_activity_users`;
CREATE TABLE `jdroplet_activity_users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shop_id` int(10) unsigned NOT NULL,
  `activity_id` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `count` int(10) unsigned NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shop_act_user` (`shop_id`,`activity_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of jdroplet_activity_users
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_attachments
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_attachments`;
CREATE TABLE `jdroplet_attachments` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shop_id` int(10) unsigned NOT NULL,
  `item_id` int(10) unsigned DEFAULT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `total_downloads` int(10) unsigned NOT NULL,
  `size` bigint(20) unsigned NOT NULL,
  `filename` varchar(512) NOT NULL,
  `disk_filename` varchar(128) NOT NULL,
  `content_type` varchar(24) NOT NULL,
  `extension` varchar(16) NOT NULL,
  `description` varchar(512) DEFAULT '',
  `has_thumb` tinyint(1) unsigned DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `folder` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_jdroplet_attachments_site` (`shop_id`),
  KEY `FK_jdroplet_attachments_post` (`item_id`),
  KEY `FK_jdroplet_attachments_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_attachments
-- ----------------------------
 
-- ----------------------------
-- Table structure for jdroplet_attributes
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_attributes`;
CREATE TABLE `jdroplet_attributes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `section_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '分类ID',
  `name` varchar(30) NOT NULL COMMENT '属性名称',
  `sequence` int(11) DEFAULT '0' COMMENT '排序',
  `type` varchar(50) DEFAULT NULL COMMENT '属性类别',
  `form_type` varchar(100) NOT NULL COMMENT '表单类型',
  `placeholder` varchar(100) DEFAULT NULL COMMENT '属性输入框提示文字',
  `reg` varchar(100) DEFAULT NULL COMMENT '属性输入框校验正则',
  `unique` int(1) unsigned NOT NULL DEFAULT '0' COMMENT '值是否可重复',
  `search_able` int(1) unsigned NOT NULL DEFAULT '1' COMMENT '值是否可搜索',
  `required` int(1) unsigned NOT NULL DEFAULT '0' COMMENT '值是否必填',
  `args` text COMMENT '输入框CSS参数',
  `screening_show` int(1) unsigned DEFAULT '0' COMMENT '筛查选项中是否显示',
  `default_value` text COMMENT '属性默认值',
  `length` int(10) unsigned DEFAULT '255' COMMENT '内容最大长度',
  `postfix` varchar(10) DEFAULT NULL COMMENT '属性后缀',
  `warning_msg` varchar(128) DEFAULT '',
  `error_msg` varchar(128) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分类信息 分类属性';

-- ----------------------------
-- Records of jdroplet_attributes
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_attribute_values
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_attribute_values`;
CREATE TABLE `jdroplet_attribute_values` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(32) DEFAULT '',
  `attribute_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '属性ID',
  `item_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '内容ID',
  `value` text NOT NULL COMMENT '值',
  `slug` varchar(64) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分类信息属性值';

-- ----------------------------
-- Records of jdroplet_attribute_values
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_bills
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_bills`;
CREATE TABLE `jdroplet_bills` (
  `id` varchar(64) NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `fee` int(10) NOT NULL,
  `balance` int(10) unsigned NOT NULL DEFAULT '0',
  `coupon_values` int(10) unsigned NOT NULL DEFAULT '0',
  `type` int(11) unsigned NOT NULL,
  `tran_no` bigint(20) unsigned DEFAULT NULL,
  `order_id` bigint(20) unsigned DEFAULT NULL,
  `item_id` bigint(20) unsigned DEFAULT NULL,
  `description` varchar(128) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_order` (`user_id`,`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jdroplet_bills
-- ----------------------------
 

-- ----------------------------
-- Table structure for jdroplet_checkins
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_checkins`;
CREATE TABLE `jdroplet_checkins` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shop_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `first_date` date NOT NULL,
  `last_date` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jdroplet_checkins
-- ----------------------------
 
-- ----------------------------
-- Table structure for jdroplet_clustermeta
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_clustermeta`;
CREATE TABLE `jdroplet_clustermeta` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(10) unsigned DEFAULT NULL,
  `meta_key` varchar(255) DEFAULT NULL,
  `meta_value` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_clustermeta
-- ----------------------------
INSERT INTO `jdroplet_clustermeta` VALUES ('1', '2', 'MaxAttachmentSize', '10240');
INSERT INTO `jdroplet_clustermeta` VALUES ('2', '3', 'weibo_appid', null);
INSERT INTO `jdroplet_clustermeta` VALUES ('3', '3', 'EnableWechatUnauthorizeLogin', 'true');

-- ----------------------------
-- Table structure for jdroplet_clusters
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_clusters`;
CREATE TABLE `jdroplet_clusters` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_clusters
-- ----------------------------
INSERT INTO `jdroplet_clusters` VALUES ('1', 'JDroplet Manage');
INSERT INTO `jdroplet_clusters` VALUES ('2', 'JDroplet API');
INSERT INTO `jdroplet_clusters` VALUES ('3', 'READ');
INSERT INTO `jdroplet_clusters` VALUES ('4', 'HM');
INSERT INTO `jdroplet_clusters` VALUES ('5', 'Blog');
INSERT INTO `jdroplet_clusters` VALUES ('6', 'Shop');
INSERT INTO `jdroplet_clusters` VALUES ('7', 'Info');
INSERT INTO `jdroplet_clusters` VALUES ('8', 'Bio');

-- ----------------------------
-- Table structure for jdroplet_comments
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_comments`;
CREATE TABLE `jdroplet_comments` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `component` varchar(32) DEFAULT NULL,
  `type` varchar(32) DEFAULT NULL,
  `content` varchar(1024) DEFAULT NULL,
  `item_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `likes` int(10) unsigned DEFAULT '0',
  `post_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` int(10) unsigned DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jdroplet_comments
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_contacts
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_contacts`;
CREATE TABLE `jdroplet_contacts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shop_id` int(10) unsigned NOT NULL,
  `activity_id` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned NOT NULL DEFAULT '0',
  `user_name` varchar(64) NOT NULL DEFAULT '',
  `contact_order` int(11) DEFAULT '1',
  `area` varchar(64) DEFAULT '',
  `code` varchar(64) DEFAULT '',
  `phone` varchar(16) DEFAULT '',
  `address` varchar(256) DEFAULT '',
  `is_default` int(11) DEFAULT '0',
  `mail` varchar(64) DEFAULT '',
  `remark` varchar(256) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_contacts
-- ----------------------------
 
-- ----------------------------
-- Table structure for jdroplet_coupons
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_coupons`;
CREATE TABLE `jdroplet_coupons` (
  `id` bigint(20) unsigned NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `name` varchar(64) NOT NULL,
  `value` int(10) unsigned NOT NULL,
  `balance` int(10) NOT NULL DEFAULT '0',
  `type` varchar(6) NOT NULL,
  `expired` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jdroplet_coupons
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_crons
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_crons`;
CREATE TABLE `jdroplet_crons` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `description` varchar(1024) DEFAULT '',
  `refer` varchar(1024) NOT NULL,
  `cron` varchar(256) NOT NULL,
  `status` int(10) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jdroplet_crons
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_drafts
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_drafts`;
CREATE TABLE `jdroplet_drafts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `item_id` int(10) unsigned NOT NULL,
  `type` varchar(32) NOT NULL,
  `content` text NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_book` (`user_id`,`item_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_drafts
-- ----------------------------
 
-- ----------------------------
-- Table structure for jdroplet_goods
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_goods`;
CREATE TABLE `jdroplet_goods` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shop_id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL,
  `excerpt` varchar(512) DEFAULT NULL,
  `description` longtext,
  `sku` varchar(255) DEFAULT NULL,
  `price` int(10) DEFAULT NULL,
  `sale_price` decimal(10,0) DEFAULT NULL,
  `cost` decimal(10,0) DEFAULT NULL,
  `status` int(10) unsigned NOT NULL,
  `section_id1` int(10) unsigned DEFAULT NULL,
  `section_id2` int(10) unsigned DEFAULT NULL,
  `section_id3` int(10) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `stock_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `stocks` int(10) unsigned DEFAULT NULL,
  `tags` varchar(128) DEFAULT NULL,
  `property_name1` varchar(32) DEFAULT NULL,
  `property_name2` varchar(32) DEFAULT NULL,
  `property_name3` varchar(32) DEFAULT NULL,
  `pics` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_goods
-- ----------------------------
INSERT INTO `jdroplet_goods` VALUES ('1', '1', 'Apple iPhone X (A1903)', '最棒的iPhoneX只属于你', '<img src=\"https://img.yzcdn.cn/upload_files/2018/05/12/FpBXCOi8U-XzUxiorLsh9GrVUUfq.jpg!730x0.jpg\" alt=\"\" /><br /><img src=\"https://img.yzcdn.cn/upload_files/2018/05/02/FlEwnN6Rkiz-kMTDgqftZ8Lnzf99.jpg!730x0.jpg\" alt=\"\" />', null, '999900', '888800', '777700', '1', '0', '0', '0', '2018-09-18 09:18:26', '2018-09-18 09:18:26', '999', '', '颜色', '容量', null, '2018/9/c610eecc479d6e1b315801aebe14da6f_1000001.png');

-- ----------------------------
-- Table structure for jdroplet_goods_spec
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_goods_spec`;
CREATE TABLE `jdroplet_goods_spec` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shop_id` int(10) unsigned NOT NULL,
  `goods_id` int(10) unsigned NOT NULL,
  `sku` varchar(255) DEFAULT NULL,
  `price` int(10) unsigned NOT NULL,
  `cost` int(10) unsigned DEFAULT NULL,
  `stocks` int(10) unsigned NOT NULL,
  `status` int(10) unsigned DEFAULT '1',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `property1` int(10) DEFAULT NULL,
  `property2` int(10) DEFAULT NULL,
  `property3` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of jdroplet_goods_spec
-- ----------------------------
INSERT INTO `jdroplet_goods_spec` VALUES ('1', '1', '1', 'A1903', '999900', '888800', '99', '1', '2018-09-10 17:35:30', '7', '8', null);
INSERT INTO `jdroplet_goods_spec` VALUES ('2', '1', '1', 'A1904', '1099900', '888800', '99', '1', '2018-09-10 17:35:30', '7', '9', null);
INSERT INTO `jdroplet_goods_spec` VALUES ('3', '1', '1', 'A1905', '999900', '888800', '99', '1', '2018-09-10 17:35:30', '10', '8', null);
INSERT INTO `jdroplet_goods_spec` VALUES ('4', '1', '1', 'A1906', '1099900', '888800', '99', '1', '2018-09-10 17:35:30', '10', '9', null);

-- ----------------------------
-- Table structure for jdroplet_groups
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_groups`;
CREATE TABLE `jdroplet_groups` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shop_id` int(10) unsigned DEFAULT '0',
  `activity_id` int(10) unsigned DEFAULT NULL,
  `name` varchar(64) DEFAULT '',
  `user_id` int(10) unsigned DEFAULT '0',
  `rank` int(10) unsigned DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `type` varchar(32) DEFAULT '',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `owner_id_activity_id` (`activity_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_groups
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_groups_members
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_groups_members`;
CREATE TABLE `jdroplet_groups_members` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `activity_id` int(10) unsigned DEFAULT '0',
  `group_id` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `inviter_id` int(10) unsigned DEFAULT '0',
  `is_admin` int(10) unsigned DEFAULT '0',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`) USING BTREE,
  KEY `activity_id_group_id` (`activity_id`,`group_id`) USING BTREE,
  KEY `activity_id_user_id` (`activity_id`,`user_id`) USING BTREE,
  KEY `user_id_group_id_activity_id` (`activity_id`,`group_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_groups_members
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_links
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_links`;
CREATE TABLE `jdroplet_links` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `post_date` int(11) NOT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `size` varchar(255) NOT NULL,
  `cat` varchar(255) NOT NULL,
  `tags` varchar(255) NOT NULL,
  `urls` longtext NOT NULL,
  `description` longtext NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_post_date` (`post_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jdroplet_links
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_logs
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_logs`;
CREATE TABLE `jdroplet_logs` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned DEFAULT NULL,
  `tag` varchar(64) DEFAULT NULL,
  `content` text,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `type` (`tag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_logs
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_lotteries
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_lotteries`;
CREATE TABLE `jdroplet_lotteries` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `shop_id` int(10) unsigned NOT NULL COMMENT '关联板块id',
  `activity_id` int(10) unsigned NOT NULL COMMENT '关联活动id',
  `user_id` int(10) unsigned NOT NULL DEFAULT '0',
  `count` int(10) unsigned NOT NULL COMMENT '数量',
  `rate` int(10) unsigned NOT NULL COMMENT '概率',
  `rank` int(10) unsigned NOT NULL COMMENT '级别',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '标题',
  `icon` varchar(1024) DEFAULT NULL COMMENT '图标',
  `message` varchar(1024) NOT NULL,
  `point` int(10) unsigned NOT NULL COMMENT '积分',
  `flag` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_lotteries
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_lotterymeta
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_lotterymeta`;
CREATE TABLE `jdroplet_lotterymeta` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(10) unsigned NOT NULL,
  `meta_key` varchar(255) NOT NULL,
  `meta_value` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_lotterymeta
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_lottery_users
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_lottery_users`;
CREATE TABLE `jdroplet_lottery_users` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `activity_id` int(10) unsigned DEFAULT NULL,
  `sender_id` int(10) unsigned DEFAULT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  `item_id` int(10) unsigned DEFAULT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
  `age` int(10) unsigned DEFAULT NULL,
  `status` int(10) unsigned DEFAULT '1',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jdroplet_lottery_users
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_messages
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_messages`;
CREATE TABLE `jdroplet_messages` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sender_id` int(10) unsigned NOT NULL,
  `content` varchar(4096) CHARACTER SET utf8mb4 NOT NULL,
  `post_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sender_id` (`sender_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of jdroplet_messages
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_messages_recipients
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_messages_recipients`;
CREATE TABLE `jdroplet_messages_recipients` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `message_id` int(10) unsigned NOT NULL,
  `is_read` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `is_sender` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `thread_id` (`message_id`),
  KEY `is_deleted` (`status`),
  KEY `sender_only` (`is_sender`),
  KEY `unread_count` (`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of jdroplet_messages_recipients
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_orders
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_orders`;
CREATE TABLE `jdroplet_orders` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) unsigned DEFAULT '0',
  `trade_no` varchar(128) DEFAULT NULL,
  `platform` varchar(16) DEFAULT NULL,
  `product_id` int(10) unsigned DEFAULT '0',
  `item_id` int(10) unsigned DEFAULT '0',
  `user_id` int(10) unsigned DEFAULT '0',
  `shop_id` int(10) unsigned DEFAULT '1',
  `ticket_id` int(10) unsigned DEFAULT '0',
  `contact_id` int(10) unsigned DEFAULT NULL,
  `shop_name` varchar(64) DEFAULT '',
  `create_time` timestamp NULL DEFAULT NULL,
  `pay_time` timestamp NULL DEFAULT NULL,
  `shipping_amount` int(10) unsigned DEFAULT '0',
  `coupon_values` int(10) unsigned DEFAULT '0',
  `total_amount` int(10) unsigned DEFAULT NULL,
  `pay_amount` int(10) unsigned DEFAULT NULL,
  `quantity` int(11) unsigned DEFAULT '1',
  `status` int(11) unsigned DEFAULT '3',
  `type` varchar(26) NOT NULL,
  `remarks` varchar(1024) DEFAULT '',
  `time` int(11) DEFAULT '0' COMMENT '充值 次数',
  `inviter` int(10) DEFAULT '0',
  `come_from` varchar(32) DEFAULT '',
  `new_user` int(10) unsigned DEFAULT NULL,
  `ip` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order` (`order_id`) USING BTREE,
  KEY `orders_order_id` (`order_id`) USING BTREE,
  KEY `orders_item_id` (`item_id`) USING BTREE,
  KEY `type_time` (`type`,`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jdroplet_orders
-- ----------------------------
INSERT INTO `jdroplet_orders` VALUES ('1', '10046901770001', null, null, '0', '0', '1000001', '1', '0', '15', null, '2018-09-18 10:07:02', null, '0', '0', '999900', '999900', '1', '1', 'goods', '', null, '0', null, '0', '127.0.0.1');

-- ----------------------------
-- Table structure for jdroplet_order_coupons
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_order_coupons`;
CREATE TABLE `jdroplet_order_coupons` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL,
  `coupon_id` bigint(20) NOT NULL,
  `coupon_type` varchar(32) DEFAULT NULL,
  `coupon_value` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_order_coupons
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_order_items
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_order_items`;
CREATE TABLE `jdroplet_order_items` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shop_id` int(10) NOT NULL,
  `order_id` bigint(20) unsigned NOT NULL,
  `product_id` int(10) unsigned NOT NULL,
  `spec_id` int(10) unsigned NOT NULL,
  `quantity` int(11) unsigned NOT NULL,
  `icon` varchar(1024) DEFAULT '',
  `description` varchar(1024) DEFAULT '',
  `name` varchar(64) DEFAULT NULL,
  `price` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_order_items
-- ----------------------------
INSERT INTO `jdroplet_order_items` VALUES ('1', '1', '10046901770001', '1', '3', '1', '2018/9/c610eecc479d6e1b315801aebe14da6f_1000001.png', '', 'Apple iPhone X (A1903) 黑色 64G', '999900');

-- ----------------------------
-- Table structure for jdroplet_page_pages
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_page_pages`;
CREATE TABLE `jdroplet_page_pages` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `refer` varchar(128) NOT NULL,
  `user_id` int(10) unsigned DEFAULT '1',
  `date_created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_jdroplet_page_pages_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_page_pages
-- ----------------------------
INSERT INTO `jdroplet_page_pages` VALUES ('1', 'index', 'jdroplet.app.view.admin.ManageMasterPage', '1', '2017-06-20 10:15:56');
INSERT INTO `jdroplet_page_pages` VALUES ('2', 'method', 'jdroplet.app.view.MethodPage', '1', '2017-06-20 16:07:50');
INSERT INTO `jdroplet_page_pages` VALUES ('3', 'message', 'jdroplet.app.view.MessagePage', '1', '2017-06-20 17:44:37');
INSERT INTO `jdroplet_page_pages` VALUES ('4', 'login', 'jdroplet.app.view.LoginPage', '1', '2017-06-22 16:23:10');
INSERT INTO `jdroplet_page_pages` VALUES ('5', 'manage', 'jdroplet.app.view.admin.ManageMasterPage', '1', '2017-06-27 21:43:50');
INSERT INTO `jdroplet_page_pages` VALUES ('6', 'oauth', 'jdroplet.app.view.admin.ManageOauthPage', '1', '2017-06-25 17:08:29');
INSERT INTO `jdroplet_page_pages` VALUES ('7', 'user', 'jdroplet.app.view.admin.ManageUserPage', '1', '2017-09-08 14:41:29');
INSERT INTO `jdroplet_page_pages` VALUES ('8', 'page', 'jdroplet.app.view.admin.ManagePagePage', '1', '2017-09-19 09:41:37');
INSERT INTO `jdroplet_page_pages` VALUES ('9', 'snsproxy', 'jdroplet.app.view.SnsProxyPage', '1', '2018-02-26 22:35:09');
INSERT INTO `jdroplet_page_pages` VALUES ('10', 'section', 'jdroplet.app.view.api.SectionPage', '1', '2017-09-25 11:52:19');
INSERT INTO `jdroplet_page_pages` VALUES ('11', 'post', 'jdroplet.app.view.api.PostPage', '1', '2017-09-06 11:52:37');
INSERT INTO `jdroplet_page_pages` VALUES ('12', 'user', 'jdroplet.app.view.api.UserPage', '1', '2017-09-13 17:56:10');
INSERT INTO `jdroplet_page_pages` VALUES ('13', 'contact', 'jdroplet.app.view.api.ContactPage', '1', '2017-09-13 21:50:26');
INSERT INTO `jdroplet_page_pages` VALUES ('14', 'order', 'jdroplet.app.view.OrderPage', '1', '2017-09-19 14:55:45');
INSERT INTO `jdroplet_page_pages` VALUES ('15', 'pay', 'jdroplet.app.view.PayPage', '1', '2017-09-19 14:55:53');
INSERT INTO `jdroplet_page_pages` VALUES ('16', 'file', 'jdroplet.app.view.FilePage', '1', '2017-09-21 17:17:32');
INSERT INTO `jdroplet_page_pages` VALUES ('17', 'role', 'jdroplet.app.view.api.RolePage', '1', '2017-09-22 23:13:26');
INSERT INTO `jdroplet_page_pages` VALUES ('18', 'url', 'jdroplet.app.view.admin.ManageUrlPage', '1', '2017-11-02 16:51:23');
INSERT INTO `jdroplet_page_pages` VALUES ('19', 'cluster', 'jdroplet.app.view.admin.ManageClusterPage', '1', '2017-11-03 11:28:29');
INSERT INTO `jdroplet_page_pages` VALUES ('20', 'site', 'jdroplet.app.view.admin.ManageSitePage', '1', '2017-11-06 09:47:26');
INSERT INTO `jdroplet_page_pages` VALUES ('21', 'book', 'jdroplet.app2.view.api.BookPage', '1', '2017-11-08 14:14:36');
INSERT INTO `jdroplet_page_pages` VALUES ('22', 'chapter', 'jdroplet.app2.view.api.ChapterPage', '1', '2017-11-09 16:17:51');
INSERT INTO `jdroplet_page_pages` VALUES ('23', 'section', 'jdroplet.app.view.admin.ManageSectionPage', '1', '2017-11-30 22:07:44');
INSERT INTO `jdroplet_page_pages` VALUES ('24', 'post', 'jdroplet.app.view.admin.ManagePostPage', '1', '2017-11-30 22:09:17');
INSERT INTO `jdroplet_page_pages` VALUES ('25', 'shop', 'jdroplet.app.view.api.ShopPage', '1', '2017-12-19 10:30:34');
INSERT INTO `jdroplet_page_pages` VALUES ('26', 'cron', 'jdroplet.app.view.admin.ManageCronPage', '1', '2017-12-26 16:15:04');
INSERT INTO `jdroplet_page_pages` VALUES ('27', 'free', 'jdroplet.app2.view.api.FreePage', '1', '2018-01-02 14:02:57');
INSERT INTO `jdroplet_page_pages` VALUES ('28', 'file', 'jdroplet.app.view.FilePage', '1', '2018-01-14 11:00:28');
INSERT INTO `jdroplet_page_pages` VALUES ('29', 'order', 'jdroplet.app.view.admin.ManageOrderPage', '1', '2018-01-20 18:33:09');
INSERT INTO `jdroplet_page_pages` VALUES ('30', 'shop', 'jdroplet.app.view.admin.ManageShopPage', '1', '2018-01-20 23:33:22');
INSERT INTO `jdroplet_page_pages` VALUES ('31', 'activity', 'jdroplet.app.view.admin.ManageActivityPage', '1', '2018-01-24 16:38:14');
INSERT INTO `jdroplet_page_pages` VALUES ('32', 'book', 'jdroplet.app2.view.BookPage', '1', '2018-02-25 22:56:48');
INSERT INTO `jdroplet_page_pages` VALUES ('33', 'login', 'jdroplet.app.view.LoginPage', '1', '2018-02-06 09:50:42');
INSERT INTO `jdroplet_page_pages` VALUES ('34', 'snsproxy', 'jdroplet.app.view.SnsProxyPage', '1', '2018-02-06 10:06:41');
INSERT INTO `jdroplet_page_pages` VALUES ('35', 'user', 'jdroplet.app2.view.UserPage', '1', '2018-02-06 17:08:26');
INSERT INTO `jdroplet_page_pages` VALUES ('36', 'pay', 'jdroplet.app.view.PayPage', '1', '2018-02-08 15:24:59');
INSERT INTO `jdroplet_page_pages` VALUES ('37', 'order', 'jdroplet.app.view.OrderPage', '1', '2018-02-10 09:58:35');
INSERT INTO `jdroplet_page_pages` VALUES ('38', 'bookhistory', 'jdroplet.app2.view.api.BookHistoryPage', null, '2018-09-18 15:16:48');
INSERT INTO `jdroplet_page_pages` VALUES ('39', 'bill', 'jdroplet.app.view.api.BillPage', '1', '2018-02-13 10:31:45');
INSERT INTO `jdroplet_page_pages` VALUES ('40', 'message', 'jdroplet.app.view.api.MessagePage', '1', '2018-02-23 23:13:27');
INSERT INTO `jdroplet_page_pages` VALUES ('41', 'vote', 'jdroplet.app.view.api.VotePage', '1', '2018-02-26 10:18:48');
INSERT INTO `jdroplet_page_pages` VALUES ('42', 'link', 'jdroplet.app.view.LinkPage', '1', '2018-03-06 11:19:04');
INSERT INTO `jdroplet_page_pages` VALUES ('43', 'panel', 'jdroplet.app.view.PanelPage', '1', '2018-03-07 16:13:55');
INSERT INTO `jdroplet_page_pages` VALUES ('44', 'app', 'jdroplet.app2.view.api.AppPage', '1000001', '2018-03-13 16:41:56');
INSERT INTO `jdroplet_page_pages` VALUES ('45', 'activity', 'jdroplet.app.view.ActivityPage', '1000001', '2018-03-30 11:15:34');
INSERT INTO `jdroplet_page_pages` VALUES ('46', 'invite', 'jdroplet.app.view.api.game.InvitePage', '1000001', '2018-04-08 14:51:42');
INSERT INTO `jdroplet_page_pages` VALUES ('47', 'snsproxy', 'jdroplet.app.view.SnsProxyPage', '1000001', '2018-04-14 21:20:31');
INSERT INTO `jdroplet_page_pages` VALUES ('48', 'lottery', 'jdroplet.app.view.admin.ManageLotteryPage', '1000001', '2018-04-23 15:09:55');
INSERT INTO `jdroplet_page_pages` VALUES ('49', 'collection', 'jdroplet.app.view.api.game.CollectionPage', '1000001', '2018-04-26 20:36:32');
INSERT INTO `jdroplet_page_pages` VALUES ('50', 'draft', 'jdroplet.app.view.api.DraftPage', '1000001', '2018-05-08 01:00:39');
INSERT INTO `jdroplet_page_pages` VALUES ('51', 'message', 'jdroplet.app.view.api.MessagePage', '1000001', '2018-05-13 17:29:12');
INSERT INTO `jdroplet_page_pages` VALUES ('52', 'lotteryuser', 'jdroplet.app.view.admin.ManageLotteryUserPage', '1000001', '2018-05-14 10:53:45');
INSERT INTO `jdroplet_page_pages` VALUES ('53', 'lottery', 'jdroplet.app.view.api.game.LotteryPage', '1000001', '2018-05-25 17:28:52');
INSERT INTO `jdroplet_page_pages` VALUES ('54', 'undueword', 'jdroplet.app.view.api.UndueWordPage', '1000001', '2018-05-26 17:16:07');
INSERT INTO `jdroplet_page_pages` VALUES ('55', 'index', 'jdroplet.app.view.MasterPage', '1000001', '2018-06-02 12:24:52');
INSERT INTO `jdroplet_page_pages` VALUES ('56', 'main', 'jdroplet.yue.view.MainPage', '1000001', '2018-06-04 16:45:07');
INSERT INTO `jdroplet_page_pages` VALUES ('57', 'rain', 'jdroplet.app.view.api.game.RainPage', '1000001', '2018-06-19 10:55:49');
INSERT INTO `jdroplet_page_pages` VALUES ('58', 'book', 'jdroplet.yue.view.BookPage', '1000001', '2018-06-20 11:56:23');
INSERT INTO `jdroplet_page_pages` VALUES ('59', 'link', 'jdroplet.yue.view.LinkPage', '1000001', '2018-06-20 11:57:09');
INSERT INTO `jdroplet_page_pages` VALUES ('60', 'goods', 'jdroplet.app.view.admin.ManageGoodsPage', '1000001', '2018-06-22 12:26:29');
INSERT INTO `jdroplet_page_pages` VALUES ('61', 'main', 'jdroplet.shop.view.MainPage', '1000001', '2018-06-30 10:23:44');
INSERT INTO `jdroplet_page_pages` VALUES ('62', 'item', 'jdroplet.shop.view.ItemPage', '1000001', '2018-06-30 10:41:40');
INSERT INTO `jdroplet_page_pages` VALUES ('63', 'attachment', 'jdroplet.app.view.api.AttachmentPage', '1000001', '2018-07-01 13:52:44');
INSERT INTO `jdroplet_page_pages` VALUES ('64', 'login', 'jdroplet.app.view.LoginPage', '1000001', '2018-07-02 11:53:39');
INSERT INTO `jdroplet_page_pages` VALUES ('65', 'user', 'jdroplet.shop.view.UserPage', '1000001', '2018-07-02 14:44:49');
INSERT INTO `jdroplet_page_pages` VALUES ('66', 'cart', 'jdroplet.shop.view.CartPage', '1000001', '2018-08-04 11:40:31');
INSERT INTO `jdroplet_page_pages` VALUES ('67', 'action', 'jdroplet.app.view.api.ActionPage', '1000001', '2018-07-09 10:46:22');
INSERT INTO `jdroplet_page_pages` VALUES ('68', 'order', 'jdroplet.app.view.OrderPage', '1000001', '2018-07-10 16:57:51');
INSERT INTO `jdroplet_page_pages` VALUES ('69', 'pay', 'jdroplet.app.view.PayPage', '1000001', '2018-07-10 17:03:15');
INSERT INTO `jdroplet_page_pages` VALUES ('70', 'snsproxy', 'jdroplet.app.view.SnsProxyPage', '1000001', '2018-07-16 10:58:39');
INSERT INTO `jdroplet_page_pages` VALUES ('71', 'goods', 'jdroplet.app.view.api.GoodsPage', '1000001', '2018-07-23 15:32:18');
INSERT INTO `jdroplet_page_pages` VALUES ('72', 'cart', 'jdroplet.app.view.api.CartPage', '1000001', '2018-08-20 09:07:56');
INSERT INTO `jdroplet_page_pages` VALUES ('73', 'stat', 'jdroplet.app.view.api.StatisticPage', '1000001', '2018-08-22 15:13:12');
INSERT INTO `jdroplet_page_pages` VALUES ('74', 'debug', 'jdroplet.app2.view.DebugPage', '1000001', '2018-07-31 21:51:30');
INSERT INTO `jdroplet_page_pages` VALUES ('75', 'error', 'jdroplet.app.view.ErrorPage', '1000001', '2018-08-08 10:42:17');
INSERT INTO `jdroplet_page_pages` VALUES ('76', 'qrcode', 'jdroplet.app.view.QRCodePage', '1000001', '2018-08-17 16:13:13');
INSERT INTO `jdroplet_page_pages` VALUES ('77', 'visit', 'jdroplet.app.view.api.VisitPage', '1000001', '2018-08-22 09:50:44');
INSERT INTO `jdroplet_page_pages` VALUES ('78', 'post', 'jdroplet.info.view.PostPage', '1000001', '2018-08-28 15:58:29');
INSERT INTO `jdroplet_page_pages` VALUES ('79', 'group', 'jdroplet.app.view.api.GroupPage', '1000001', '2018-09-06 09:33:21');
INSERT INTO `jdroplet_page_pages` VALUES ('80', 'main', 'jdroplet.app.view.MasterPage', '1000001', '2018-09-09 13:22:25');
INSERT INTO `jdroplet_page_pages` VALUES ('81', 'post', 'jdroplet.app.view.MasterPage', '1000001', '2018-09-10 09:42:48');

-- ----------------------------
-- Table structure for jdroplet_page_urlpatterns
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_page_urlpatterns`;
CREATE TABLE `jdroplet_page_urlpatterns` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cluster_id` int(10) unsigned NOT NULL DEFAULT '1',
  `page_id` int(10) unsigned NOT NULL DEFAULT '1',
  `action` varchar(32) NOT NULL,
  `type` int(10) unsigned NOT NULL DEFAULT '0',
  `params` varchar(256) DEFAULT '',
  `allows` varchar(1024) DEFAULT '',
  `description` varchar(512) DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `FK_jdroplet_page_urlpatterns_page` (`page_id`),
  KEY `FK_jdroplet_page_urlpatterns_method` (`action`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=242 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_page_urlpatterns
-- ----------------------------
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('1', '1', '1', 'show', '0', '', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('2', '1', '2', 'show', '0', '', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('3', '1', '3', 'show', '0', '', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('4', '1', '4', 'show', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('5', '1', '4', 'recv', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('6', '1', '5', 'show', '0', '', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('7', '1', '6', 'show', '0', 'page_index', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('8', '1', '6', 'edit', '0', 'id', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('9', '1', '6', 'recv', '0', 'id', 'Administrators,Moderators', 'OAuth Edit Page');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('10', '1', '6', 'delete', '0', 'id', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('11', '1', '7', 'show', '0', 'page_index', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('12', '1', '7', 'edit', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('13', '1', '7', 'recv', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('14', '1', '8', 'list', '0', 'clusterId,pageIndex', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('15', '1', '8', 'edit', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('16', '1', '8', 'recv', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('17', '2', '9', 'oauth', '0', 'name,shopId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('18', '2', '9', 'oauth_callback', '0', 'name,shopId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('19', '2', '10', 'list', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('20', '2', '11', 'list', '0', 'sectionId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('21', '2', '10', 'save', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('22', '2', '10', 'get', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('23', '2', '11', 'get', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('24', '2', '12', 'search', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('25', '2', '11', 'save', '0', '', 'Users,Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('26', '2', '13', 'save', '0', 'id', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('27', '2', '13', 'list', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('28', '2', '14', 'create', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('29', '2', '15', 'create', '0', 'module', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('30', '2', '15', 'feedback', '0', 'module', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('31', '2', '11', 'delete', '0', 'id', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('32', '2', '16', 'recv', '0', '', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('33', '2', '12', 'create', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('34', '2', '17', 'save', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('35', '2', '11', 'reset', '0', 'fields', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('36', '2', '12', 'list', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('37', '2', '12', 'reset', '0', 'fields', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('38', '2', '9', 'wechatsubscribe', '0', 'shopId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('39', '2', '4', 'recv', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('40', '2', '12', 'delete', '0', 'id', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('41', '2', '9', 'wechat', '0', 'type,id', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('42', '1', '18', 'list', '0', 'clusterId,pageId', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('43', '1', '19', 'list', '0', 'pageIndex', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('44', '1', '19', 'edit', '0', 'id', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('45', '1', '19', 'delete', '0', 'id', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('46', '1', '19', 'recv', '0', 'id', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('47', '1', '20', 'list', '0', 'id,pageIndex', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('48', '1', '20', 'edit', '0', 'clusterId,id', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('49', '1', '20', 'recv', '0', 'clusterId,id', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('50', '1', '18', 'edit', '0', 'id', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('51', '1', '18', 'recv', '0', 'id', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('52', '1', '18', 'delete', '0', 'id', 'Administrators', 'ttt');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('53', '2', '21', 'get', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('54', '2', '22', 'get', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('55', '2', '21', 'list', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('56', '2', '22', 'list', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('57', '2', '21', 'save', '0', '', 'Users,Administrators,Moderators,Editors,运营员,运营员', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('58', '2', '22', 'save', '0', '', 'Administrators,Editors', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('59', '1', '23', 'list', '0', 'pageIndex', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('60', '1', '23', 'edit', '0', 'id', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('61', '1', '23', 'recv', '0', '', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('62', '1', '24', 'list', '0', 'pageIndex', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('63', '1', '24', 'edit', '0', 'id', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('64', '1', '24', 'recv', '0', '', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('65', '2', '22', 'reset', '0', 'fields', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('66', '2', '25', 'menu', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('67', '1', '26', 'list', '0', 'pageIndex', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('68', '1', '26', 'edit', '0', 'id', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('69', '1', '26', 'recv', '0', 'id', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('70', '1', '26', 'delete', '0', 'id', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('71', '2', '27', 'list', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('72', '2', '27', 'save', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('73', '2', '25', 'confirmOrder', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('74', '2', '25', 'orders', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('75', '2', '21', 'join', '0', 'types', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('76', '2', '25', 'order', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('77', '1', '28', 'recv', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('78', '2', '14', 'list', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('79', '1', '29', 'list', '0', 'pageIndex', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('80', '1', '30', 'list', '0', 'pageIndex', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('81', '1', '30', 'edit', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('82', '1', '30', 'recv', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('83', '1', '7', 'list', '0', 'pageIndex', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('84', '1', '31', 'list', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('85', '1', '31', 'edit', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('86', '1', '31', 'recv', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('87', '2', '12', 'get', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('88', '3', '32', 'show', '0', 'id', 'Everyone', '作品页');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('89', '3', '32', 'rank', '0', '', 'Everyone', '排行榜');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('90', '3', '32', 'free', '0', '', 'Everyone', '免费页');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('91', '3', '32', 'chapter', '0', 'id,chapterId', 'Everyone', '作品内容页');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('92', '3', '32', 'comments', '0', 'id', 'Everyone', '评论页');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('93', '3', '32', 'catalog', '0', 'id,pageIndex', 'Everyone', '作品目录页');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('94', '3', '32', 'index', '0', '', 'Everyone', '站点首页');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('95', '3', '32', 'charge', '0', 'id,chapterId', 'Everyone', '提示充值页面');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('96', '3', '33', 'show', '0', '', 'Everyone', '登录页');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('97', '3', '34', 'oauth', '0', 'name,shopId', 'Everyone', 'oauth登录');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('98', '3', '34', 'oauth_callback', '0', 'name,shopId', 'Everyone', 'oauth回调');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('99', '3', '35', 'my', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('100', '3', '36', 'create', '0', 'shopId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('101', '3', '36', 'feedback', '0', 'module,shopId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('102', '3', '36', 'success', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('103', '3', '36', 'fail', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('104', '3', '37', 'create', '0', 'shopId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('105', '3', '35', 'recharge', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('106', '2', '38', 'list', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('107', '2', '39', 'list', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('108', '3', '35', 'reset', '0', 'fields', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('109', '3', '35', 'edit', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('110', '3', '35', 'record', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('111', '3', '35', 'bookmark', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('112', '3', '35', 'msg', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('113', '2', '38', 'add', '0', 'bookId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('114', '3', '32', 'list', '0', 'pageIndex', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('115', '3', '32', 'tags', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('116', '3', '33', 'logout', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('117', '2', '38', 'count', '0', 'bookId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('118', '3', '35', 'books', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('119', '3', '35', 'write', '0', 'bookId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('120', '2', '40', 'list', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('121', '2', '40', 'reset', '0', 'fields', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('122', '2', '40', 'unreads', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('123', '2', '41', 'add', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('124', '3', '32', 'pubcomment', '0', 'id', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('125', '3', '36', 'callback', '0', 'module,shopId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('126', '3', '42', 'show', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('127', '3', '32', 'simple', '0', 'id,chapterId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('128', '3', '43', 'show', '0', 'shopId,name', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('129', '2', '44', 'data', '0', '', 'Users,Administrators,Moderators,Editors', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('130', '3', '35', 'admin', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('131', '4', '45', 'invite', '0', 'shopId,activityId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('132', '2', '14', 'save', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('133', '2', '22', 'injection', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('134', '2', '21', 'reset', '0', 'fields', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('135', '4', '46', 'get', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('136', '4', '46', 'create', '0', 'activityId', 'Users,Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('137', '4', '46', 'join', '0', 'groupId', 'Users,Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('138', '4', '46', 'group', '0', 'groupId', 'Users,Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('139', '4', '47', 'oauth', '0', 'name,shopId', 'Everyone', 'oauth登录');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('140', '4', '47', 'oauth_callback', '0', 'name,shopId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('141', '4', '46', 'rank', '0', 'activityId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('142', '4', '46', 'save', '0', 'activityId,groupId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('143', '4', '45', 'turntable', '0', 'shopId,activityId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('144', '4', '45', 'collection', '0', 'shopId,activityId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('145', '1', '48', 'list', '0', 'activityId', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('146', '1', '48', 'edit', '0', 'id', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('147', '1', '48', 'recv', '0', 'id', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('148', '4', '49', 'get', '0', 'activityId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('149', '4', '49', 'lottery', '0', 'activityId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('150', '4', '49', 'transfer', '0', 'activityId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('151', '4', '47', 'wxshare', '0', 'shopId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('152', '2', '12', 'save', '0', '', 'Users,Administrators,Moderators,Editors', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('153', '2', '50', 'list', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('154', '2', '50', 'save', '0', '', 'Users,Administrators,Moderators,Editors', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('155', '2', '50', 'get', '0', 'id', 'Users,Administrators,Moderators,Editors', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('156', '4', '49', 'mix', '0', 'shopId,activityId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('157', '4', '51', 'list', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('158', '1', '52', 'list', '0', 'activityId,pageIndex', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('159', '2', '50', 'delete', '0', 'id', 'Administrators,Moderators,Editors', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('160', '2', '12', 'check', '0', 'field', 'Users,Administrators,Moderators,Editors', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('161', '4', '45', 'pray', '0', 'shopId,activityId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('162', '4', '53', 'get', '0', 'shopId,activityId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('163', '2', '54', 'check', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('164', '4', '53', 'save', '0', 'activityId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('165', '4', '45', 'lode', '0', 'shopId,activityId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('166', '4', '45', 'rain', '0', 'shopId,activityId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('167', '4', '55', 'show', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('168', '5', '56', 'index', '0', 'pageIndex', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('169', '5', '56', 'list', '0', 'pageIndex', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('170', '5', '56', 'show', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('171', '5', '56', 'catalog', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('172', '4', '57', 'get', '0', 'activityId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('173', '4', '57', 'start', '0', 'activityId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('174', '4', '57', 'check', '0', 'activityId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('175', '5', '58', 'list', '0', 'pageIndex', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('176', '5', '59', 'list', '0', 'pageIndex', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('177', '5', '58', 'show', '0', 'id,oid', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('178', '5', '58', 'catalog', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('179', '5', '59', 'show', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('180', '1', '60', 'list', '0', 'pageIndex', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('181', '3', '32', 'batch', '0', 'bookId,chapterId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('182', '1', '60', 'edit', '0', 'id', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('183', '6', '61', 'index', '0', 'shopId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('184', '6', '62', 'show', '0', 'shopId,id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('185', '2', '63', 'list', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('186', '6', '64', 'show', '0', 'shopId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('187', '6', '64', 'recv', '0', 'shopId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('188', '6', '65', 'my', '0', 'shopId', 'Users,Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('189', '1', '30', 'main', '0', 'id', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('190', '6', '62', 'list', '0', 'shopId,pageIndex', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('191', '6', '66', 'show', '0', 'shopId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('192', '6', '66', 'add', '0', 'shopId,id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('193', '6', '66', 'checkout', '0', 'shopId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('194', '2', '67', 'invoke', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('195', '6', '68', 'create', '0', 'shopId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('196', '6', '69', 'create', '0', 'shopId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('197', '6', '69', 'feedback', '0', 'module,shopId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('198', '6', '69', 'success', '0', 'shopId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('199', '6', '69', 'fail', '0', 'shopId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('200', '6', '69', 'callback', '0', 'module,shopId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('201', '2', '17', 'list', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('202', '6', '65', 'contacts', '0', 'shopId', 'Users,Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('203', '6', '65', 'orders', '0', 'shopId,pageIndex', 'Users,Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('204', '2', '17', 'remove', '0', '', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('205', '6', '70', 'oauth', '0', 'name,shopId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('206', '6', '70', 'oauth_callback', '0', 'name,shopId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('207', '1', '7', 'my', '0', '', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('208', '6', '66', 'remove', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('209', '2', '71', 'list', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('210', '2', '71', 'get', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('211', '2', '13', 'reset', '0', 'fields', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('212', '2', '13', 'get', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('213', '2', '72', 'create', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('214', '2', '73', 'list', '0', '', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('215', '2', '25', 'get', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('216', '3', '74', 'show', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('217', '1', '29', 'show', '0', 'orderId', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('218', '2', '14', 'reset', '0', 'fields', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('219', '6', '64', 'logout', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('220', '6', '65', 'profile', '0', 'shopId', 'Users,Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('221', '6', '65', 'order', '0', 'shopId,orderId', 'Administrators,Moderators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('222', '6', '69', 'show', '0', 'shopId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('223', '6', '68', 'cancel', '0', 'shopId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('224', '6', '75', 'show', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('225', '6', '76', 'show', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('226', '6', '65', 'editcontact', '0', 'shopId,id', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('227', '2', '14', 'count', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('228', '2', '72', 'list', '0', 'shopId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('229', '2', '72', 'add', '0', 'shopId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('230', '2', '77', 'recv', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('231', '1', '23', 'atts', '0', 'id', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('232', '1', '23', 'att', '0', 'id', 'Administrators', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('233', '7', '78', 'publish', '0', 'cityId,sectionId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('234', '7', '78', 'select', '0', 'cityId', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('235', '7', '78', 'show', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('236', '2', '79', 'list', '0', '', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('237', '7', '78', 'list', '0', 'slug', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('238', '8', '80', 'index', '0', '', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('239', '8', '81', 'show', '0', 'id', 'Everyone', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('240', '2', '72', 'clean', '0', 'shopId', 'Users', '');
INSERT INTO `jdroplet_page_urlpatterns` VALUES ('241', '2', '72', 'remove', '0', '', 'Users', '');

-- ----------------------------
-- Table structure for jdroplet_postmeta
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_postmeta`;
CREATE TABLE `jdroplet_postmeta` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(10) unsigned NOT NULL,
  `meta_key` varchar(255) NOT NULL,
  `meta_value` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_postmeta
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_posts
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_posts`;
CREATE TABLE `jdroplet_posts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shop_id` int(10) unsigned NOT NULL DEFAULT '1',
  `parent_id` int(10) unsigned DEFAULT '0',
  `user_id` int(10) unsigned NOT NULL,
  `editor` varchar(64) DEFAULT '',
  `item_id` int(10) unsigned NOT NULL DEFAULT '0',
  `title` varchar(256) DEFAULT NULL,
  `content` longtext,
  `excerpt` varchar(8192) DEFAULT NULL,
  `url` varchar(1024) DEFAULT NULL,
  `icon` varchar(1024) DEFAULT NULL,
  `tags` varchar(1024) DEFAULT NULL,
  `type` varchar(32) DEFAULT 'post',
  `views` int(10) unsigned DEFAULT '0',
  `likes` int(10) unsigned DEFAULT '0',
  `valuables` int(10) unsigned DEFAULT NULL,
  `votes` int(11) DEFAULT '0',
  `price` decimal(10,2) DEFAULT NULL,
  `post_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` int(11) NOT NULL DEFAULT '0',
  `city_id` int(10) unsigned DEFAULT '0',
  `phone` varchar(16) DEFAULT '',
  `section_id` int(10) unsigned DEFAULT '0',
  `address` varchar(512) DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `FK_jdroplet_posts_sectionId` (`parent_id`),
  KEY `FK_jdroplet_posts_userId` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_posts
-- ----------------------------
INSERT INTO `jdroplet_posts` VALUES ('1', '1', null, '1000001', '', '3', '', '', '', '', '/upload/attas/2018/9/2d44a6e12a5c0f151220da0a0f06cbed_1000001.png', '', 'shop_main_carousel', '0', '0', '0', '0', null, '2018-09-17 15:20:25', '2018-09-17 15:20:25', '1', null, '', null, '');
INSERT INTO `jdroplet_posts` VALUES ('2', '1', null, '1000001', '', '2', '', '', '', '', '/upload/attas/2018/9/c610eecc479d6e1b315801aebe14da6f_1000001.png', '', 'shop_main_carousel', '0', '0', '0', '0', null, '2018-09-17 15:20:25', '2018-09-17 15:20:25', '1', null, '', null, '');
INSERT INTO `jdroplet_posts` VALUES ('3', '1', null, '1000001', '', '1', '', '', '', '', '/upload/attas/2018/9/9c32559de5de887387acfb33439b4b43_1000001.png', '', 'shop_main_carousel', '0', '0', '0', '0', null, '2018-09-17 15:20:26', '2018-09-17 15:20:26', '1', null, '', null, '');
INSERT INTO `jdroplet_posts` VALUES ('4', '1', null, '1000001', '', '1', '今日推荐A', '', '今日推荐A', '', '/upload/attas/2018/9/9c32559de5de887387acfb33439b4b43_1000001.png', '', 'shop_main_recommend', '0', '0', '0', '0', '55.00', '2018-09-17 15:20:26', '2018-09-17 15:20:26', '1', null, '', null, '');
INSERT INTO `jdroplet_posts` VALUES ('5', '1', null, '1000001', '', '2', '今日推荐B', '', '今日推荐B', '', '/upload/attas/2018/9/c610eecc479d6e1b315801aebe14da6f_1000001.png', '', 'shop_main_recommend', '0', '0', '0', '0', '44.00', '2018-09-17 15:20:26', '2018-09-17 15:20:26', '1', null, '', null, '');
INSERT INTO `jdroplet_posts` VALUES ('6', '1', null, '1000001', '', '3', '今日推荐C', '', '今日推荐C', '', '/upload/attas/2018/9/55aa21a59c72444fa57672065f4aebac_1000001.png', '', 'shop_main_recommend', '0', '0', '0', '0', '33.00', '2018-09-17 15:20:26', '2018-09-17 15:20:26', '1', null, '', null, '');
INSERT INTO `jdroplet_posts` VALUES ('7', '1', '0', '1000001', null, '0', '双11期间电脑办公用品全场低至5折！', '', '', 'https://test.jdroplet.com/mshop/item/show/1/1.shtml', '', '', 'news', '0', '0', null, '0', '0.00', '2018-09-18 15:32:37', '2018-09-18 15:32:37', '1', null, null, null, null);

-- ----------------------------
-- Table structure for jdroplet_rolemeta
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_rolemeta`;
CREATE TABLE `jdroplet_rolemeta` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(10) unsigned NOT NULL,
  `meta_key` varchar(255) NOT NULL,
  `meta_value` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_rolemeta
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_roles
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_roles`;
CREATE TABLE `jdroplet_roles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `description` varchar(256) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_roles
-- ----------------------------
INSERT INTO `jdroplet_roles` VALUES ('1', 'Everyone', '所有人');
INSERT INTO `jdroplet_roles` VALUES ('2', 'Users', '普通用户组');
INSERT INTO `jdroplet_roles` VALUES ('3', 'Administrators', '系统管理员');
INSERT INTO `jdroplet_roles` VALUES ('4', 'Moderators', '管理员');

-- ----------------------------
-- Table structure for jdroplet_role_relationships
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_role_relationships`;
CREATE TABLE `jdroplet_role_relationships` (
  `role_id` int(10) unsigned NOT NULL DEFAULT '1',
  `user_id` int(10) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`role_id`,`user_id`),
  KEY `Index_roleId` (`role_id`),
  KEY `Index_userId` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_role_relationships
-- ----------------------------
INSERT INTO `jdroplet_role_relationships` VALUES ('2', '1000001');
INSERT INTO `jdroplet_role_relationships` VALUES ('2', '1000002');
INSERT INTO `jdroplet_role_relationships` VALUES ('3', '1000001');
INSERT INTO `jdroplet_role_relationships` VALUES ('4', '1000001');
INSERT INTO `jdroplet_role_relationships` VALUES ('5', '1000001');
INSERT INTO `jdroplet_role_relationships` VALUES ('12', '1000001');

-- ----------------------------
-- Table structure for jdroplet_sectionmeta
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_sectionmeta`;
CREATE TABLE `jdroplet_sectionmeta` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(10) unsigned NOT NULL,
  `meta_key` varchar(255) NOT NULL,
  `meta_value` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_sectionmeta
-- ----------------------------
INSERT INTO `jdroplet_sectionmeta` VALUES ('1', '720', 'content_title', '内容2');
INSERT INTO `jdroplet_sectionmeta` VALUES ('2', '720', 'title_title', '标题2');
INSERT INTO `jdroplet_sectionmeta` VALUES ('3', '720', 'price_title', '租金');
INSERT INTO `jdroplet_sectionmeta` VALUES ('4', '720', 'price_postfix', '元/月');

-- ----------------------------
-- Table structure for jdroplet_sectionpermissions
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_sectionpermissions`;
CREATE TABLE `jdroplet_sectionpermissions` (
  `cluster_id` int(10) unsigned NOT NULL,
  `section_id` int(10) unsigned NOT NULL,
  `role_id` int(10) unsigned NOT NULL,
  `allow_mask` int(10) DEFAULT '0',
  `deny_mask` int(10) DEFAULT '0',
  PRIMARY KEY (`cluster_id`,`section_id`,`role_id`),
  KEY `FK_jdroplet_sectionpermissions_2` (`section_id`),
  KEY `FK_jdroplet_sectionpermissions_3` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_sectionpermissions
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_sections
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_sections`;
CREATE TABLE `jdroplet_sections` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shop_id` int(10) unsigned NOT NULL DEFAULT '1',
  `parent_id` int(10) unsigned DEFAULT '0',
  `name` varchar(128) NOT NULL DEFAULT '',
  `slug` varchar(256) DEFAULT '',
  `keywords` varchar(128) DEFAULT NULL,
  `description` varchar(8192) DEFAULT '',
  `user_id` bigint(11) DEFAULT '0',
  `count` int(11) unsigned DEFAULT '0',
  `icon` varchar(512) DEFAULT '',
  `attributes` varchar(512) DEFAULT '',
  `url` varchar(1024) DEFAULT '',
  `type` varchar(64) DEFAULT '',
  `date_created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `sort_order` int(2) unsigned DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_jdroplet_sections_cluster` (`shop_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_sections
-- ----------------------------
INSERT INTO `jdroplet_sections` VALUES ('1', '1', '0', '实验技术服务', 'shiyanjishufuwu', null, '', '1000001', null, '', '', '', 'cat', '2018-09-09 23:04:56', null);
INSERT INTO `jdroplet_sections` VALUES ('2', '1', '0', '高通量测序服务', 'gaotongliangcexufuwu', null, '', '1000001', null, '', '', '', 'cat', '2018-09-09 23:05:54', null);
INSERT INTO `jdroplet_sections` VALUES ('3', '1', '0', '生物美学', 'shengwumeixue', null, '', '1000001', null, '', '', '', 'cat', '2018-09-09 23:06:17', null);
INSERT INTO `jdroplet_sections` VALUES ('4', '1', '0', '顶部轮播', 'dingbulunbo', null, '', '1000001', null, '', '', '', 'swiper', '2018-09-10 09:56:00', null);
INSERT INTO `jdroplet_sections` VALUES ('5', '1', '0', '颜色', 'yanse', null, '', null, null, '', '', '', 'goods_property_name', '2018-09-10 17:35:29', null);
INSERT INTO `jdroplet_sections` VALUES ('6', '1', '0', '容量', 'rongliang', null, '', null, null, '', '', '', 'goods_property_name', '2018-09-10 17:35:29', null);
INSERT INTO `jdroplet_sections` VALUES ('7', '1', '5', '粉色', 'fense', null, '', null, null, '', '', '', 'goods_property_value', '2018-09-10 17:35:30', null);
INSERT INTO `jdroplet_sections` VALUES ('8', '1', '6', '64G', '64G', null, '', null, null, '', '', '', 'goods_property_value', '2018-09-10 17:35:30', null);
INSERT INTO `jdroplet_sections` VALUES ('9', '1', '6', '128G', '128G', null, '', null, null, '', '', '', 'goods_property_value', '2018-09-10 17:35:30', null);
INSERT INTO `jdroplet_sections` VALUES ('10', '1', '5', '黑色', 'heise', null, '', null, null, '', '', '', 'goods_property_value', '2018-09-10 17:35:30', null);

-- ----------------------------
-- Table structure for jdroplet_section_objects
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_section_objects`;
CREATE TABLE `jdroplet_section_objects` (
  `object_id` int(10) unsigned NOT NULL,
  `section_id` int(10) unsigned NOT NULL,
  `type` varchar(32) NOT NULL,
  PRIMARY KEY (`object_id`,`section_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_section_objects
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_shopmeta
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_shopmeta`;
CREATE TABLE `jdroplet_shopmeta` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(10) unsigned NOT NULL,
  `meta_key` varchar(255) NOT NULL,
  `meta_value` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_shopmeta
-- ----------------------------
 
-- ----------------------------
-- Table structure for jdroplet_shops
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_shops`;
CREATE TABLE `jdroplet_shops` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL COMMENT '商店所属id',
  `name` varchar(64) DEFAULT '' COMMENT '商店名称',
  `description` varchar(4096) DEFAULT '' COMMENT '简单描述',
  `icon` varchar(1024) DEFAULT '' COMMENT '商店logo',
  `type` varchar(64) DEFAULT 'normal' COMMENT '店铺类型',
  `phone` varchar(16) DEFAULT NULL,
  `address` varchar(1024) DEFAULT NULL,
  `status` int(10) unsigned DEFAULT '1' COMMENT '状态0屏蔽，1显示',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '商店创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_shops
-- ----------------------------
INSERT INTO `jdroplet_shops` VALUES ('1', '1000001', 'jDroplet商城', 'jDroplet商城', '/upload/ocean/2018/1/4ed41d293c04875e882cc1bc1bc993d2_1.png', 'mall', '000', '滨海', '0', '2018-09-17 17:33:25');

-- ----------------------------
-- Table structure for jdroplet_shop_atts
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_shop_atts`;
CREATE TABLE `jdroplet_shop_atts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_shop_atts
-- ----------------------------
INSERT INTO `jdroplet_shop_atts` VALUES ('1', '皮革');
INSERT INTO `jdroplet_shop_atts` VALUES ('2', '真皮');
INSERT INTO `jdroplet_shop_atts` VALUES ('3', '米色');
INSERT INTO `jdroplet_shop_atts` VALUES ('4', '黑色');
INSERT INTO `jdroplet_shop_atts` VALUES ('5', '蓝色');

-- ----------------------------
-- Table structure for jdroplet_shop_skus
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_shop_skus`;
CREATE TABLE `jdroplet_shop_skus` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(10) unsigned NOT NULL,
  `name` varchar(64) NOT NULL,
  `price` decimal(10,2) unsigned DEFAULT NULL,
  `count` int(10) unsigned DEFAULT '0',
  `status` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_shop_skus
-- ----------------------------
  
-- ----------------------------
-- Table structure for jdroplet_shop_sku_atts
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_shop_sku_atts`;
CREATE TABLE `jdroplet_shop_sku_atts` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `product_id` int(10) unsigned NOT NULL DEFAULT '0',
  `sku_id` int(10) unsigned NOT NULL,
  `att_id` int(10) unsigned NOT NULL,
  `section_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_shop_sku_atts
-- ----------------------------
INSERT INTO `jdroplet_shop_sku_atts` VALUES ('1', '2', '1', '1', '1');
INSERT INTO `jdroplet_shop_sku_atts` VALUES ('2', '2', '1', '3', '2');
INSERT INTO `jdroplet_shop_sku_atts` VALUES ('3', '2', '2', '2', '1');
INSERT INTO `jdroplet_shop_sku_atts` VALUES ('4', '2', '2', '3', '2');

-- ----------------------------
-- Table structure for jdroplet_sitemappings
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_sitemappings`;
CREATE TABLE `jdroplet_sitemappings` (
  `site_id` bigint(10) unsigned NOT NULL,
  `cluster_id` bigint(10) unsigned NOT NULL,
  PRIMARY KEY (`site_id`,`cluster_id`),
  KEY `FK_jdroplet_sitemappings_2` (`cluster_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_sitemappings
-- ----------------------------
INSERT INTO `jdroplet_sitemappings` VALUES ('1', '1');
INSERT INTO `jdroplet_sitemappings` VALUES ('8', '1');
INSERT INTO `jdroplet_sitemappings` VALUES ('24', '1');
INSERT INTO `jdroplet_sitemappings` VALUES ('25', '1');
INSERT INTO `jdroplet_sitemappings` VALUES ('2', '2');
INSERT INTO `jdroplet_sitemappings` VALUES ('3', '2');
INSERT INTO `jdroplet_sitemappings` VALUES ('7', '2');
INSERT INTO `jdroplet_sitemappings` VALUES ('9', '2');
INSERT INTO `jdroplet_sitemappings` VALUES ('18', '2');
INSERT INTO `jdroplet_sitemappings` VALUES ('19', '2');
INSERT INTO `jdroplet_sitemappings` VALUES ('20', '2');
INSERT INTO `jdroplet_sitemappings` VALUES ('4', '3');
INSERT INTO `jdroplet_sitemappings` VALUES ('5', '3');
INSERT INTO `jdroplet_sitemappings` VALUES ('6', '4');
INSERT INTO `jdroplet_sitemappings` VALUES ('10', '4');
INSERT INTO `jdroplet_sitemappings` VALUES ('11', '5');
INSERT INTO `jdroplet_sitemappings` VALUES ('12', '6');
INSERT INTO `jdroplet_sitemappings` VALUES ('22', '6');
INSERT INTO `jdroplet_sitemappings` VALUES ('23', '6');
INSERT INTO `jdroplet_sitemappings` VALUES ('21', '7');
INSERT INTO `jdroplet_sitemappings` VALUES ('26', '8');

-- ----------------------------
-- Table structure for jdroplet_sitemeta
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_sitemeta`;
CREATE TABLE `jdroplet_sitemeta` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(10) unsigned DEFAULT NULL,
  `meta_key` varchar(255) DEFAULT NULL,
  `meta_value` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_sitemeta
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_sites
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_sites`;
CREATE TABLE `jdroplet_sites` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `app_root` varchar(128) NOT NULL,
  `title` varchar(64) NOT NULL,
  `description` varchar(512) DEFAULT '',
  `keywords` varchar(512) DEFAULT '',
  `date_created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` bigint(10) unsigned DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_jdroplet_sites_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_sites
-- ----------------------------
INSERT INTO `jdroplet_sites` VALUES ('1', 'admin.jdroplet.com/ocean', 'ocean', 'JDroplet Ocean', '', '2017-06-20 10:08:23', '1');
INSERT INTO `jdroplet_sites` VALUES ('2', 'test.jdroplet.com/ebooks', 'ebooks', 'JDroplet Open', '', '2017-09-08 15:32:37', '1');
INSERT INTO `jdroplet_sites` VALUES ('3', 'api.jdroplet.com/ocean', 'ocean', 'JDroplet Open', '', '2017-09-13 23:54:32', '1');
INSERT INTO `jdroplet_sites` VALUES ('4', 'test.jdroplet.com/read', 'read', 'read', '', '2018-01-26 16:17:57', '1');
INSERT INTO `jdroplet_sites` VALUES ('5', 'www.jdroplet.com/read', 'read', 'read', '', '2018-02-08 17:58:23', '1');
INSERT INTO `jdroplet_sites` VALUES ('6', 'test.jdroplet.com/game', 'game', 'HM', '', '2018-03-30 10:51:24', '1');
INSERT INTO `jdroplet_sites` VALUES ('7', 'dev.jdroplet.com/ebooks', 'ebooks', 'HM', '', '2018-04-03 00:34:36', '0');
INSERT INTO `jdroplet_sites` VALUES ('8', 'admin.jdroplet.com/ocean', 'ocean', 'HM', '', '2018-04-08 11:19:03', '0');
INSERT INTO `jdroplet_sites` VALUES ('9', 'api.jdroplet.com/ebooks', 'api', 'api', '', '2018-05-26 07:42:17', '0');
INSERT INTO `jdroplet_sites` VALUES ('10', 'www.haimao.tech/hm', 'hm', 'HM', '', '2018-06-02 12:19:30', '1');
INSERT INTO `jdroplet_sites` VALUES ('11', 'www.outwiki.com/yue', 'yue', 'Blog', '', '2018-06-04 16:01:18', '1');
INSERT INTO `jdroplet_sites` VALUES ('12', 'test.jdroplet.com/shop', 'shop', 'Shop', '', '2018-06-30 10:21:32', '0');
INSERT INTO `jdroplet_sites` VALUES ('18', 'admin.jdroplet.com/api', 'api', 'api', '', '2018-07-01 16:18:01', '1000001');
INSERT INTO `jdroplet_sites` VALUES ('19', 'test.jdroplet.com/mall', 'mall', 'mall', '', '2018-07-23 10:21:05', '1000001');
INSERT INTO `jdroplet_sites` VALUES ('20', 'www.outwiki.com/mall', 'mall', 'mall', '', '2018-07-23 10:32:53', '1000001');
INSERT INTO `jdroplet_sites` VALUES ('21', 'beijing.jdroplet.com/info', '北京分类信息', '北京分类信息', '北京分类信息', '2018-07-23 10:45:31', '1000001');
INSERT INTO `jdroplet_sites` VALUES ('22', 'test.jdroplet.com/mshopx', 'mshopx', 'mshopx', '', '2018-08-17 17:24:17', '1000001');
INSERT INTO `jdroplet_sites` VALUES ('23', 'test.jdroplet.com/mshop', 'mshop', 'mshop', '', '2018-08-17 17:24:34', '1000001');
INSERT INTO `jdroplet_sites` VALUES ('24', 'admin.jdroplet.com/ocean', 'JDroplet Ocean2', 'JDroplet Ocean2', 'ewr', '2018-09-04 15:30:33', '1000001');
INSERT INTO `jdroplet_sites` VALUES ('25', 'admin.jdroplet.com/ocean', 'JDroplet Ocean', 'JDroplet Ocean', '', '2018-09-04 15:30:50', '1000001');
INSERT INTO `jdroplet_sites` VALUES ('26', 'www.jdroplet.com/bio', 'bio', 'bio', '', '2018-09-09 13:20:13', '0');

-- ----------------------------
-- Table structure for jdroplet_statistics
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_statistics`;
CREATE TABLE `jdroplet_statistics` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int(10) unsigned NOT NULL,
  `name` varchar(64) NOT NULL,
  `data` text NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_statistics
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_usermeta
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_usermeta`;
CREATE TABLE `jdroplet_usermeta` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(10) unsigned NOT NULL,
  `meta_key` varchar(255) NOT NULL,
  `meta_value` longtext,
  PRIMARY KEY (`id`),
  KEY `item_meta_key` (`item_id`,`meta_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_usermeta
-- ----------------------------

-- ----------------------------
-- Table structure for jdroplet_users
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_users`;
CREATE TABLE `jdroplet_users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `display_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `gender` int(10) unsigned DEFAULT NULL,
  `avatar` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `phone` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `password` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `salt` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `email` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `password_format` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `registered` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `lastvisit` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `come_from` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inviter` int(10) unsigned DEFAULT '0',
  `status` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000003 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of jdroplet_users
-- ----------------------------
INSERT INTO `jdroplet_users` VALUES ('1000001', 'guest@jdroplet', 'gongchandang', '0', '', '', 'abe72ed0ceb7de4910ff31b5251b03b1', 'MTI0Mw==', '210@qq.com', 'MD5HASH', '2017-07-17 02:03:41', '2018-05-07 22:54:38', null, '0', '0');

-- ----------------------------
-- Table structure for jdroplet_visits
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_visits`;
CREATE TABLE `jdroplet_visits` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user` varchar(64) DEFAULT NULL,
  `ip` varchar(64) DEFAULT NULL,
  `referer` varchar(1024) DEFAULT NULL,
  `browser` varchar(64) DEFAULT NULL,
  `platform` varchar(32) DEFAULT NULL,
  `region` varchar(32) CHARACTER SET utf8 DEFAULT '',
  `city` varchar(32) DEFAULT '',
  `isp` varchar(32) DEFAULT NULL,
  `resource` varchar(1024) DEFAULT NULL,
  `user_agent` varchar(512) DEFAULT NULL,
  `resolution` varchar(16) DEFAULT NULL,
  `depth` int(10) unsigned DEFAULT '0',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jdroplet_visits
-- ----------------------------
 
-- ----------------------------
-- Table structure for jdroplet_votes
-- ----------------------------
DROP TABLE IF EXISTS `jdroplet_votes`;
CREATE TABLE `jdroplet_votes` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(11) unsigned NOT NULL,
  `user_id` int(11) NOT NULL,
  `type` varchar(16) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of jdroplet_votes
-- ----------------------------
