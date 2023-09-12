# 创建表
create table example_db.page_visit(
    page_id INT NOT NULL COMMENT '页面id',
    visit_date date not null COMMENT '访问日期',
    visit_users BITMAP BITMAP_UNION NOT NULL COMMENT '访问用户id',
    visit_cnt bigint sum COMMENT '访问次数'
) ENGINE =OLAP
AGGREGATE KEY(page_id, visit_date)
DISTRIBUTED BY HASH(page_id) BUCKETS 1
PROPERTIES (
    "replication_num"="1",
    "storage_format"="DEFAULT"
);

# 插入数据
insert into example_db.page_visit values
(1, '2020-06-23', to_bitmap(13), 3),
(1, '2020-06-23', to_bitmap(23), 7),
(1, '2020-06-23', to_bitmap(33), 5),
(1, '2020-06-23', to_bitmap(13), 2),
(2, '2020-06-23', to_bitmap(23), 6);

# 创建一张明细数据表
create table example_db.page_visit_detail(
    visit_date date not null comment '访问日期',
    page_id int not null comment '页面id',
    user_id int not null comment '访问用户id',
    visit_cnt bigint comment '访问次数'
)
Duplicate KEY(visit_date, page_id)
DISTRIBUTED BY HASH(visit_date) BUCKETS 1
PROPERTIES (
    "replication_num"="1",
    "storage_format"="DEFAULT"
);

# 将数据写入聚合表
