create database example_db;

# 创建Duplicate模型
create table if not exists example_db.sale_order_dk(
    ticket_id BIGINT NOT NULL COMMENT '小票ID',
    ticket_line_id BIGINT NOT NULL COMMENT '小票行ID',
    ticket_date DATE COMMENT '订单日期',
    sku_code VARCHAR(60) COMMENT '商品SKU编码',
    shop_code VARCHAR(60) COMMENT  '门店编码',
    qty INT COMMENT '销售数量',
    amount decimal(22, 4) COMMENT '销售金额',
    last_update_time DATETIME NOT NULL COMMENT '数据更新时间'
) DUPLICATE KEY(ticket_id)
DISTRIBUTED BY HASH(ticket_id) BUCKETS 3
PROPERTIES(
    "replication_num"="1"
);

# 创建Aggregate模型
create table if not exists example_db.sale_order_ak(
    ticket_date DATE COMMENT '订单日期',
    sku_code VARCHAR(60) COMMENT '商品SKU编码',
    shop_code VARCHAR(60) COMMENT  '门店编码',
    ticket_id BITMAP BITMAP_UNION NULL COMMENT '小票ID',
    ticket_line_id BITMAP BITMAP_UNION NULL COMMENT '小票行ID',
    qty INT SUM COMMENT '销售数量',
    amount decimal(22, 4) SUM COMMENT '销售金额',
    last_update_time DATETIME REPLACE NOT NULL COMMENT '数据更新时间'
) AGGREGATE KEY(ticket_date, sku_code, shop_code)
DISTRIBUTED BY HASH(sku_code) BUCKETS 3
PROPERTIES(
    "replication_num"="1"
);

# 创建Unique模型
create table if not exists example_db.sale_order_uk(
   ticket_id BIGINT NOT NULL COMMENT '小票ID',
   ticket_line_id BIGINT NOT NULL COMMENT '小票行ID',
   ticket_date DATE COMMENT '订单日期',
   sku_code VARCHAR(60) COMMENT '商品SKU编码',
   shop_code VARCHAR(60) COMMENT  '门店编码',
   qty INT COMMENT '销售数量',
   amount decimal(22, 4) COMMENT '销售金额',
   last_update_time DATETIME NOT NULL COMMENT '数据更新时间'
) UNIQUE KEY(ticket_id, ticket_line_id)
DISTRIBUTED BY HASH(ticket_id) BUCKETS 3
PROPERTIES(
    "replication_num"="1"
);
