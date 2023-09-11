# 插入数据到Duplicate模型表
INSERT INTO example_db.sale_order_dk(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values ('10000', '1', '2022-01-01', '982419326950', 'X0303417', '4', '498.0000', '2022-01-01 19:20:23');

INSERT INTO example_db.sale_order_dk(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values ('10000', '2', '2022-01-01', '982428631508', 'X0303417', '3', '486.0000', '2022-01-01 19:20:23');

INSERT INTO example_db.sale_order_dk(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values ('10000', '2', '2022-01-01', '982428631508', 'X0303417', '2', '583.0000', '2022-01-01 22:20:23');

INSERT INTO example_db.sale_order_dk(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values ('10000', '1', '2022-01-01', '982419326950', 'X0303417', '4', '726.0000', '2022-01-02 22:20:23');

INSERT INTO example_db.sale_order_dk(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values ('10002', '1', '2022-01-02', '982419326788', 'X2318236', '4', '632.0000', '2022-01-02 10:20:23');

INSERT INTO example_db.sale_order_dk(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values ('10002', '2', '2022-01-02', '982419326788', 'X2318236', '5', '1663.0000', '2022-01-02 10:20:23');

INSERT INTO example_db.sale_order_dk(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values ('10003', '1', '2022-01-03', '982419326950', 'X2318236', '2', '542.0000', '2022-01-03 18:20:23');

# 插入数据到Aggregate模型表
INSERT INTO example_db.sale_order_ak(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values (bitmap_hash('10000'), bitmap_hash('1'), '2022-01-01', '982419326950', 'X0303417', '4', '498.0000', '2022-01-01 19:20:23');

INSERT INTO example_db.sale_order_ak(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values (bitmap_hash('10000'), bitmap_hash('2'), '2022-01-01', '982428631508', 'X0303417', '3', '486.0000', '2022-01-01 19:20:23');

INSERT INTO example_db.sale_order_ak(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values (bitmap_hash('10000'), bitmap_hash('2'), '2022-01-01', '982428631508', 'X0303417', '2', '583.0000', '2022-01-01 22:20:23');

INSERT INTO example_db.sale_order_ak(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values (bitmap_hash('10000'), bitmap_hash('1'), '2022-01-01', '982419326950', 'X0303417', '4', '726.0000', '2022-01-02 22:20:23');

INSERT INTO example_db.sale_order_ak(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values (bitmap_hash('10002'), bitmap_hash('1'), '2022-01-02', '982419326788', 'X2318236', '4', '632.0000', '2022-01-02 10:20:23');

INSERT INTO example_db.sale_order_ak(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values (bitmap_hash('10002'), bitmap_hash('2'), '2022-01-02', '982419326788', 'X2318236', '5', '1663.0000', '2022-01-02 10:20:23');

INSERT INTO example_db.sale_order_ak(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values (bitmap_hash('10003'), bitmap_hash('1'), '2022-01-03', '982419326950', 'X2318236', '2', '542.0000', '2022-01-03 18:20:23');

# 插入数据到Unique模型表
INSERT INTO example_db.sale_order_uk(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values ('10000', '1', '2022-01-01', '982419326950', 'X0303417', '4', '498.0000', '2022-01-01 19:20:23');

INSERT INTO example_db.sale_order_uk(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values ('10000', '2', '2022-01-01', '982428631508', 'X0303417', '3', '486.0000', '2022-01-01 19:20:23');

INSERT INTO example_db.sale_order_uk(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values ('10000', '2', '2022-01-01', '982428631508', 'X0303417', '2', '583.0000', '2022-01-01 22:20:23');

INSERT INTO example_db.sale_order_uk(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values ('10000', '1', '2022-01-01', '982419326950', 'X0303417', '4', '726.0000', '2022-01-02 22:20:23');

INSERT INTO example_db.sale_order_uk(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values ('10002', '1', '2022-01-02', '982419326788', 'X2318236', '4', '632.0000', '2022-01-02 10:20:23');

INSERT INTO example_db.sale_order_uk(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values ('10002', '2', '2022-01-02', '982419326788', 'X2318236', '5', '1663.0000', '2022-01-02 10:20:23');

INSERT INTO example_db.sale_order_uk(ticket_id, ticket_line_id, ticket_date, sku_code, shop_code, qty, amount, last_update_time)
values ('10003', '1', '2022-01-03', '982419326950', 'X2318236', '2', '542.0000', '2022-01-03 18:20:23');