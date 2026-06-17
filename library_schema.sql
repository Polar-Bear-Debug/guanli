DROP TABLE IF EXISTS `bus_books`;
CREATE TABLE `bus_books` (
  `id` varchar(50) NOT NULL COMMENT '主键雪花ID',
  `book_name` varchar(100) DEFAULT NULL COMMENT '图书名称',
  `isbn` varchar(50) DEFAULT NULL COMMENT 'ISBN编码',
  `author` varchar(50) DEFAULT NULL COMMENT '著者',
  `stock` int DEFAULT NULL COMMENT '可用在库库存',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书主表';

DROP TABLE IF EXISTS `bus_readers`;
CREATE TABLE `bus_readers` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `reader_card` varchar(50) DEFAULT NULL COMMENT '读者卡号',
  `name` varchar(50) DEFAULT NULL COMMENT '读者姓名',
  `level` int DEFAULT NULL COMMENT '借阅分级',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='读者表';

DROP TABLE IF EXISTS `bus_borrow_records`;
CREATE TABLE `bus_borrow_records` (
  `id` varchar(50) NOT NULL COMMENT '流转流水号',
  `book_id` varchar(50) DEFAULT NULL COMMENT '关联图书主键',
  `reader_id` varchar(50) DEFAULT NULL COMMENT '借阅读者卡号',
  `borrow_time` datetime DEFAULT NULL COMMENT '出库时间',
  `status` varchar(20) DEFAULT NULL COMMENT '流转状态(待审批/已批准/已驳回)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='借阅轨迹流转表';

INSERT INTO `bus_books` VALUES ('1', 'Java核心技术高级进阶', '9787121345620', 'lenovo', 10);
INSERT INTO `bus_books` VALUES ('2', 'MySQL必知必会(第三版)', '9787115162632', 'Ben Forta', 5);
