CREATE TABLE `time_line` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `date` varchar(50) NOT NULL COMMENT '日期',
  `description` varchar(200) NOT NULL COMMENT '描述',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `utime` int(11) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='时间线表';

CREATE TABLE `pic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `seq` int(11) NOT NULL COMMENT '顺序',
  `url` varchar(100) NOT NULL COMMENT '图片URL',
  `thumb_url` varchar(100) NOT NULL COMMENT '缩略图图片URL',
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '图片标题',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `utime` int(11) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_seq` (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图片表';

CREATE TABLE `story` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '标题',
  `author` varchar(20) NOT NULL DEFAULT '' COMMENT '作者',
  `dt` int COMMENT '日期,20171015格式',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `utime` int(11) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_dt` (`dt`),
  KEY `idx_ctime` (`ctime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='故事表';

CREATE TABLE `story_content` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `story_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '故事ID',
  `content` mediumtext COMMENT '内容',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `utime` int(11) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_story_id` (`story_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='故事与内容映射表';

CREATE TABLE `xq` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `xq` varchar(500) NOT NULL DEFAULT '' COMMENT '心情',
  `author` varchar(20) NOT NULL DEFAULT '' COMMENT '作者',
  `stamp` int(11) NOT NULL COMMENT '标记',
  `view` int(11) NOT NULL COMMENT '看过的数目',
  `like` int(11) NOT NULL COMMENT '点赞的数目',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `utime` int(11) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_stamp` (`stamp`),
  KEY `idx_ctime` (`ctime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心情表';

CREATE TABLE `xq_pic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `stamp` int(11) NOT NULL COMMENT '标记',
  `url` varchar(100) COMMENT '图片地址',
  `thumb_url` varchar(100) NOT NULL COMMENT '缩略图图片URL',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `utime` int(11) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_stamp` (`stamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心情图片表';

CREATE TABLE `xq_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `xq_id` bigint(20) NOT NULL COMMENT '心情ID',
  `comment` varchar(300) COMMENT '留言',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `utime` int(11) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_xq_id` (`xq_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心情留言表';

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) COMMENT '用户名',
  `head_photo` varchar(100) NOT NULL DEFAULT '' COMMENT '头像',
  `password` varchar(100) COMMENT '密码',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `utime` int(11) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `user_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) COMMENT '用户ID',
  `token` varchar(100) COMMENT '登录token',
  `status` int(4) COMMENT '是否有效,0:有效,1:无效',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `utime` int(11) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户登录表';